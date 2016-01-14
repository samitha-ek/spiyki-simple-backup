/*
   Copyright 2015 Samitha Ekanayake

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.spiyki.simplebackup.utils;
import java.io.*;
/**
 *
 * @author samitha
 */
public class RestoreBackup {
    //This restores the backup to a given restore point
    //RestoreHistory file has the following format
    //Date&Time1||BackupChangesFolder1
    //Date&Time2||BackupChangesFolder2
    public String[] BackupChangeFolders;
    public String[] BackupDateTimes;    
    public boolean BackupHistoryFound=false;
    
    
    int CurrentRestorePoints;
    int RestorePoints;
    String BackupHistory;
            
    public RestoreBackup(BackupFileHandle thisBackupSet, int BackupRecordSet){
        //Load the information in restore folder
        RestorePoints=Integer.parseInt(((BackupInfo)thisBackupSet.Backups.get(BackupRecordSet)).GetRestorePoints());
        BackupChangeFolders=new String[RestorePoints];
        BackupDateTimes=new String[RestorePoints];
        
        try{
            BackupHistory=((BackupInfo)thisBackupSet.Backups.get(BackupRecordSet)).GetDestinationFolder() + File.separator +((BackupInfo)thisBackupSet.Backups.get(BackupRecordSet)).GetBackupName()+"History.Log";
            File ThisBackupFile=new File(BackupHistory);
            BackupHistoryFound=false;
            if(ThisBackupFile.exists()){
                //Check for the file           
                if(ReadFromFile(BackupHistory)){
                    BackupHistoryFound=true;
                }
            }             
        }catch(IOException e){
            
        }
    }
    
    private boolean ReadFromFile(String strFileName) throws IOException{
        BufferedReader inputStream = null;
        int i=0;
        try {
            inputStream = new BufferedReader(new FileReader(strFileName));
            String l;            
            while ((l = inputStream.readLine()) != null) {
                String[] tempValues=l.split("--");
                if(i<RestorePoints){
                    BackupDateTimes[i]=tempValues[0];
                    BackupChangeFolders[i]=tempValues[1];
                }else{
                    DeleteUnusedBackupHistory(tempValues[1]);
                }
                i++;                               
            }
        }  finally {
            if (inputStream != null) {                
                inputStream.close();                
            }
        }
        if(i<RestorePoints){
            CurrentRestorePoints=i;
        }else{
            CurrentRestorePoints=RestorePoints;
        }
        if(i==0){
            return false;
        }else{
            return true;
        }
    }    
    
    private String CreateHistoryString(){        
        String newline = System.getProperty("line.separator");
        String strHistoryMsg="";
        for(int i=0;i<CurrentRestorePoints;i++){
            strHistoryMsg+=BackupDateTimes[i]+"--"+BackupChangeFolders[i]+newline;
        }
        return strHistoryMsg;
    }
    
    public void AddNewBackupRecord(String DateTime,String BackupPath){
        if(CurrentRestorePoints<RestorePoints){
            for(int i=CurrentRestorePoints;i>0;i--){
                BackupChangeFolders[i]=BackupChangeFolders[i-1];
                BackupDateTimes[i]=BackupDateTimes[i-1];
            }
            BackupChangeFolders[0]=BackupPath;
            BackupDateTimes[0]=DateTime;
            CurrentRestorePoints++;
        }else{
            DeleteUnusedBackupHistory(BackupChangeFolders[RestorePoints-1]);
            for(int i=RestorePoints-1;i>0;i--){
                BackupChangeFolders[i]=BackupChangeFolders[i-1];
                BackupDateTimes[i]=BackupDateTimes[i-1];
            }          
            BackupChangeFolders[0]=BackupPath;
            BackupDateTimes[0]=DateTime;
        }
        
        try{
            FileOverWrite saveFile=new FileOverWrite(BackupHistory, CreateHistoryString());
            if(ReadFromFile(BackupHistory)){
                BackupHistoryFound=true;
            }
        }catch(IOException e){
            
        }
    }
    
   
    private void DeleteUnusedBackupHistory(String BackupDeletePath){
        File DeleteFolder=new File(BackupDeletePath);
        DeleteFolderNoBackup thisDelete=new DeleteFolderNoBackup(DeleteFolder);
    }
    
    public void RestoreBackupFiles(BackupFileHandle thisBackupSet, int BackupRecordSet, int RestorePoint){
        RestoreManager thisRestore=new RestoreManager();
        thisRestore.InitialRestore(thisBackupSet, BackupRecordSet);   

        
        
        try{
        for(int i=0;i<RestorePoint;i++){
            DeleteNewFolders((BackupInfo)thisBackupSet.Backups.get(BackupRecordSet),BackupChangeFolders[i]);
            DeleteNewFiles((BackupInfo)thisBackupSet.Backups.get(BackupRecordSet),BackupChangeFolders[i]);
            UpdateModifiedFiles((BackupInfo)thisBackupSet.Backups.get(BackupRecordSet),BackupChangeFolders[i]);
            AddDeletedFolders((BackupInfo)thisBackupSet.Backups.get(BackupRecordSet),BackupChangeFolders[i]);
            AddDeletedFiles((BackupInfo)thisBackupSet.Backups.get(BackupRecordSet),BackupChangeFolders[i]);
        }
        }catch(IOException e){
            
        }
        
    }
    
    private void DeleteNewFolders(BackupInfo thisBackup, String BackupFolder) throws IOException{
        String DestFolder=thisBackup.GetDestinationFolder();
        BufferedReader inputStream = null;
        try {
            inputStream = new BufferedReader(new FileReader(BackupFolder + File.separator + "AddFolderLog.log"));
            String l;            
            while ((l = inputStream.readLine()) != null) {
                String thisReading=l;
                /*
                
                */
                String tempRestoreFolder=thisBackup.GetRestoreFolder();
                String tempDeleteFolder=thisReading.replaceFirst(java.util.regex.Matcher.quoteReplacement(DestFolder), java.util.regex.Matcher.quoteReplacement(tempRestoreFolder));
                File DeleteFolder=new File(tempDeleteFolder);
                
                DeleteFolderNoBackup thisFolderDelete=new DeleteFolderNoBackup(DeleteFolder);
            }
        }catch(Exception e){
            //System.out.print(e.getMessage());
        }finally {
            if (inputStream != null) {                
                inputStream.close();                
            }
        }
    }
    
    private void DeleteNewFiles(BackupInfo thisBackup, String BackupFolder) throws IOException{
        String DestFolder=thisBackup.GetDestinationFolder();
        BufferedReader inputStream = null;
        try {
            inputStream = new BufferedReader(new FileReader(BackupFolder + File.separator + "NewLog.log"));
            String l;            
            while ((l = inputStream.readLine()) != null) {
                String[] thisReading=l.split(":-:");
                
                /*
                
                */
                String tempRestoreFolder=thisBackup.GetRestoreFolder();
                String tempDeleteFile=thisReading[0].replaceFirst(java.util.regex.Matcher.quoteReplacement(DestFolder), java.util.regex.Matcher.quoteReplacement(tempRestoreFolder));

                File DeleteFile = new File(tempDeleteFile);
                if (DeleteFile.exists()) {
                    if (DeleteFile.delete()) {
                        System.out.println("Sucessfully Deleted newly added file from the backup folder");
                    }
                }
            }
        }catch(Exception e){
            //System.out.print(e.getMessage());
        }finally {
            if (inputStream != null) {                
                inputStream.close();                
            }
        }        
    }
    
    private void UpdateModifiedFiles(BackupInfo thisBackup, String BackupFolder) throws IOException{
        String DestFolder=thisBackup.GetDestinationFolder();
        BufferedReader inputStream = null;
        try {
            inputStream = new BufferedReader(new FileReader(BackupFolder + File.separator + "ModLog.log"));
            String l;            
            while ((l = inputStream.readLine()) != null) {
                String[] thisReading=l.split(":-:");
                
                /*
                              
                */
                String tempRestoreFolder=thisBackup.GetRestoreFolder(); 
                String tempFileToModify=thisReading[0].replaceFirst(java.util.regex.Matcher.quoteReplacement(DestFolder), java.util.regex.Matcher.quoteReplacement(tempRestoreFolder));
                File toModify=new File(tempFileToModify); 
                
                String tempFileToReplace=thisReading[1];
                File ReplaceFrom=new File(tempFileToReplace);
                CopyFile backMod=new CopyFile(ReplaceFrom,toModify);
                System.out.println("Sucessfully replaced the modifed file with the previous version from the backup folder");
            }
        }catch(Exception e){
            //System.out.print(e.getMessage());
        }finally {
            if (inputStream != null) {                
                inputStream.close();                
            }
        }         
    }
    
    private void AddDeletedFolders(BackupInfo thisBackup, String BackupFolder) throws IOException{
        String DestFolder=thisBackup.GetDestinationFolder();
        BufferedReader inputStream = null;
        try {
            inputStream = new BufferedReader(new FileReader(BackupFolder + File.separator + "DelFolderLog.log"));
            String l;            
            while ((l = inputStream.readLine()) != null) {
                String thisReading=l;
                
                /*
                
                */
                String tempRestoreFolder=thisBackup.GetRestoreFolder();
                String tempDeletedFolder=thisReading.replaceFirst(java.util.regex.Matcher.quoteReplacement(DestFolder), java.util.regex.Matcher.quoteReplacement(tempRestoreFolder));
                File DeletedFolder=new File(tempDeletedFolder);
                
                if(!DeletedFolder.exists()){
                    DeletedFolder.mkdir();
                }
            }
        }catch(Exception e){
            //System.out.print(e.getMessage());
        }finally {
            if (inputStream != null) {                
                inputStream.close();                
            }
        }        
    }
    
    private void AddDeletedFiles(BackupInfo thisBackup, String BackupFolder) throws IOException{
        String DestFolder=thisBackup.GetDestinationFolder();
        BufferedReader inputStream = null;
        try {
            inputStream = new BufferedReader(new FileReader(BackupFolder + File.separator + "DeleteLog.log"));
            String l;            
            while ((l = inputStream.readLine()) != null) {
                String[] thisReading=l.split(":-:");
                /*
                
                */
                String tempRestoreFolder=thisBackup.GetRestoreFolder();
                String tempFileToModify=thisReading[0].replaceFirst(java.util.regex.Matcher.quoteReplacement(DestFolder), java.util.regex.Matcher.quoteReplacement(tempRestoreFolder));
                File toModify=new File(tempFileToModify);
                              
                String tempFileToReplace=thisReading[1];
                File ReplaceFrom=new File(tempFileToReplace);
                CopyFile backMod=new CopyFile(ReplaceFrom,toModify);
            }
        }catch(Exception e){
            //System.out.print(e.getMessage());
        }finally {
            if (inputStream != null) {                
                inputStream.close();                
            }
        }            
    }
}

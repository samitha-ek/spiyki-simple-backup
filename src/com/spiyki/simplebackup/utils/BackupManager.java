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
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import com.spiyki.simplebackup.gui.*;
/**
 *
 * @author samitha_2
 */
public class BackupManager {
    public static int NumberOfModifiedFilesGlobal=1;
    public static int NumberOfNewFilesGlobal=1;
    public static int NumberOfDeletedFilesGlobal=1;
    public static int FilesInTheSource=0;
    public static int FilesProcessedInTheSource=0;
    //public static int NumberOfNewFoldersGlobal=1;
    //public static int NumberOfDeletedFoldersGlobal=1;
    guiSimpleBackupMain ThisGUI;
    
    public BackupManager( guiSimpleBackupMain GUI){
        ThisGUI=GUI;
    }
    
    public boolean BackupNow(BackupFileHandle thisBackupSet, int BackupRecordSet){
        boolean BackupStat=true;
        NumberOfModifiedFilesGlobal=1;
        NumberOfNewFilesGlobal=1;
        NumberOfDeletedFilesGlobal=1;
        FilesInTheSource=0;
        FilesProcessedInTheSource=0;
        //NumberOfNewFoldersGlobal=1;
        //NumberOfDeletedFoldersGlobal=1;
        
        String DatedBackup;
        String DestFolder;
        File tempFolder;
        System.out.println("Executing backup " + ((BackupInfo)thisBackupSet.Backups.get(BackupRecordSet)).GetBackupName());
        //String[] BackUpInformation=new String[10];
        //String[] BackupFodlers = new String[10];
        Calendar today = Calendar.getInstance();
        Calendar statTime = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        System.out.println("File Backup Started at " + statTime.getTime());
//        ThisGUI.pgrsBackup.setVisible(true);
        try{
            //FileRead thisBackUp = new FileRead(backUpFile);        
            //BackUpInformation = thisBackUp.ReadFile();
            //String[] BackupFodlers=BackUpInformation[0].split("--");
            String SourceFolder=((BackupInfo)thisBackupSet.Backups.get(BackupRecordSet)).GetSourceFolder();
            System.out.println("Checking for source folder " + SourceFolder);         
            tempFolder=new File(SourceFolder);
            if(tempFolder.exists()){
                
                System.out.println("OK");                
                DestFolder=((BackupInfo)thisBackupSet.Backups.get(BackupRecordSet)).GetDestinationFolder() + File.separator + ((BackupInfo)thisBackupSet.Backups.get(BackupRecordSet)).GetBackupName();
                System.out.println("Checking for destination folder " + DestFolder);
                tempFolder=new File(DestFolder);
                if(tempFolder.exists()){
                    System.out.println("OK");
                }else{
                    System.out.println("NOT found, creating desination folder");
                    tempFolder.mkdirs();
                }                                     
                System.out.println("Starting backup process...");
                ThisGUI.lblMainStat.setText("Starting backup process...");
                //FileCount filesInThisFolder=new FileCount();
                //FilesInTheSource=filesInThisFolder.getFileCount(SourceFolder);
                //ThisGUI.pgrsBackup.setMaximum(2*FilesInTheSource);
                //Main.ThisSourceFileGlobal=BackupFodlers[1];
                String BackupDateTime=sdf.format(statTime.getTime());
                DatedBackup=((BackupInfo)thisBackupSet.Backups.get(BackupRecordSet)).GetDestinationFolder() + File.separator + ((BackupInfo)thisBackupSet.Backups.get(BackupRecordSet)).GetBackupName()+BackupDateTime;
                System.out.println("Checking for destination folder " +DatedBackup);
                tempFolder=new File(DatedBackup);
                if(tempFolder.exists()){
                    System.out.println("OK");
                }else{
                    System.out.println("NOT found, creating backup folder");
                    tempFolder.mkdirs();
                } 
                ScanFolder ThisFolder = new ScanFolder(SourceFolder,DestFolder,DatedBackup,this);
                RestoreBackup thisRestoreSettings=new RestoreBackup(thisBackupSet, BackupRecordSet);
                thisRestoreSettings.AddNewBackupRecord(statTime.getTime().toString(), DatedBackup);
            }else{
                System.out.println("NOT found, aborting the backup process. Check your source folder name in the backup information file");
                BackupStat=false;
            }                                
        }catch(IOException e){
            System.out.println("Could not locate the backup info file");
            BackupStat=false;
        }
        Calendar endTime = Calendar.getInstance();
        System.out.println("File Backup Finished at " + endTime.getTime()); 
//        ThisGUI.pgrsBackup.setVisible(false);
        if(BackupStat){return true;}else{return false;}
        //System.out.println("Time taken for backup is" + endTime.getTime()- statTime.getTime());         
    }
}

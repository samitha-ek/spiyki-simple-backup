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
import java.util.ArrayList;

/**
 *
 * @author samitha_2
 */
public class BackupFileHandle {
    int CurrentRecordSet;
    boolean NewRecord;
    ArrayList Backups=new ArrayList();
    private File ThisBackupFile;

    
    
    public BackupFileHandle(){
        try{
            //Constructor for the BackupFileHandle
            ThisBackupFile=new File("BackupInfo.dat");
            //On the initialization of the class, load the Info file.
           if(!ThisBackupFile.exists()){
               //Check for the file           
               ThisBackupFile.createNewFile();
               //If the file does not exists, then create a new file.           
               CreateSampleDataSet();
               //Generate Sample data on the file           
               SaveTheBackupConfiguration();
               //Save the current configuration on the file
            }        
            ReadFromFile("BackupInfo.dat");
            //Read the current configuration and load it to memory
        }catch(IOException e){
            
        }
    }
    
    private boolean ReadFromFile(String strFileName) throws IOException{
        Backups.clear();
        BufferedReader inputStream = null;
        int i=0;
        try {
            inputStream = new BufferedReader(new FileReader(strFileName));
            String l;            
            while ((l = inputStream.readLine()) != null) {
                  BackupInfo tempBackup=new BackupInfo(l);
                  Backups.add(tempBackup);
                  i++;                               
            }
        }  finally {
            if (inputStream != null) {                
                inputStream.close();                
            }
        }
        if(i==0){
            return false;
        }else{
            return true;
        }
    }
    
    private void CreateSampleDataSet(){
        String tempBackupString="Backup Name--Source Folder--Destination Folder--0--0--0--5--Restore Folder";
        BackupInfo tempBackup=new BackupInfo(tempBackupString);
        Backups.add(tempBackup);                
    }
    
    public void SaveTheBackupConfiguration() throws IOException{
        String newline = System.getProperty("line.separator");
        String RecordToWrite="";
        for(int i=0;i<Backups.size();i++){
            RecordToWrite+=((BackupInfo)Backups.get(i)).GetBackupString()+newline;            
        }
        FileOverWrite saveFile=new FileOverWrite("BackupInfo.dat", RecordToWrite);
    }
}

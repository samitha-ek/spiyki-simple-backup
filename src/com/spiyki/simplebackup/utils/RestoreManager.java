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
/**
 *
 * @author samitha_2
 */
public class RestoreManager {
    public void InitialRestore(BackupFileHandle thisBackupSet, int BackupRecordSet){
        
        String DatedBackup;
        String DestFolder;
        File tempFolder;
        System.out.println("Executing restore for " + ((BackupInfo)thisBackupSet.Backups.get(BackupRecordSet)).GetBackupName());
        //String[] BackUpInformation=new String[10];
        //String[] BackupFodlers = new String[10];
        Calendar today = Calendar.getInstance();
        Calendar statTime = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        System.out.println("File Restore Started at " + statTime.getTime());
        try{
            //FileRead thisBackUp = new FileRead(backUpFile);        
            //BackUpInformation = thisBackUp.ReadFile();
            //String[] BackupFodlers=BackUpInformation[0].split("--");
            String SourceFolder=((BackupInfo)thisBackupSet.Backups.get(BackupRecordSet)).GetDestinationFolder() + File.separator + ((BackupInfo)thisBackupSet.Backups.get(BackupRecordSet)).GetBackupName();
            System.out.println("Checking for backup folder " + SourceFolder);         
            tempFolder=new File(SourceFolder);
            if(tempFolder.exists()){
                
                System.out.println("OK");                
                DestFolder=((BackupInfo)thisBackupSet.Backups.get(BackupRecordSet)).GetRestoreFolder() + File.separator + ((BackupInfo)thisBackupSet.Backups.get(BackupRecordSet)).GetBackupName();
                System.out.println("Checking for restore folder " + DestFolder);
                tempFolder=new File(DestFolder);
                if(tempFolder.exists()){
                    System.out.println("OK");
                }else{
                    System.out.println("NOT found, creating restore folder");
                    tempFolder.mkdirs();
                }                                     
                System.out.println("Starting restore process...");

                ScanFolderNoBackup ThisFolder = new ScanFolderNoBackup(SourceFolder,DestFolder);
            }else{
                System.out.println("NOT found, aborting the backup process. Check your source folder name in the backup information file");
            }                                
        }catch(IOException e){
            System.out.println("Could not locate the backup info file");
        }
        Calendar endTime = Calendar.getInstance();
        System.out.println("Initial restore finished at " + endTime.getTime()); 
        //System.out.println("Time taken for backup is" + endTime.getTime()- statTime.getTime());         
    }
}

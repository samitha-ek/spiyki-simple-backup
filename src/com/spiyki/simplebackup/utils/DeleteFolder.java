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

/**
 *
 * @author samitha_2
 */
//import java.io.*;
import java.io.File;
//import java.nio.channels.*;
import java.io.IOException;

public class DeleteFolder {

    private File[] ListInThisFolder;
    BackupManager thisMgr;
    
    public DeleteFolder(File folderToDelete,String BackUpFolderName,BackupManager BkMgr){
        thisMgr=BkMgr;
        String strBackupFolder=BackUpFolderName;         
        File tempBackupFile;        
        ListInThisFolder=folderToDelete.listFiles();
        DeleteFolder tempDeletingFolder;
        for (int i=0; i<ListInThisFolder.length;i++){
          
            if(ListInThisFolder[i].isFile()){
                System.out.println("Deleting file " + ListInThisFolder[i].getAbsolutePath());
                try{
                    String srtFileNameToDatedBackup=strBackupFolder + File.separator + "D"+thisMgr.NumberOfDeletedFilesGlobal++;
                    tempBackupFile=new File(srtFileNameToDatedBackup);             
                    CopyFile datedBackDel=new CopyFile(ListInThisFolder[i],tempBackupFile);
                    if(ListInThisFolder[i].delete()){
                        System.out.println("\t File sucessfully deleted");                            
                        String DeleteFileDetails=ListInThisFolder[i].getAbsolutePath()+":-:"+tempBackupFile.getAbsolutePath();
                        FileAppend deleteLog=new FileAppend(strBackupFolder + File.separator + "DeleteLog.log",DeleteFileDetails);
                    }else{
                        System.out.println("\t Failed to delete the file");
                    }
                }catch(IOException e){
                    //throw e;
                }                     
            }else if(ListInThisFolder[i].isDirectory()){
                tempDeletingFolder= new DeleteFolder(ListInThisFolder[i],strBackupFolder,thisMgr);
            }
        }
        System.out.println("Deleting folder -- " + folderToDelete.getAbsolutePath());
        if(folderToDelete.delete()){
            System.out.println("\t Folder sucessfully deleted");
            String DelFolderDetils=folderToDelete.getAbsolutePath();
            try{
                FileAppend AFLog=new FileAppend(strBackupFolder + File.separator + "DelFolderLog.log",DelFolderDetils);
            }catch(IOException e){
                
            }
        }else{
            System.out.println("\t Failed to delete the Folder");
        }
    }            
}
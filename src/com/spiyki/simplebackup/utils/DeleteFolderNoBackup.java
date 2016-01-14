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
//import java.io.IOException;
/**
 *
 * @author samitha
 */
public class DeleteFolderNoBackup {
private File[] ListInThisFolder;
    
    public DeleteFolderNoBackup(File folderToDelete){
        //String strBackupFolder=BackUpFolderName;         
//        File tempBackupFile;        
        try{
            ListInThisFolder=folderToDelete.listFiles();
            DeleteFolderNoBackup tempDeletingFolder;
            for (int i=0; i<ListInThisFolder.length;i++){
                if(ListInThisFolder[i].isFile()){
                    System.out.println("Deleting file " + ListInThisFolder[i].getAbsolutePath());
    //                try{
    //                    String srtFileNameToDatedBackup=strBackupFolder+"\\D"+Main.NumberOfDeletedFilesGlobal++;
    //                    tempBackupFile=new File(srtFileNameToDatedBackup);             
    //                    copyFile datedBackDel=new copyFile(ListInThisFolder[i],tempBackupFile);
                        if(ListInThisFolder[i].delete()){
                            System.out.println("\t File sucessfully deleted");                            
    //                        String DeleteFileDetails=ListInThisFolder[i].getAbsolutePath()+":-:"+tempBackupFile.getAbsolutePath();
    //                        FileAppend deleteLog=new FileAppend(strBackupFolder+"\\DeleteLog.log",DeleteFileDetails);
                        }else{
                            System.out.println("\t Failed to delete the file");
                        }
    //                }catch(IOException e){
    //                    //throw e;
    //                }                     
                }else if(ListInThisFolder[i].isDirectory()){
                    tempDeletingFolder= new DeleteFolderNoBackup(ListInThisFolder[i]);
                }
            }
            System.out.println("Deleting folder -- " + folderToDelete.getAbsolutePath());
            if(folderToDelete.delete()){
                System.out.println("\t Folder sucessfully deleted");
            }else{
                System.out.println("\t Failed to delete the Folder");
            }
        }catch(Exception e){
            
        }
    }     
}

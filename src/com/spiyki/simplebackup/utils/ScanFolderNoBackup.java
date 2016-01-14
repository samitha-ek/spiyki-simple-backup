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
 * @author samitha_2
 */
public class ScanFolderNoBackup {
    private String strFolderName;
    private String strToFolder;
    private String strBackupFolder;
    private File ThisFolder;
    private File ToFolder;
    private File[] ListInThisFolder;
    private ScanFolderNoBackup ListOfFolders[];
    private File ListOfFiles[];
    private int FileCount=0,FolderCount=0;
    
    public ScanFolderNoBackup(String SourceFolderName,String DestinationFolderName) throws IOException{
        strFolderName=SourceFolderName;
        strToFolder=DestinationFolderName;
        //strBackupFolder=BackUpFolderName;
        ThisFolder=new File(strFolderName);
        ToFolder =new File(strToFolder);
        ListInThisFolder=ThisFolder.listFiles();
        CreateListOfFilesAndFolders();
        BackUpFiles();       
    }
    
    private void CreateListOfFilesAndFolders() throws IOException{     
        int tempFolderCnt=0,tempFileCnt=0;
        
        System.out.println("Checking if the Destination folder is available -- " + strToFolder);
        if(ToFolder.exists()){
            System.out.println("Destination folder Exists");
        }else{
            System.out.println("Destination folder Does not Exists... Creating one");
            if(ToFolder.mkdir()){
                System.out.println("Created the folder -- " + ToFolder);
            }else{
                System.out.println("Failed creating the folder -- " + ToFolder);
            }              
        }
        
        for (int i = 0; i < ListInThisFolder.length; i++) {
            if(ListInThisFolder[i].isDirectory()){
                tempFolderCnt++;
            }else if(ListInThisFolder[i].isFile()){
                tempFileCnt++;
            }
        }
        ListOfFiles=new File[tempFileCnt];
        ListOfFolders=new ScanFolderNoBackup[tempFolderCnt];
        for (int i = 0; i < ListInThisFolder.length; i++) {
            if(ListInThisFolder[i].isDirectory()){
                ListOfFolders[FolderCount]=new ScanFolderNoBackup(ListInThisFolder[i].getAbsolutePath(),strToFolder+File.separator + ListInThisFolder[i].getName());                
                System.out.println("DestinationFolder --" +strToFolder+File.separator + ListInThisFolder[i].getName());
                FolderCount++;
            }else if(ListInThisFolder[i].isFile()){
                ListOfFiles[FileCount]=new File(ListInThisFolder[i].getAbsolutePath());
                FileCount++;
            }
        }
    }       
    
    private void BackUpFiles() throws IOException{                
        String strFileNameInBackup;    
        //String srtFileNameToDatedBackup;        
        File tempBackupFile;
        File tempDestFile;
        File tempSrcFile;
        File[] tempDestFiles;
        DeleteFolderNoBackup tempDeleteFolder;
        //System.out.println(strBackupFolder);
        System.out.println("Checking for new or modified files in the Source folder");
        for(int i=0; i<FileCount;i++){
            System.out.println("Source file name -- " + ListOfFiles[i].getAbsolutePath());
            strFileNameInBackup=strToFolder + File.separator +  ListOfFiles[i].getName();
            System.out.println("Check if the Destination file name -- " + strFileNameInBackup + " Exists");
            tempDestFile=new File(strFileNameInBackup);
            if(tempDestFile.exists()){
                System.out.println("\t The file exists, checking for the modification date");
                if(tempDestFile.lastModified()!=ListOfFiles[i].lastModified()){
                    //System.out.println("Check if the Destination file name -- " + strFileNameInBackup + " Exists");
                    System.out.println("\t File has been modified since the last backup...");
                    System.out.println("\t\t Updating backup file...");
                    CopyFile backMod=new CopyFile(ListOfFiles[i],tempDestFile);
                }else{
                    System.out.println("\t Nothing changed in the file");
                }
            }else{
                //System.out.println("Check if the Destination file name -- " + strFileNameInBackup + " Exists");
                System.out.println("\t The file does not exists, this is a new file");
                System.out.println("\t\t Updating backup file...");
                //tempBackupFile=new File(srtFileNameToDatedBackup);
                //copyFile datedBackNew=new copyFile(ListOfFiles[i],tempBackupFile);
                CopyFile backNew=new CopyFile(ListOfFiles[i],tempDestFile);
                //String NewFileDetails=tempDestFile.getAbsolutePath()+":-:"+tempBackupFile.getAbsolutePath();
            }
        }
        //System.out.println("Checking for deleted files in the source folder");
        tempDestFile=new File(strToFolder);
        tempDestFiles=tempDestFile.listFiles();
        for(int i=0;i<tempDestFiles.length;i++){
            tempSrcFile=new File(strFolderName + File.separator +  tempDestFiles[i].getName());
            System.out.println("Checking for the source file -- " + tempSrcFile.getAbsolutePath());
            if(tempSrcFile.exists()){
                System.out.println("\t File is not deleted");
            }else{
                System.out.println("\t File is deleted...");                       
                System.out.println("\t\t Making changes in the backup");
                if(tempDestFiles[i].isFile()){
                    //tempBackupFile=new File(srtFileNameToDatedBackup);
                    //copyFile datedBackDel=new copyFile(tempDestFiles[i],tempBackupFile);    
                    String FileToDelete=tempDestFiles[i].getAbsolutePath();
                    if(tempDestFiles[i].delete()){
                        System.out.println("\t\t Backup file is sucessfully deleted...");             
                        //String DeleteFileDetails=FileToDelete+":-:"+tempBackupFile.getAbsolutePath();

                    }else{
                        System.out.println("\t\t Can not delete backup file...");
                    }
                }else if (tempDestFiles[i].isDirectory()){
                    tempDeleteFolder= new DeleteFolderNoBackup(tempDestFiles[i]);
                }
                
            }                
        }
    }
}

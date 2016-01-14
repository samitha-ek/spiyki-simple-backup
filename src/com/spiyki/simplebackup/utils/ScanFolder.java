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
import java.text.SimpleDateFormat;
/**
 *
 * @author samitha_2
 */
import java.io.*;
import java.io.File;

public class ScanFolder{
    private String strFolderName;
    private String strToFolder;
    private String strBackupFolder;
    private File ThisFolder;
    private File ToFolder;
    private File[] ListInThisFolder;
    private ScanFolder ListOfFolders[];
    private File ListOfFiles[];
    private int FileCount=0,FolderCount=0;
    BackupManager thisMgr;
    
    public ScanFolder(String SourceFolderName,String DestinationFolderName,String BackUpFolderName,BackupManager BkMgr) throws IOException{
        thisMgr=BkMgr;
        strFolderName=SourceFolderName;
        strToFolder=DestinationFolderName;
        strBackupFolder=BackUpFolderName;
        ThisFolder=new File(strFolderName);
        ToFolder =new File(strToFolder);
        ListInThisFolder=ThisFolder.listFiles();
        CreateListOfFilesAndFolders();
        BackUpFiles();       
    }
    
    private void CreateListOfFilesAndFolders() throws IOException{     
        int tempFolderCnt=0,tempFileCnt=0;
        
//        System.out.println("Checking if the Destination folder is available -- " + strToFolder);
        if(ToFolder.exists()){
//            System.out.println("Destination folder Exists");
        }else{
//            System.out.println("Destination folder Does not Exists... Creating one");
            if(ToFolder.mkdir()){
//                System.out.println("Created the folder -- " + ToFolder);
                String AddFolderDetils=ToFolder.getAbsolutePath();
                FileAppend AFLog=new FileAppend(strBackupFolder + File.separator + "AddFolderLog.log",AddFolderDetils);
            }else{
//                System.out.println("Failed creating the folder -- " + ToFolder);
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
        ListOfFolders=new ScanFolder[tempFolderCnt];
        for (int i = 0; i < ListInThisFolder.length; i++) {
            if(ListInThisFolder[i].isDirectory()){
                ListOfFolders[FolderCount]=new ScanFolder(ListInThisFolder[i].getAbsolutePath(),strToFolder + File.separator + ListInThisFolder[i].getName(),strBackupFolder,thisMgr);                
                FolderCount++;
            }else if(ListInThisFolder[i].isFile()){
                ListOfFiles[FileCount]=new File(ListInThisFolder[i].getAbsolutePath());
                FileCount++;
            }
        }
    }       
    
    private void BackUpFiles() throws IOException{                
        String strFileNameInBackup;    
        String srtFileNameToDatedBackup;        
        File tempBackupFile;
        File tempDestFile;
        File tempSrcFile;
        File[] tempDestFiles;
        DeleteFolder tempDeleteFolder;
//        System.out.println("Checking for new or modified files in the Source folder");
        for(int i=0; i<FileCount;i++){
//            System.out.println("Source file name -- " + ListOfFiles[i].getAbsolutePath());
            strFileNameInBackup=strToFolder + File.separator +  ListOfFiles[i].getName();
//            System.out.println("Check if the Destination file name -- " + strFileNameInBackup + " Exists");
            tempDestFile=new File(strFileNameInBackup);
            if(tempDestFile.exists()){
//                System.out.println("\t The file exists, checking for the modification date");
                SimpleDateFormat formatter;
                formatter = new SimpleDateFormat("yyyyMMddHHmm");     
                long comp1=Long.parseLong(formatter.format(tempDestFile.lastModified()));
                long comp2=Long.parseLong(formatter.format(ListOfFiles[i].lastModified()));
                if(comp1>comp2+1 || comp1<comp2-1){
//                    System.out.println("Destination : "+tempDestFile.lastModified()+" "+formatter.format(tempDestFile.lastModified()));
//                    System.out.println("Source : "+ListOfFiles[i].lastModified()+" "+formatter.format(ListOfFiles[i].lastModified()));
//                    System.out.println("\t File has been modified since the last backup...");
//                    System.out.println("\t\t Updating backup file...");
                    srtFileNameToDatedBackup=strBackupFolder + File.separator + "M"+thisMgr.NumberOfModifiedFilesGlobal++;
                    tempBackupFile=new File(srtFileNameToDatedBackup);
                    CopyFile datedBackMod=new CopyFile(tempDestFile,tempBackupFile);
                    CopyFile backMod=new CopyFile(ListOfFiles[i],tempDestFile);
                    if(tempDestFile.setLastModified(ListOfFiles[i].lastModified())){
//                        System.out.println("Mod OK");
//                        System.out.println("Destination : "+tempDestFile.lastModified()+" "+formatter.format(tempDestFile.lastModified()));
//                        System.out.println("Source : "+ListOfFiles[i].lastModified()+" "+formatter.format(ListOfFiles[i].lastModified()));                        
                    }
                    String ModFileDetails=tempDestFile.getAbsolutePath()+":-:"+tempBackupFile.getAbsolutePath();
                    FileAppend ModLog=new FileAppend(strBackupFolder + File.separator + "ModLog.log",ModFileDetails);
                }else{
//                    System.out.println("\t Nothing changed in the file");
                }
            }else{
//                System.out.println("\t The file does not exists, this is a new file");
//                System.out.println("\t\t Updating backup file...");
                srtFileNameToDatedBackup=strBackupFolder + File.separator + "N"+thisMgr.NumberOfNewFilesGlobal++;
                tempBackupFile=new File(srtFileNameToDatedBackup);
                CopyFile datedBackNew=new CopyFile(ListOfFiles[i],tempBackupFile);
                CopyFile backNew=new CopyFile(ListOfFiles[i],tempDestFile);
                String NewFileDetails=tempDestFile.getAbsolutePath()+":-:"+tempBackupFile.getAbsolutePath();
                FileAppend NewLog=new FileAppend(strBackupFolder + File.separator + "NewLog.log",NewFileDetails);
            }
        }
        tempDestFile=new File(strToFolder);
        tempDestFiles=tempDestFile.listFiles();
        for(int i=0;i<tempDestFiles.length;i++){            
            tempSrcFile=new File(strFolderName + File.separator +  tempDestFiles[i].getName());
//            System.out.println("Checking for the source file -- " + tempSrcFile.getAbsolutePath());
            if(tempSrcFile.exists()){
//                System.out.println("\t File is not deleted");
            }else{
//                System.out.println("\t File is deleted...");                       
//                System.out.println("\t\t Making changes in the backup");
                if(tempDestFiles[i].isFile()){
                    srtFileNameToDatedBackup=strBackupFolder+File.separator + "D"+thisMgr.NumberOfDeletedFilesGlobal++;
                    tempBackupFile=new File(srtFileNameToDatedBackup);
                    CopyFile datedBackDel=new CopyFile(tempDestFiles[i],tempBackupFile);    
                    String FileToDelete=tempDestFiles[i].getAbsolutePath();
                    if(tempDestFiles[i].delete()){
//                        System.out.println("\t\t Backup file is sucessfully deleted...");             
                        String DeleteFileDetails=FileToDelete+":-:"+tempBackupFile.getAbsolutePath();
                        FileAppend deleteLog=new FileAppend(strBackupFolder+File.separator + "DeleteLog.log",DeleteFileDetails);                        
                    }else{
//                        System.out.println("\t\t Can not delete backup file...");
                    }
                }else if (tempDestFiles[i].isDirectory()){
                    tempDeleteFolder= new DeleteFolder(tempDestFiles[i],strBackupFolder,thisMgr);
                }
                
            }                
        }
    }
}
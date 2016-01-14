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
public class FileCount {
    public int getFileCount(String strFolderName){
        int tempFileCnt=0;
        File ThisFolder=new File(strFolderName);
        File[] ListInThisFolder=ThisFolder.listFiles();
        for (int i = 0; i < ListInThisFolder.length; i++) {
            if(ListInThisFolder[i].isDirectory()){
                FileCount filesInThisFolder=new FileCount();
                tempFileCnt+=filesInThisFolder.getFileCount(ListInThisFolder[i].getAbsolutePath());
            }else if(ListInThisFolder[i].isFile()){
                tempFileCnt++;
            }
        }
        return tempFileCnt;
    }
}

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
import java.io.*;
import java.io.File;
import java.nio.channels.*;

public class CopyFile {
    public CopyFile(File fromFile, File toFile) throws IOException 
    {
        FileChannel inChannel = new FileInputStream(fromFile).getChannel();
        FileChannel outChannel = new FileOutputStream(toFile).getChannel();
        try {           
           int maxCount = (64 * 1024 * 1024) - (32 * 1024); // magic number for Windows, 64Mb - 32Kb)
           long size = inChannel.size();
           long position = 0;
           while (position < size) {
              position += inChannel.transferTo(position, maxCount, outChannel);
            } 
        }
        catch (IOException e) {
            //throw e;
        }
        finally {
            if (inChannel != null) inChannel.close();
            if (outChannel != null) outChannel.close();
            toFile.setLastModified(fromFile.lastModified());
        }
    }
}

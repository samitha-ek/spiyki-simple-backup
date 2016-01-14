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
//import java.io.IOException;

public class FileAppend {
    public FileAppend(String FileName, String txtToAppend) throws IOException {
              BufferedWriter bw = null;
      try {
         bw = new BufferedWriter(new FileWriter(FileName, true));
	 bw.write(txtToAppend);
	 bw.newLine();
	 bw.flush();
      } 
      catch (IOException ioe) {
	 ioe.printStackTrace();
      } finally {
        if(bw!=null){
            bw.close();
        }
      }

    }

}

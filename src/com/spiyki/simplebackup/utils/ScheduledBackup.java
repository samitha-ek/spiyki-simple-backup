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
import java.util.TimerTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
/**
 *
 * @author samitha_2
 */
public class ScheduledBackup extends TimerTask {
  public int iBkRecord=0;
  public MainFunctions thisMain;
  int iCnt=0;
  public void run() {      
      Calendar date = Calendar.getInstance();    
      DateFormat df=new SimpleDateFormat("HH:mm:ss dd/MMM/yyyy");
      String thisTime= df.format(date.getTime());
      System.out.println("Backup process for shedule"+ Integer.toString(iBkRecord) +" started at: "+thisTime+". Process count = "+ Integer.toString(iCnt++));
      thisMain.BackupFileHandle.BackupNow(thisMain.BackupFiles, iBkRecord);
  }
}

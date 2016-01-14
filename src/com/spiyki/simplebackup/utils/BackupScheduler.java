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
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
//import java.io.*;
/**
 *
 * @author samitha_2
 */
public class BackupScheduler {
    Timer thisTimer;
    MainFunctions thisEnvMain;
    public BackupScheduler(int iBkRecord, Date dtLastBk, int iHr, Timer tmrSch, MainFunctions thisMain){        
        thisEnvMain=thisMain;
        thisTimer=tmrSch;
        initSchedulerDaily(iBkRecord,dtLastBk,iHr);
    }
    
    public BackupScheduler(int iBkRecord, Date dtLastBk, int iHr,int iDoW, Boolean bMonthly, Timer tmrSch, MainFunctions thisMain){  
        thisTimer=tmrSch;
        if(bMonthly){
            initSchedulerMonthly(iBkRecord,dtLastBk,iHr,iDoW);
        }else{
            initSchedulerWeekly(iBkRecord,dtLastBk,iHr,iDoW);
        }
    }
    
    private void initSchedulerDaily(int iBkRecord, Date dtLastBk, int iHr){     
        Calendar date = Calendar.getInstance();                   
        Date dtCurr=date.getTime();
        Date dtNxtBk= new Date(dtCurr.getTime()-1000*60*60*24);        //Check the last 24 HRS from now
        if(dtNxtBk.getTime()>dtLastBk.getTime()){         
            //If any backup has not been done in the last 24HRS
            Date dtFirstTask=new Date(dtCurr.getTime()+10*1000);   
            //Schedule a backup task in 10 seconds
            ScheduledBackup thisFirstSchedule=new ScheduledBackup();
            thisFirstSchedule.iBkRecord=iBkRecord;
            thisFirstSchedule.thisMain=thisEnvMain;
            thisTimer.schedule(
              thisFirstSchedule,
              dtFirstTask
            );            
        }else{ //If it is due future, SCHEDULE the event            
        }        
        ScheduledBackup thisSchedule=new ScheduledBackup();
        thisSchedule.iBkRecord=iBkRecord;
        thisSchedule.thisMain=thisEnvMain;
        date.set(Calendar.HOUR_OF_DAY, iHr);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        // Schedule to run every iHr hour each day
        thisTimer.schedule(
          thisSchedule,        //The task
          date.getTime(),      //First time
          1000 * 60 * 60 * 24  //Period = 24 HRS
        );
    }
    
    private void initSchedulerWeekly(int iBkRecord, Date dtLastBk, int iHr,int iDoW){
        Calendar date = Calendar.getInstance();                   
        Date dtCurr=date.getTime();
        Date dtNxtBk= new Date(dtCurr.getTime()-1000*60*60*24*7);        //Check the last 24 HRS from now
        if(dtNxtBk.getTime()>dtLastBk.getTime()){         
            //If any backup has not been done in the last week
            Date dtFirstTask=new Date(dtCurr.getTime()+10*1000);   
            //Schedule a backup task in 10 seconds
            ScheduledBackup thisFirstSchedule=new ScheduledBackup();
            thisFirstSchedule.iBkRecord=iBkRecord;
            thisFirstSchedule.thisMain=thisEnvMain;
            thisTimer.schedule(
              thisFirstSchedule,
              dtFirstTask
            );            
        }else{ //If it is due future, SCHEDULE the event            
        }        
        ScheduledBackup thisSchedule=new ScheduledBackup();
        thisSchedule.iBkRecord=iBkRecord;
        thisSchedule.thisMain=thisEnvMain;
        date.set(Calendar.DAY_OF_WEEK, iDoW);
        date.set(Calendar.HOUR_OF_DAY, iHr);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        // Schedule to run every iHr hour each day
        thisTimer.schedule(
          thisSchedule,        //The task
          date.getTime(),      //First time
          1000 * 60 * 60 * 24 * 7  //Period = 7 days
        );    
    }
    
    private void initSchedulerMonthly(int iBkRecord, Date dtLastBk, int iHr,int iDoW){
        Calendar date = Calendar.getInstance();       
        Calendar date1 = Calendar.getInstance();      
        Date dtCurr=date.getTime();        
        //date.get(Calendar.MONTH);
        if(date.get(Calendar.MONTH)==0){    //If January
            date1.set(Calendar.YEAR,date.get(Calendar.YEAR)-1);
            date1.set(Calendar.DAY_OF_MONTH,11);
        }else{
            date1.set(Calendar.DAY_OF_MONTH,date.get(Calendar.MONTH)-1);
        }
        Date dtNxtBk=date1.getTime();
        if(dtNxtBk.getTime()>dtLastBk.getTime()){         
            //If any backup has not been done in the last month
            Date dtFirstTask=new Date(dtCurr.getTime()+10*1000);   
            //Schedule a backup task in 10 seconds
            ScheduledBackup thisFirstSchedule=new ScheduledBackup();
            thisFirstSchedule.iBkRecord=iBkRecord;
            thisFirstSchedule.thisMain=thisEnvMain;
            thisTimer.schedule(
              thisFirstSchedule,
              dtFirstTask
            );           
        }else{ //If it is due future, SCHEDULE the event            
        }        
        ScheduledBackup thisSchedule=new ScheduledBackup();
        thisSchedule.iBkRecord=iBkRecord;
        thisSchedule.thisMain=thisEnvMain;
        date.set(Calendar.DAY_OF_MONTH, iDoW);
        date.set(Calendar.HOUR_OF_DAY, iHr);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        // Schedule to run every iHr hour each day
        thisTimer.schedule(
          thisSchedule,        //The task
          date.getTime(),      //First time
          1000 * 60 * 60 * 24 * 30  //Period = 24 HRS
        );         
    }
}

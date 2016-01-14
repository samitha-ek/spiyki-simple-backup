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
public class BackupInfo {
    String[] BackupData;                
    //This stores the backup information in the following order
    //BackupName,Source,Destination,ScheduleType,ScheduleInterval1,ScheduleInterval2,RestorePoints,Restore Folder
    
    //This is the constructor which takes the initial data string and stores in the BackupData String array
    public BackupInfo(String RawData){
        //Raw data is seperated by -- charactor pair
        BackupData=RawData.split("--");        
    }
    
    public String GetBackupString(){
        String strBackupMsg;
        strBackupMsg=BackupData[0]+"--"+BackupData[1]+"--"+BackupData[2]+"--"+BackupData[3]+"--"+BackupData[4]+"--"+BackupData[5]+"--"+BackupData[6]+"--"+BackupData[7];
        return strBackupMsg;
    }
    
    public String GetBackupName(){        
        if(!BackupData[0].contentEquals("")){
            return BackupData[0];
        }else{
             return null;
        }          
    }
    
   public String GetSourceFolder(){        
        if(!BackupData[1].contentEquals("")){
            return BackupData[1];
        }else{
             return null;
        }          
    }
   
    public String GetDestinationFolder(){        
        if(!BackupData[2].contentEquals("")){
            return BackupData[2];
        }else{
             return null;
        }          
    }   
    
    public String GetScheduleType(){        
        if(!BackupData[3].contentEquals("")){
            return BackupData[3];
        }else{
             return null;
        }          
    }     
    
    public String GetScheduleInt1(){        
        if(!BackupData[4].contentEquals("")){
            return BackupData[4];
        }else{
             return "0";
        }          
    }     

    public String GetScheduleInt2(){        
        if(!BackupData[5].contentEquals("")){
            return BackupData[5];
        }else{
             return "0";
        }          
    }  
    
    public String GetRestorePoints(){        
        if(!BackupData[6].contentEquals("")){
            return BackupData[6];
        }else{
             return "0";
        }          
    }    
    
    public String GetRestoreFolder(){        
        if(!BackupData[7].contentEquals("")){
            return BackupData[7];
        }else{
             return null;
        }          
    }       
    
    public void SetBackupName(String Value){
        BackupData[0]=Value;
    }
    
    public void SetSourceFolder(String Value){
        BackupData[1]=Value;
    }
    
    public void SetDestinationFolder(String Value){
        BackupData[2]=Value;
    }
    
    public void SetScheduleType(String Value){
        BackupData[3]=Value;
    }
    
    public void SetScheduleInt1(String Value){
        BackupData[4]=Value;
    }
    
    public void SetScheduleInt2(String Value){
        BackupData[5]=Value;
    }
    
    public void SetRestorePoints(String Value){
        BackupData[6]=Value;
    }    
    
    public void SetRestoreFolder(String Value){
        BackupData[7]=Value;
    }     
}

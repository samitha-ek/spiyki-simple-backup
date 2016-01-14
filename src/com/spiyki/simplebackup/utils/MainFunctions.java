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
import java.util.Timer;
import java.io.IOException;
//import javax.swing.JFileChooser;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.text.DateFormat;
import com.spiyki.simplebackup.gui.*;

/**
 *
 * @author samitha_2
 */
public class MainFunctions {
    public static final String IMAGE_PATH = "/res/images/";
    Timer thisSchTimer=null;
    BackupScheduler theseScheduledBakcups=null;
    guiSimpleBackupMain ThisGUI;
    public BackupFileHandle BackupFiles;
    BackupManager BackupFileHandle;//=new BackupManager(ThisGUI);
    
    int RestoreRecordNumber;
    
    public MainFunctions(guiSimpleBackupMain Env){
       ThisGUI=Env;
       BackupFileHandle=new BackupManager(ThisGUI);
       InitGUI();       
    }
    
    private void InitGUI(){
        BackupFiles=new BackupFileHandle();
        ThisGUI.cmdEditPath.setEnabled(false);                
        ThisGUI.cmdDeletePath.setEnabled(false);
        ThisGUI.cmdViewRestore.setEnabled(false);
        ThisGUI.cmdBackupNow.setEnabled(false);
        DisplayMainWindow();
        
        final String[] strBackupListItems=new String[BackupFiles.Backups.size()];
        for(int i=0;i<BackupFiles.Backups.size();i++){
            strBackupListItems[i]=((BackupInfo)BackupFiles.Backups.get(i)).GetBackupName();
        }
        ThisGUI.lstBackupPaths.setModel(new javax.swing.AbstractListModel() {
            String[] strings = strBackupListItems;
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        
        String[] strScheduleItems={"Manual","Daily","Weekly","Monthly"};
        ThisGUI.cmbScheduleItems.setModel(new javax.swing.DefaultComboBoxModel(strScheduleItems));        
        PerformBackupScheduling();
    }
    
    private void PerformBackupScheduling(){
//        SimpleDateFormat dtFormat =new SimpleDateFormat("yyyyMMddhhmmss");
        SimpleDateFormat dtFormat =new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        DateFormat df = DateFormat.getDateInstance();

        //Cancel the existing backup tasks, if the timer is already initialized
        if(thisSchTimer!=null){
            thisSchTimer.cancel();
        }
        thisSchTimer=new Timer();
        //Look for any backup schedules defined
        for(int i=0;i<BackupFiles.Backups.size();i++){
            int iRec=i;
            //If the backup schedule is not a manual backup (automatic backup)
            int iBkType=Integer.parseInt(((BackupInfo)BackupFiles.Backups.get(i)).GetScheduleType());
            if(iBkType!=0){
                //Now get the last backup date
                RestoreBackup thisRestore= new RestoreBackup(BackupFiles, iRec);    //Create a new restore information variable
                String strRestorePoints=thisRestore.BackupDateTimes[0];             //Get the last restore date in String format
                int iBkInt1=Integer.parseInt(((BackupInfo)BackupFiles.Backups.get(i)).GetScheduleInt1());
                int iBkInt2=Integer.parseInt(((BackupInfo)BackupFiles.Backups.get(i)).GetScheduleInt2());
                try {                    
                    Date dtLastBk = dtFormat.parse(strRestorePoints);               //Convert it to Date format                    
//                    Date dtLastBk = df.parse(strRestorePoints);               //Convert it to Date format                    
                    //Now schedule the backup with backupschedular class
                    switch(iBkType){
                        case 1:         //Daily
                            theseScheduledBakcups=new BackupScheduler(iRec,dtLastBk,iBkInt1,thisSchTimer,this);
                            break;
                        case 2:         //Weekely
                            theseScheduledBakcups=new BackupScheduler(iRec,dtLastBk,iBkInt1,iBkInt2,false,thisSchTimer,this);
                            break;
                        case 3:         //Monthly
                            theseScheduledBakcups=new BackupScheduler(iRec,dtLastBk,iBkInt1,iBkInt2,true,thisSchTimer,this);
                            break;
                        default:
                            break;
                    }                    
                }
                catch(ParseException pe) {               
                    pe.printStackTrace();
                }                
            }
        }
    }

    public void ExecuteBackup(int RecordNumber){
        ThisGUI.lblMainStat.setText("Backup process started ...");
        if(BackupFileHandle.BackupNow(BackupFiles, RecordNumber)){
            ThisGUI.lblMainStat.setText("Backup process successful");
        }else{
            ThisGUI.lblMainStat.setText("Backup process failed");
        }        
    }
    
    public void DisplayMainWindow(){
        ThisGUI.MainPanel.setVisible(true);
        ThisGUI.AddPathPanel.setVisible(false);
        ThisGUI.RestorePanel.setVisible(false);        
    }
    
    public void DisplayRestoreWindow(int RecordNumber){
        ThisGUI.MainPanel.setVisible(false);
        ThisGUI.AddPathPanel.setVisible(false);
        ThisGUI.RestorePanel.setVisible(true);              
        RestoreRecordNumber=RecordNumber;
        RestoreBackup thisRestore= new RestoreBackup(BackupFiles, RecordNumber);
        final String[] strRestorePoints=new String[thisRestore.CurrentRestorePoints];
        for(int i=0;i<thisRestore.CurrentRestorePoints;i++){
            strRestorePoints[i]=thisRestore.BackupDateTimes[i];
        }
        ThisGUI.lstRestorePoints.setModel(new javax.swing.AbstractListModel() {
            String[] strings = strRestorePoints;
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });        
        ThisGUI.lblRestoreName.setText(((BackupInfo)BackupFiles.Backups.get(RecordNumber)).GetBackupName());
        ThisGUI.txtRestoreFolderView.setText(((BackupInfo)BackupFiles.Backups.get(RecordNumber)).GetRestoreFolder());
    }
    
    public void ExecuteRestore(int RestorePoint){
        RestoreBackup thisRestore=new RestoreBackup(BackupFiles, RestoreRecordNumber);     
        thisRestore.RestoreBackupFiles(BackupFiles, RestoreRecordNumber, RestorePoint);

    }
    
    public void DisplayFolderSelector(){
        ThisGUI.MainPanel.setVisible(false);
        ThisGUI.AddPathPanel.setVisible(false);
        ThisGUI.RestorePanel.setVisible(false);
    }
    
    public void DisplayAddNewWindow(){
        ThisGUI.MainPanel.setVisible(false);
        ThisGUI.AddPathPanel.setVisible(true);
        ThisGUI.RestorePanel.setVisible(false);
        ThisGUI.cmbScheduleItems.setSelectedIndex(0);
        ThisGUI.lblSchDesc1.setEnabled(false);
        ThisGUI.lblSchDesc2.setEnabled(false);
        ThisGUI.txtScheduleInterval1.setEnabled(false);
        ThisGUI.txtScheduleInterval2.setEnabled(false);
    }
    
    public void EditExistingBackupPath(int RecordNumber){
        DisplayAddNewWindow();
        BackupInfo tempInfo;
        BackupFiles.CurrentRecordSet=RecordNumber;
        tempInfo=(BackupInfo)BackupFiles.Backups.get(RecordNumber);
        ThisGUI.txtBackupName.setText(tempInfo.GetBackupName());
        ThisGUI.txtSrc.setText(tempInfo.GetSourceFolder());
        ThisGUI.txtDst.setText(tempInfo.GetDestinationFolder());
        SettingsForSelectedSchedule(Integer.parseInt(tempInfo.GetScheduleType()), Integer.parseInt(tempInfo.GetScheduleInt1()), Integer.parseInt(tempInfo.GetScheduleInt2()));
        ThisGUI.txtRestore.setText(tempInfo.GetRestorePoints());
        ThisGUI.txtRestoreFolder.setText(tempInfo.GetRestoreFolder());
        BackupFiles.NewRecord=false;
    }
    
    public void AddNewBackupPath(){
        DisplayAddNewWindow();
        ThisGUI.txtBackupName.setText("");
        ThisGUI.txtSrc.setText("");
        ThisGUI.txtDst.setText("");
        ThisGUI.txtRestore.setText("");
        ThisGUI.txtRestoreFolder.setText(""); 
        ThisGUI.txtScheduleInterval1.setText("");
        ThisGUI.txtScheduleInterval2.setText("");                
        BackupFiles.NewRecord=true;
    }
    
    public void DeleteSelectedPath(int RecordNumber){
        try{
            BackupFiles.Backups.remove(RecordNumber);
            BackupFiles.SaveTheBackupConfiguration();
            InitGUI();
        }catch(IOException e){
            
        }      
    }
    
    public void SaveBackupFileSettings(){
        BackupInfo tempInfo;
        try{
            if(ValidateBackupSave()==true){
                if(BackupFiles.NewRecord){
                    //If this is a new record create new array item
                    String tempBackupString="Backup Name--Source Folder--Destination Folder--0--0--0--5--Restore Folder";
                    BackupInfo tempBackup=new BackupInfo(tempBackupString);
                    BackupFiles.Backups.add(tempBackup);  
                    BackupFiles.CurrentRecordSet=BackupFiles.Backups.size()-1;
                }
                tempInfo=(BackupInfo)BackupFiles.Backups.get(BackupFiles.CurrentRecordSet);
                tempInfo.SetBackupName(ThisGUI.txtBackupName.getText());
                tempInfo.SetDestinationFolder(ThisGUI.txtDst.getText());
                tempInfo.SetSourceFolder(ThisGUI.txtSrc.getText());
                tempInfo.SetRestoreFolder(ThisGUI.txtRestoreFolder.getText());
                tempInfo.SetRestorePoints(ThisGUI.txtRestore.getText());
                tempInfo.SetScheduleType(Integer.toString(ThisGUI.cmbScheduleItems.getSelectedIndex()));
                tempInfo.SetScheduleInt1(ThisGUI.txtScheduleInterval1.getText());
                tempInfo.SetScheduleInt2(ThisGUI.txtScheduleInterval2.getText());
                BackupFiles.SaveTheBackupConfiguration();
                InitGUI();
            }
        }catch(IOException e){

        }
    }
    
    private boolean ValidateBackupSave(){
        if(ThisGUI.txtBackupName.getText().contentEquals("")){
            return false;
        }
        if(ThisGUI.txtDst.getText().contentEquals("")){
            return false;
        }
        if(ThisGUI.txtSrc.getText().contentEquals("")){
            return false;
        }
        if(ThisGUI.txtRestoreFolder.getText().contentEquals("")){
            return false;
        }
        if(ThisGUI.txtRestore.getText().contentEquals("")){
            return false;
        }
        if(ThisGUI.cmbScheduleItems.getSelectedIndex()==1){
            if(ThisGUI.txtScheduleInterval1.getText().contentEquals("")){
                return false;
            }
        }     
        if(ThisGUI.cmbScheduleItems.getSelectedIndex()==2 || ThisGUI.cmbScheduleItems.getSelectedIndex()==3){
            if(ThisGUI.txtScheduleInterval1.getText().contentEquals("")){
                return false;
            }
            if(ThisGUI.txtScheduleInterval2.getText().contentEquals("")){
                return false;
            }            
        }        
        return true;
    }
    
    public void SettingsForSelectedSchedule(int ScheduleType,int SchInt1,int SchInt2){
        switch(ScheduleType){
            case 0: //Manual
                ThisGUI.lblSchDesc1.setEnabled(false);
                ThisGUI.lblSchDesc2.setEnabled(false);
                ThisGUI.txtScheduleInterval1.setEnabled(false);
                ThisGUI.txtScheduleInterval2.setEnabled(false);      
                ThisGUI.cmbScheduleItems.setSelectedIndex(ScheduleType);
                break;
            case 1: //Daily
                ThisGUI.lblSchDesc1.setEnabled(true);
                ThisGUI.lblSchDesc2.setEnabled(false);
                ThisGUI.txtScheduleInterval1.setEnabled(true);
                ThisGUI.txtScheduleInterval2.setEnabled(false);  
                ThisGUI.cmbScheduleItems.setSelectedIndex(ScheduleType);
                if(ThisGUI.txtScheduleInterval1.getText().contentEquals("0") || ThisGUI.txtScheduleInterval1.getText().contentEquals("")){
                    ThisGUI.txtScheduleInterval1.setText(Integer.toString(SchInt1));
                }                                
                break;
            case 2: //Weekly
                ThisGUI.lblSchDesc1.setEnabled(true);
                ThisGUI.lblSchDesc2.setEnabled(true);
                ThisGUI.txtScheduleInterval1.setEnabled(true);
                ThisGUI.txtScheduleInterval2.setEnabled(true);    
                ThisGUI.cmbScheduleItems.setSelectedIndex(ScheduleType);
                if(ThisGUI.txtScheduleInterval1.getText().contentEquals("0") || ThisGUI.txtScheduleInterval1.getText().contentEquals("")){
                    ThisGUI.txtScheduleInterval1.setText(Integer.toString(SchInt1));
                } 
                if(ThisGUI.txtScheduleInterval2.getText().contentEquals("0") || ThisGUI.txtScheduleInterval2.getText().contentEquals("")){
                    ThisGUI.txtScheduleInterval2.setText(Integer.toString(SchInt2));
                }                                
                break;
            case 3: //Monthly
                ThisGUI.lblSchDesc1.setEnabled(true);
                ThisGUI.lblSchDesc2.setEnabled(true);
                ThisGUI.txtScheduleInterval1.setEnabled(true);
                ThisGUI.txtScheduleInterval2.setEnabled(true);        
                ThisGUI.cmbScheduleItems.setSelectedIndex(ScheduleType);
                if(ThisGUI.txtScheduleInterval1.getText().contentEquals("0") || ThisGUI.txtScheduleInterval1.getText().contentEquals("")){
                    ThisGUI.txtScheduleInterval1.setText(Integer.toString(SchInt1));
                } 
                if(ThisGUI.txtScheduleInterval2.getText().contentEquals("0") || ThisGUI.txtScheduleInterval2.getText().contentEquals("")){
                    ThisGUI.txtScheduleInterval2.setText(Integer.toString(SchInt2));
                }                     
                break;
            default:
                break;
        }                   
    }
}

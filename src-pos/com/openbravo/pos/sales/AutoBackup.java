/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.sales;

/**
 *
 * @author shilpa
 */
public class AutoBackup {
    
    public static void main (String[] args){
       String  dbname="toitplive";
       String user="root";
       String password="root";
       String path="/home/shilpa/toitbackupfromsystem.sql;";
       boolean val=backupDB(dbname,user, password, path) ; 
       
       
    } 
    
   public static boolean backupDB(String dbName, String dbUserName, String dbPassword, String path) {
	 
    String executeCmd = "mysqldump -u " + dbUserName + " -p" + dbPassword + " --add-drop-database -B " + dbName + " -r " + path;
    System.out.println("executeCmd:"+executeCmd);
    Process runtimeProcess;
    boolean dbstatus=false;
    try {

        runtimeProcess = Runtime.getRuntime().exec(executeCmd);
        int processComplete = runtimeProcess.waitFor();

        if (processComplete == 0) {
            System.out.println("Backup created successfully");
            dbstatus=true;
        } else {
            System.out.println("Could not create the backup");
            dbstatus=false;
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
//    if(dbstatus==false){
//      backupDB(dbName, dbUserName, dbPassword, path);  
//    }
    return dbstatus;

    }
}

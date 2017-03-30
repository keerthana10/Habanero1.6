/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.sales;

import com.openbravo.basic.BasicException;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author preethi
 */
public class ProcessInfo {
     private AppView m_App;
      private static  DataLogicSales m_dlSales;
public ProcessInfo(AppView app) {
        m_App = app;
      }
public static void setProcessInfo(String processName, DataLogicSales dlsales){
    m_dlSales= dlsales;
   // String processName = "Purchase Order";
       int purchaseCount = 0;
        try {
            purchaseCount = m_dlSales.getPurchaseCount(processName);
        } catch (BasicException ex) {
            Logger.getLogger(ProcessInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        String id= UUID.randomUUID().toString();

        if(purchaseCount==0){
            m_dlSales.insertProcessInfo(id, processName, 1);
        }else{
             try {
                purchaseCount = m_dlSales.getProcessCount(processName);
            } catch (BasicException ex) {
                Logger.getLogger(ProcessInfo.class.getName()).log(Level.SEVERE, null, ex);
            }
             int count = purchaseCount+1;

            try {
                m_dlSales.updateProcessInfo(processName, count);
            } catch (BasicException ex) {
                Logger.getLogger(ProcessInfo.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
}
public static int setProcessCount(String processName, DataLogicSales dlsales){
     m_dlSales = dlsales;
    int processCount = 0;
     int purchaseCount = 0;
        try {
            purchaseCount = m_dlSales.getPurchaseCount(processName);
        } catch (BasicException ex) {
            Logger.getLogger(ProcessInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
     if(purchaseCount!=0){
        try {
           processCount = m_dlSales.getProcessCount(processName);
        } catch (BasicException ex) {
            Logger.getLogger(ProcessInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
     }
     return processCount;
}
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sysfore.pos.employeemanagement;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.data.loader.SerializerWriteBasicExt;
import com.openbravo.data.loader.Session;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.pos.forms.BeanFactoryDataSingle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mateen
 */
public class CheckInOutReceipts extends BeanFactoryDataSingle {

    private Session s;

    /**
     * Creates a new instance of PurchaseOrderReceipts
     */
    public CheckInOutReceipts() {
    }

    public void init(Session s) {
        this.s = s;
    }

    public int insertCheckInOut(String empId, String isCheckIn, String isCheckOut, String empcheckintime, String empcheckedittime, Date currentdate, String reasonId, String otherReasonDropDown, String otherReasonTxt) {

        int success = 0;
        try {
            Object[] values = new Object[]{UUID.randomUUID().toString(), empId, isCheckIn, isCheckOut, empcheckintime, empcheckedittime, currentdate, reasonId, otherReasonDropDown, otherReasonTxt};
            Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.TIMESTAMP, Datas.STRING, Datas.STRING, Datas.STRING};
            success = new PreparedSentence(s, "INSERT INTO CHECKINOUT(ID, EMPLOYEEID, CHECKIN, CHECKOUT, SYSTEMCHECKINTIME, EMPCHECKINTIME, CURRENTDATE, CHECKINDESCID, CHECKINDESC, OTHERCHECKINDESC) "
                    + "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9})).exec(values);
        } catch (BasicException ex) {
            Logger.getLogger(CheckInOutReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return success;
    }

    public java.util.List<CheckInOutInfo> getCheckInOut(String id) {
        List<CheckInOutInfo> lines = new ArrayList<CheckInOutInfo>();
        String query ="SELECT ID, EMPLOYEEID, CHECKIN, CHECKOUT, SYSTEMCHECKINTIME, SYSTEMCHECKOUTTIME, EMPCHECKINTIME, EMPCHECKOUTTIME, CURRENTDATE, CHECKINDESCID, CHECKINDESC, CHECKOUTDESCID, CHECKOUTDESC, OTHERCHECKINDESC, OTHERCHECKOUTDESC, ID, ID, CHECKINATTENDANCEAPPROVAL, CHECKOUTATTENDANCEAPPROVAL "
                    + "FROM CHECKINOUT WHERE EMPLOYEEID='" + id + "' AND CHECKOUT='N' ";
        try {
            lines = new StaticSentence(s, query, null, new SerializerReadClass(CheckInOutInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(CheckInOutReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }

    public int updateCheckInOut(String id, String empId, String systemCheckOutTime, String empCheckOutTime, String checkOutDescId, String checkOutDesc, String txtCheckOutDesc) {

        int success = 0;
        String query1 = "UPDATE CHECKINOUT SET CHECKOUT='Y', SYSTEMCHECKOUTTIME='" + systemCheckOutTime + "', EMPCHECKOUTTIME = '" + empCheckOutTime + "',"
                + " CHECKOUTDESCID = '" + checkOutDescId + "', CHECKOUTDESC = '" + checkOutDesc + "', OTHERCHECKOUTDESC = '" + txtCheckOutDesc + "'"
                + " WHERE EMPLOYEEID= '" + empId + "' AND ID= '" + id + "'";
        String query2 = "UPDATE CHECKINOUT SET LASTCHECKOUTSTATUS = 'Y' WHERE ID='" + id + "' ";
        try {
            success = new PreparedSentence(s, query1, null, null).exec();
            new StaticSentence(s, query2).exec();
        } catch (BasicException ex) {
            Logger.getLogger(CheckInOutReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return success;
    }

    public java.util.List<CheckInOutInfo> getPreviousDateCheckInOut(String id, String pDate) {
        List<CheckInOutInfo> lines = new ArrayList<CheckInOutInfo>();
        String query = "SELECT ID, EMPLOYEEID, CHECKIN, CHECKOUT, SYSTEMCHECKINTIME, SYSTEMCHECKOUTTIME, EMPCHECKINTIME, EMPCHECKOUTTIME, CURRENTDATE, CHECKINDESCID, CHECKINDESC, CHECKOUTDESCID, CHECKOUTDESC, OTHERCHECKINDESC, OTHERCHECKOUTDESC ,'', '', CHECKINATTENDANCEAPPROVAL, CHECKOUTATTENDANCEAPPROVAL "
                    + "FROM CHECKINOUT WHERE EMPLOYEEID='" + id + "' AND LASTCHECKOUTSTATUS='N' ";
        try {
            lines = new StaticSentence(s, query , null, new SerializerReadClass(CheckInOutInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(CheckInOutReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }

    public java.util.List<CheckInOutInfo> getCheckInOutToday(String id, String date) {
        List<CheckInOutInfo> lines = new ArrayList<CheckInOutInfo>();
        String query = "SELECT ID, EMPLOYEEID, CHECKIN, CHECKOUT, SYSTEMCHECKINTIME, SYSTEMCHECKOUTTIME, EMPCHECKINTIME, EMPCHECKOUTTIME, CURRENTDATE, CHECKINDESCID, CHECKINDESC, CHECKOUTDESCID, CHECKOUTDESC, OTHERCHECKINDESC, OTHERCHECKOUTDESC, ID, ID, CHECKINATTENDANCEAPPROVAL, CHECKOUTATTENDANCEAPPROVAL "
                + "FROM CHECKINOUT WHERE EMPLOYEEID='" + id + "' AND CURRENTDATE= '" + date + "'";
        try {
            lines = new StaticSentence(s, query, null, new SerializerReadClass(CheckInOutInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(CheckInOutReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }

    public java.util.List<AttendanceReasonsInfo> getAttendanceReasons() {
        List<AttendanceReasonsInfo> lines = new ArrayList<AttendanceReasonsInfo>();
        try {
            lines = new StaticSentence(s, "SELECT ID, DESCRIPTION FROM ATTENDANCEREASON", null, new SerializerReadClass(AttendanceReasonsInfo.class)).list();

        } catch (BasicException ex) {
            Logger.getLogger(CheckInOutReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }

    public java.util.List<CheckInOutInfo> getAllCheckInOut(String field) {
        List<CheckInOutInfo> lines = new ArrayList<CheckInOutInfo>();
        String query = "SELECT C.ID, C.EMPLOYEEID, C.CHECKIN, C.CHECKOUT, C.SYSTEMCHECKINTIME, C.SYSTEMCHECKOUTTIME, C.EMPCHECKINTIME, C.EMPCHECKOUTTIME, "
                + "C.CURRENTDATE, C.CHECKINDESCID, C.CHECKINDESC, C.CHECKOUTDESCID, C.CHECKOUTDESC, C.OTHERCHECKINDESC, C.OTHERCHECKOUTDESC "
                + ",  P.ID AS PID, P.NAME AS EMPNAME, C.CHECKINATTENDANCEAPPROVAL, C.CHECKOUTATTENDANCEAPPROVAL "
                + "FROM CHECKINOUT AS C, PEOPLE AS P WHERE P.ID=C.EMPLOYEEID " + field;
        try {
            lines = new StaticSentence(s, query, null, new SerializerReadClass(CheckInOutInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(CheckInOutReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }

    public int approveCheckInOut(String id, String field) {
        int retVal = 0;
        String query = "UPDATE CHECKINOUT SET " + field + " WHERE ID='" + id + "'";
        try {
            retVal = new StaticSentence(s, query).exec();
        } catch (BasicException ex) {
            Logger.getLogger(CheckInOutReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retVal;
    }
}

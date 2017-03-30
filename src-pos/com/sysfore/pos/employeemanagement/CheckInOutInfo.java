/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sysfore.pos.employeemanagement;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;
import java.util.Date;

/**
 *
 * @author mateen
 */
public class CheckInOutInfo implements SerializableRead, SerializableWrite {

    private long serialVersionUID = 123456789L;
    
    private String empid;
    private String empname;
    private String id;
    private String employeeid;
    private String checkin;
    private String checkout;
    private String systemcheckintime;
    private String systemcheckouttime;
    private String empcheckintime;
    private String empchekcouttime;
    private Date currentdate;
    private String checkindescid;
    private String checkindesc;
    private String checkoutdescid;
    private String checkoutdesc;
    private String othercheckindesc;
    private String othercheckoutdesc;
    private String checkinattendanceapproval;
    private String checkoutattendanceapproval;
     
    public CheckInOutInfo() {
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the employeeid
     */
    public String getEmployeeid() {
        return employeeid;
    }

    /**
     * @param employeeid the employeeid to set
     */
    public void setEmployeeid(String employeeid) {
        this.employeeid = employeeid;
    }

    /**
     * @return the checkin
     */
    public String getCheckin() {
        return checkin;
    }

    /**
     * @param checkin the checkin to set
     */
    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }

    /**
     * @return the checkout
     */
    public String getCheckout() {
        return checkout;
    }

    /**
     * @param checkout the checkout to set
     */
    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

    /**
     * @return the systemcheckintime
     */
    public String getSystemcheckintime() {
        return systemcheckintime;
    }

    /**
     * @param systemcheckintime the systemcheckintime to set
     */
    public void setSystemcheckintime(String systemcheckintime) {
        this.systemcheckintime = systemcheckintime;
    }

    /**
     * @return the systemcheckouttime
     */
    public String getSystemcheckouttime() {
        return systemcheckouttime;
    }

    /**
     * @param systemcheckouttime the systemcheckouttime to set
     */
    public void setSystemcheckouttime(String systemcheckouttime) {
        this.systemcheckouttime = systemcheckouttime;
    }

    /**
     * @return the empcheckintime
     */
    public String getEmpcheckintime() {
        return empcheckintime;
    }

    /**
     * @param empcheckintime the empcheckintime to set
     */
    public void setEmpcheckintime(String empcheckintime) {
        this.empcheckintime = empcheckintime;
    }

    /**
     * @return the empchekcouttime
     */
    public String getEmpchekcouttime() {
        return empchekcouttime;
    }

    /**
     * @param empchekcouttime the empchekcouttime to set
     */
    public void setEmpchekcouttime(String empchekcouttime) {
        this.empchekcouttime = empchekcouttime;
    }

    /**
     * @return the currentdate
     */
    public Date getCurrentdate() {
        return currentdate;
    }

    /**
     * @param currentdate the currentdate to set
     */
    public void setCurrentdate(Date currentdate) {
        this.currentdate = currentdate;
    }

    /**
     * @param checkindescid the checkindescid to set
     */
    public void setCheckindescid(String checkindescid) {
        this.checkindescid = checkindescid;
    }

    /**
     * @return the checkindescid
     */
    public String getCheckindescid() {
        return checkindescid;
    }

    /**
     * @return the checkindesc
     */
    public String getCheckindesc() {
        return checkindesc;
    }

    /**
     * @param checkindesc the checkindesc to set
     */
    public void setCheckindesc(String checkindesc) {
        this.checkindesc = checkindesc;
    }

    /**
     * @return the checkoutdescid
     */
    public String getCheckoutdescid() {
        return checkoutdescid;
    }

    /**
     * @param checkoutdescid the checkoutdescid to set
     */
    public void setCheckoutdescid(String checkoutdescid) {
        this.checkoutdescid = checkoutdescid;
    }

    /**
     * @return the checkoutdesc
     */
    public String getCheckoutdesc() {
        return checkoutdesc;
    }

    /**
     * @param checkoutdesc the checkoutdesc to set
     */
    public void setCheckoutdesc(String checkoutdesc) {
        this.checkoutdesc = checkoutdesc;
    }

    /**
     * @return the othercheckindesc
     */
    public String getOthercheckindesc() {
        return othercheckindesc;
    }

    /**
     * @param othercheckindesc the othercheckindesc to set
     */
    public void setOthercheckindesc(String othercheckindesc) {
        this.othercheckindesc = othercheckindesc;
    }

    /**
     * @return the othercheckoutdesc
     */
    public String getOthercheckoutdesc() {
        return othercheckoutdesc;
    }

    /**
     * @param othercheckoutdesc the othercheckoutdesc to set
     */
    public void setOthercheckoutdesc(String othercheckoutdesc) {
        this.othercheckoutdesc = othercheckoutdesc;
    }

    /**
     * @return the empid
     */
    public String getEmpid() {
        return empid;
    }

    /**
     * @param empid the empid to set
     */
    public void setEmpid(String empid) {
        this.empid = empid;
    }

    /**
     * @return the empname
     */
    public String getEmpname() {
        return empname;
    }

    /**
     * @param empname the empname to set
     */
    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public void readValues(DataRead dr) throws BasicException {

        id = dr.getString(1);
        employeeid = dr.getString(2);
        checkin = dr.getString(3);
        checkout = dr.getString(4);
        systemcheckintime = dr.getString(5);
        systemcheckouttime = dr.getString(6);
        empcheckintime = dr.getString(7);
        empchekcouttime = dr.getString(8);
        currentdate = dr.getTimestamp(9);
        checkindescid = dr.getString(10);
        checkindesc = dr.getString(11);
        checkoutdescid = dr.getString(12);
        checkoutdesc = dr.getString(13);
        othercheckindesc = dr.getString(14);
        othercheckoutdesc = dr.getString(15);
        empid = dr.getString(16);
        empname = dr.getString(17);
        checkinattendanceapproval = dr.getString(18);
        checkoutattendanceapproval = dr.getString(19);
    }

    public void writeValues(DataWrite dp) throws BasicException {

        dp.setString(1, id);
        dp.setString(2, employeeid);
        dp.setString(3, checkin);
        dp.setString(4, checkout);
        dp.setString(5, systemcheckintime);
        dp.setString(6, systemcheckouttime);
        dp.setString(7, empcheckintime);
        dp.setString(8, empchekcouttime);
        dp.setTimestamp(9, currentdate);
        dp.setString(10, checkindescid);
        dp.setString(11, checkindesc);
        dp.setString(12, checkoutdescid);
        dp.setString(13, checkoutdesc);
        dp.setString(14, othercheckindesc);
        dp.setString(15, othercheckoutdesc);
        dp.setString(16, empid);
        dp.setString(17, empname);
        dp.setString(18, checkinattendanceapproval);
        dp.setString(19, checkoutattendanceapproval);

    }

    /**
     * @return the checkinattendanceapproval
     */
    public String getCheckinattendanceapproval() {
        return checkinattendanceapproval;
    }

    /**
     * @param checkinattendanceapproval the checkinattendanceapproval to set
     */
    public void setCheckinattendanceapproval(String checkinattendanceapproval) {
        this.checkinattendanceapproval = checkinattendanceapproval;
    }

    /**
     * @return the checkoutattendanceapproval
     */
    public String getCheckoutattendanceapproval() {
        return checkoutattendanceapproval;
    }

    /**
     * @param checkoutattendanceapproval the checkoutattendanceapproval to set
     */
    public void setCheckoutattendanceapproval(String checkoutattendanceapproval) {
        this.checkoutattendanceapproval = checkoutattendanceapproval;
    }
}

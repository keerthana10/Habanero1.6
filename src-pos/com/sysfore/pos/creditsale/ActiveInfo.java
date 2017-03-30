/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sysfore.pos.creditsale;

import java.util.Date;
import javax.sound.midi.SysexMessage;

/**
 *
 * @author shilpa
 */
public class ActiveInfo {
    private String status;
    private double credit;
    private String tendorType;
    private String chequeno;
    private Date chequedate;
    private String bankname;
    private String Remarks;
    private String id;
    
    
    /**
     * @return the status
     */
    public String getStatus() {
        System.out.println("inside getter");
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        System.out.println("inside setter");
        this.status = status;
    }

    /**
     * @return the credit
     */
    public double getCredit() {
        return credit;
    }

    /**
     * @param credit the credit to set
     */
    public void setCredit(double credit) {
         System.out.println("setcredit");
        System.out.println(credit);
        this.credit = credit;
    }

    /**
     * @return the tendorType
     */
    public String getTendorType() {
        return tendorType;
    }

    /**
     * @param tendorType the tendorType to set
     */
    public void setTendorType(String tendorType) {
        this.tendorType = tendorType;
    }

    /**
     * @return the chequeno
     */
    public String getChequeno() {
        return chequeno;
    }

    /**
     * @param chequeno the chequeno to set
     */
    public void setChequeno(String chequeno) {
        if(chequeno.isEmpty())
        {
         chequeno=null;   
        }
        this.chequeno = chequeno;
    }

    /**
     * @return the chequedate
     */
    public Date getChequedate() {
        return chequedate;
    }

    /**
     * @param chequedate the chequedate to set
     */
    public void setChequedate(Date chequedate) {
        this.chequedate = chequedate;
    }

    /**
     * @return the bankname
     */
    public String getBankname() {
        return bankname;
    }

    /**
     * @param bankname the bankname to set
     */
    public void setBankname(String bankname) {
        if(bankname.isEmpty())
        {
            bankname=null;
        }
        this.bankname = bankname;
    }

    /**
     * @return the Remarks
     */
    public String getRemarks() {
        return Remarks;
    }

    /**
     * @param Remarks the Remarks to set
     */
    public void setRemarks(String Remarks) {
        if(Remarks.isEmpty())
        {
            Remarks=null;
        }
        this.Remarks = Remarks;
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
     * @return the billaddress
     */
   
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sysfore.pos.accountingconfig;

import com.openbravo.basic.BasicException;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.BeanFactoryException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author raghevendra
 */
public class Voucher {
  private String voucherType;
  private String path;  
  private String voucherNo;
  private String voucherRemoteID;
  private String remoteIDwithoutExtention;
  private String vchKey;
  private String guid;
  private String journalKey;
  private String postedDate;
  private String effectiveDate;
  private Double Amt;
  private String accountDate;
  private String Narration;

  public Voucher() {
     String args[]=null;
   //  AppConfig config = new AppConfig(args);
    // config.load();
   // journalKey =config.getProperty("tally.voucherkey");        
    UUID voucher_id = UUID.randomUUID();
  //  voucherRemoteID = config.getProperty("tally.remoteid");
    
  //  vchKey = config.getProperty("tally.remoteid");
   guid = voucher_id.toString();
       
    System.out.println("Journal Key " + journalKey);

  }
   public String getVoucherNo() {
    return voucherNo;
  }

  public void setVoucherNo(String voucherNo) {
    this.voucherNo = voucherNo;
  }

  public String getVoucherRemoteID() {
    return voucherRemoteID;
  }

  public void setVoucherRemoteID(String voucherRemoteID) {
      //System.out.println();
    this.voucherRemoteID = voucherRemoteID;
  }

  public String getVchKey() {
    return vchKey;
  }

  public void setVchKey(String vchKey) {
    this.vchKey = vchKey;
  }

  public String getGuid() {
    return guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

  public String getJournalKey() {
    return journalKey;
  }

  public void setJournalKey(String journalKey) {
    this.journalKey = journalKey;
  }

  public String getPostedDate() {
    return postedDate;
  }

  public void setPostedDate(String postedDate) {
    this.postedDate = postedDate;
  }

  public String getEffectiveDate() {
    return effectiveDate;
  }

  public void setEffectiveDate(String effectiveDate) {
      
    this.effectiveDate = effectiveDate;
  }

    /**
     * @return the Amt
     */
    public Double getAmt() {
        return Amt;
    }

    /**
     * @param Amt the Amt to set
     */
    public void setAmt(Double Amt) {
        this.Amt = Amt;
    }

    /**
     * @return the accountDate
     */
    public String getAccountDate() {
        return accountDate;
    }

    /**
     * @param accountDate the accountDate to set
     */
    public void setAccountDate(String accountDate) {
        this.accountDate = accountDate;
    }

    /**
     * @return the remoteIDwithoutExtention
     */
    public String getRemoteIDwithoutExtention() {
        return remoteIDwithoutExtention;
    }

    /**
     * @param remoteIDwithoutExtention the remoteIDwithoutExtention to set
     */
    public void setRemoteIDwithoutExtention(String remoteIDwithoutExtention) {
        this.remoteIDwithoutExtention = remoteIDwithoutExtention;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the voucherType
     */
    public String getVoucherType() {
        return voucherType;
    }

    /**
     * @param voucherType the voucherType to set
     */
    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    /**
     * @return the Narration
     */
    public String getNarration() {
        return Narration;
    }

    /**
     * @param Narration the Narration to set
     */
    public void setNarration(String Narration) {
        this.Narration = Narration;
    }

    /**
     * @return the dbRemoteId
     */
    

    /**
     * @param dbRemoteId the dbRemoteId to set
     */
  

    /**
     * @return the dbvchkey
     */
   

    /**
     * @param dbvchkey the dbvchkey to set
     */
   

}
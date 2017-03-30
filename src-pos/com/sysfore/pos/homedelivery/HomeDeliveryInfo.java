/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sysfore.pos.homedelivery;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author raghevendra
 */
public class HomeDeliveryInfo implements SerializableRead, SerializableWrite, Serializable{
    private String HomeDeliveryNO;
    private Date Billdate;
    private String DeliveryBoy;
    private Double BillAmount;
   // private Double AmountCollected;
    private double CollectedAmount;
    private double AdvanceIssued;
    private double AdvanceReturned;
    private String Status;
    private String DeliveryTo;
    private Date DeliveryDate;
     private double creditAmt;
     private String cusName;
     private String cusAddress;
     private String cusAddress1;
     private String city;
     private String isCod;
     private String isPaidStatus;
     private String ticketId;
     private String cusId;
     private String hdCusAddress;

    
    @Override
    public void readValues(DataRead dr) throws BasicException {
        setHomeDeliveryNO(dr.getString(1));
        setBilldate(dr.getTimestamp(2));
        setDeliveryBoy(dr.getString(3));
        setBillAmount(dr.getDouble(4));
    //    setAmountCollected(dr.getDouble(5));
        setCollectedAmount(dr.getDouble(5));
        setAdvanceIssued(dr.getDouble(6));
        setAdvanceReturned(dr.getDouble(7));
        setStatus(dr.getString(8));
        setDeliveryTo(dr.getString(9));
        setDeliveryDate(dr.getTimestamp(10));
        setCreditAmount(dr.getDouble(11));
        setCustName(dr.getString(12));
        setCusAddress(dr.getString(13));
        setCusAddress1(dr.getString(14));
        setCity(dr.getString(15));
        setIsCod(dr.getString(16));
        setIsPaidStatus(dr.getString(17));
        setTicketId(dr.getString(18));
        setCusId(dr.getString(19));
        setHdCusAddress(dr.getString(20));

        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void writeValues(DataWrite dp) throws BasicException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        dp.setString(1, getHomeDeliveryNO());
        dp.setTimestamp(2, getBilldate());
        dp.setString(3, getDeliveryBoy());
        dp.setDouble(4, getBillAmount());
      //  dp.setDouble(5, getAmountCollected());
        dp.setDouble(5, getCollectedAmount());
        dp.setDouble(6,getAdvanceIssued());
        dp.setDouble(7, getAdvanceReturned());
        dp.setString(8, getStatus());
        dp.setString(9, getDeliveryTo());
        dp.setTimestamp(10, getDeliveryDate());
        dp.setDouble(11, getCreditAmount());
        dp.setString(12, getCusName());
        dp.setString(13, getCusAddress());
        dp.setString(14, getCusAddress1());
        dp.setString(15, getCity());
        dp.setString(16, getIsCod());
        dp.setString(17, getIsPaidStatus());
        dp.setString(18, getTicketId());
        dp.setString(19, getCusId());
        dp.setString(20, getHdCusAddress());


    }

    /**
     * @return the HomeDeliveryNO
     */
    public String getHomeDeliveryNO() {
        return HomeDeliveryNO;
    }

    /**
     * @param HomeDeliveryNO the HomeDeliveryNO to set
     */
    public void setHomeDeliveryNO(String HomeDeliveryNO) {
        this.HomeDeliveryNO = HomeDeliveryNO;
    }

    /**
     * @return the Billdate
     */
    public Date getBilldate() {
        return Billdate;
    }

    /**
     * @param Billdate the Billdate to set
     */
    public void setBilldate(Date Billdate) {
        this.Billdate = Billdate;
    }

    /**
     * @return the DeliveryBoy
     */
    public String getDeliveryBoy() {
        return DeliveryBoy;
    }

    /**
     * @param DeliveryBoy the DeliveryBoy to set
     */
    public void setDeliveryBoy(String DeliveryBoy) {
        this.DeliveryBoy = DeliveryBoy;
    }

    /**
     * @return the BillAmount
     */
    public Double getBillAmount() {
        return BillAmount;
    }

    /**
     * @param BillAmount the BillAmount to set
     */
    public void setBillAmount(Double BillAmount) {
        this.BillAmount = BillAmount;
    }
    public Double getCreditAmount() {
        return creditAmt;
    }

    /**
     * @param BillAmount the BillAmount to set
     */
     public void setCreditAmount(Double creditAmt) {
        this.creditAmt = creditAmt;
    }

    /**
     * @return the AmountCollected
     */
//    public Double getAmountCollected() {
//        return AmountCollected;
//    }
//
//    /**
//     * @param AmountCollected the AmountCollected to set
//     */
//    public void setAmountCollected(Double AmountCollected) {
//        this.AmountCollected = AmountCollected;
//    }

    /**
     * @return the CollectedAmount
     */
    public double getCollectedAmount() {
        return CollectedAmount;
    }

    /**
     * @param CollectedAmount the CollectedAmount to set
     */
    public void setCollectedAmount(double CollectedAmount) {
        this.CollectedAmount = CollectedAmount;
    }

    public String getHdCusAddress() {
        return hdCusAddress;
    }

    public void setHdCusAddress(String hdCusAddress) {
        this.hdCusAddress = hdCusAddress;
    }

    /**
     * @return the AdvanceIssued
     */
    public double getAdvanceIssued() {
        return AdvanceIssued;
    }

    /**
     * @param AdvanceIssued the AdvanceIssued to set
     */
    public void setAdvanceIssued(double AdvanceIssued) {
        this.AdvanceIssued = AdvanceIssued;
    }

    /**
     * @return the AdvanceReturned
     */
    public double getAdvanceReturned() {
        return AdvanceReturned;
    }

    /**
     * @param AdvanceReturned the AdvanceReturned to set
     */
    public void setAdvanceReturned(double AdvanceReturned) {
        this.AdvanceReturned = AdvanceReturned;
    }

    /**
     * @return the Status
     */
    public String getStatus() {
        return Status;
    }

    /**
     * @param Status the Status to set
     */
    public void setStatus(String Status) {
        this.Status = Status;
    }

    /**
     * @return the DeliveryTo
     */
    public String getDeliveryTo() {
        System.out.println("getDeliveryTo is executed");
        return DeliveryTo;
    }

    /**
     * @param DeliveryTo the DeliveryTo to set
     */
    public void setDeliveryTo(String DeliveryTo) {
        this.DeliveryTo = DeliveryTo;
    }

    /**
     * @return the DeliveryDate
     */
    public Date getDeliveryDate() {
        return DeliveryDate;
    }

    /**
     * @param DeliveryDate the DeliveryDate to set
     */
    public void setDeliveryDate(Date DeliveryDate) {
        this.DeliveryDate = DeliveryDate;
    }

    private void setCustName(String cusName) {
        this.cusName = cusName;
    }
  public String getCusName() {
        return cusName==null?"":cusName;
    }
    private void setCusAddress(String cusAddress) {
        this.cusAddress = cusAddress;
    }
  public String getCusAddress() {
        return cusAddress==null?"":cusAddress+",";
    }
    private void setCusAddress1(String cusAddress1) {
     this.cusAddress1 = cusAddress1;
    }
  public String getCusAddress1() {
        return cusAddress1==null?"":cusAddress1+",";
    }
    private void setCity(String city) {
       this.city = city;
    }
  public String getCity() {
        return city==null?"":city;
    }
  public String getCustomerAdress(){
      return (getCusAddress()+getCusAddress1()+getCity());
  }

    private void setIsCod(String isCod) {
        this.isCod = isCod;
    }
    public String getIsCod() {
        return isCod;
    }
    private void setIsPaidStatus(String isPaidStatus) {
       this.isPaidStatus = isPaidStatus;
    }
     public String getIsPaidStatus() {
        return isPaidStatus;
    }

    private void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }
      public String getTicketId() {
        return ticketId;
    }
       private void setCusId(String cusId) {
        this.cusId = cusId;
    }
      public String getCusId() {
        return cusId;
    }

    /**
     * @return the HomeDeliveryNO
     */
  
    

}  
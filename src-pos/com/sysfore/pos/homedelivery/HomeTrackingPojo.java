/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sysfore.pos.homedelivery;

import java.util.Date;

/**
 *
 * @author raghevendra
 */
public class HomeTrackingPojo {
    
     private String HomeDeliveryNO;
    private Date Billdate;
    private String DeliveryBoy;
    private Double BillAmount;
    private Double AmountCollected;
    private String CollectedAmount;
    private String AdvanceIssued;
    private String AdvanceReturned;
    private String Status;
    private String DeliveryTo;
    private Date DeliveryDate;

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

    /**
     * @return the AmountCollected
     */
    public Double getAmountCollected() {
        return AmountCollected;
    }

    /**
     * @param AmountCollected the AmountCollected to set
     */
    public void setAmountCollected(Double AmountCollected) {
        this.AmountCollected = AmountCollected;
    }

    /**
     * @return the CollectedAmount
     */
    public String getCollectedAmount() {
        return CollectedAmount;
    }

    /**
     * @param CollectedAmount the CollectedAmount to set
     */
    public void setCollectedAmount(String CollectedAmount) {
        this.CollectedAmount = CollectedAmount;
    }

    /**
     * @return the AdvanceIssued
     */
    public String getAdvanceIssued() {
        return AdvanceIssued;
    }

    /**
     * @param AdvanceIssued the AdvanceIssued to set
     */
    public void setAdvanceIssued(String AdvanceIssued) {
        this.AdvanceIssued = AdvanceIssued;
    }

    /**
     * @return the AdvanceReturned
     */
    public String getAdvanceReturned() {
        System.out.println("------------------");
        System.out.println(AdvanceReturned);
         System.out.println("------------------");
        return AdvanceReturned;
    }

    /**
     * @param AdvanceReturned the AdvanceReturned to set
     */
    public void setAdvanceReturned(String AdvanceReturned) {
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

    
}

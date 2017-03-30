/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sysfore.pos.creditsale;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;
import java.util.Date;

/**
 *
 * @author shilpa
 */
public class CreditSalePojo implements SerializableRead, SerializableWrite{
    private String id;
    private String name;
    private String billno;
    private Date billdate;
    private Double billamount;
    private Double creditamount;
    private String chequeno;
    private Date chequedate;
    private String ticketId;

    
    @Override
    public void readValues(DataRead dr) throws BasicException {
        setId(dr.getString(1));
        setName(dr.getString(2));
        setBillno(dr.getString(3));
       setBilldate(dr.getTimestamp(4));
        setBillamount(dr.getDouble(5));
         setCreditamount(dr.getDouble(6));
          setTicketId(dr.getString(7));
       
    }

    @Override
    public void writeValues(DataWrite dp) throws BasicException {
         dp.setString(1,getId());
         dp.setString(2,getName());
         dp.setString(3, getBillno());
         dp.setTimestamp(4, getBilldate());
         dp.setDouble(5, getBillamount());
         dp.setDouble(6, getCreditamount());
          dp.setString(7, getTicketId());
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the billno
     */
    public String getBillno() {
        return billno;
    }

    /**
     * @param billno the billno to set
     */
    public void setBillno(String billno) {
        this.billno = billno;
    }

    /**
     * @return the billdate
     */
    public Date getBilldate() {
        return billdate;
    }

    /**
     * @param billdate the billdate to set
     */
    public void setBilldate(Date billdate) {
        this.billdate = billdate;
    }

    /**
     * @return the billamount
     */
    public Double getBillamount() {
        return billamount;
    }

    /**
     * @param billamount the billamount to set
     */
    public void setBillamount(Double billamount) {
        this.billamount = billamount;
    }

    /**
     * @return the creditamount
     */
    public Double getCreditamount() {
        return creditamount;
    }

    /**
     * @param creditamount the creditamount to set
     */
    public void setCreditamount(Double creditamount) {
        this.creditamount = creditamount;
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
     * @return the ticketId
     */
    public String getTicketId() {
        return ticketId;
    }

    /**
     * @param ticketId the ticketId to set
     */
    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    
    
}

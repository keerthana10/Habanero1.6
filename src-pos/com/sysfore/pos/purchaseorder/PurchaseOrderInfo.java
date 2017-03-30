/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sysfore.pos.purchaseorder;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;
import com.openbravo.format.Formats;
import java.util.Date;
import java.util.List;

/**
 *
 * @author mateen
 */
public class PurchaseOrderInfo implements SerializableRead, SerializableWrite{

    private long serialVersionUID = 123546456837719L;
    private String id;
    private String documentNo;
    private String vendor;
    private String created;
    private String delivered;
    private String status;
    private double  total;
    private Double subtotal;
    private String tax;
    private String address;
    private List<PurchaseOrderLine> m_aLines;

    public PurchaseOrderInfo() {
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
     * @return the documentNo
     */
    public String getDocumentNo() {
        return documentNo;
    }

    /**
     * @param documentNo the documentNo to set
     */
    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }

    /**
     * @return the vendor
     */
    public String getVendor() {
        return vendor;
    }

    /**
     * @param vendor the vendor to set
     */
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    /**
     * @return the created
     */
    public String getCreated() {
        return created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated(String created) {
        this.created = created;
    }

    /**
     * @return the delivered
     */
    public String getDelivered() {
        return delivered;
    }

    /**
     * @param delivered the delivered to set
     */
    public void setDelivered(String delivered) {
        this.delivered = delivered;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the total
     */
    public double getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(double total) {
        this.total = total;
    }
    public String getAddress() {
        return address;
    }

    /**
     * @param status the status to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    public void readValues(DataRead dr) throws BasicException {
        setId(dr.getString(1));
        setDocumentNo(dr.getString(2));
        setVendor(dr.getString(3));
        setCreated(dr.getString(4));
        setDelivered(dr.getString(5));
        setStatus(dr.getString(6));
        setTotal((double)dr.getDouble(7));
        setSubtotal((double)dr.getDouble(8));
        setTax(dr.getString(9));
        setAddress(dr.getString(10));
    }

    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, getId());
        dp.setString(2, getDocumentNo());
        dp.setString(3, getVendor());
        dp.setString(4, getCreated());
        dp.setString(5, getDelivered());
        dp.setString(6, getStatus());
        dp.setDouble(7, getTotal());
        dp.setDouble(8, getSubtotal());
        dp.setString(9, getTax());
        dp.setString(10, getAddress());
    }

    /**
     * @return the subtotal
     */
    public Double getSubtotal() {
        return subtotal;
    }

    /**
     * @param subtotal the subtotal to set
     */
    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    /**
     * @return the tax
     */
    public String getTax() {
        return tax;
    }

    /**
     * @param tax the tax to set
     */
    public void setTax(String tax) {
        this.tax = tax;
    }
    public String printTotal(){
        return Formats.CURRENCY.formatValue(new Double(getTotal()));
    }
    public List<PurchaseOrderLine> getLines() {
//        System.out.println("enrtrr---m_aLines"+this.m_aLines.size());
        return this.m_aLines;
    }

    public void setLines(List<PurchaseOrderLine> value) {

        m_aLines = value;

    }
    public String printDocumentNo(){
        return getDocumentNo();
    }
    public String printCreated(){
        return getCreated();
    }
     public String printDelivered(){
        return getDelivered();
    }
       public String printVendor(){
        return getVendor();
    }
        public String printAddress(){
        return getAddress();
    }
}
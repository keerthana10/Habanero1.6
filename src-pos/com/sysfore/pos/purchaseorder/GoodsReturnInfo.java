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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mateen
 */
public class GoodsReturnInfo implements SerializableRead, SerializableWrite{

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
    private List<GoodsReturnLine> m_aLines;
  private String store;
    private String warehouse;
    public GoodsReturnInfo() {
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
    
    public void setWarehouse(String Warehouse) {
        this.warehouse = Warehouse;
    }
    public String getWarehouse() {
        return warehouse;
    }

    /**
     * @return the created
     */
    public String getCreated() {
           SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
       try {
            Date date = formatter.parse(created);
          formatter.applyPattern("dd/MM/yyyy");
           created=formatter.format(date);
           System.out.println(created);
        } catch (ParseException ex) {
            Logger.getLogger(GoodsReturnInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        setWarehouse(dr.getString(11));
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
    public List<GoodsReturnLine> getLines() {
//        System.out.println("enrtrr---m_aLines"+this.m_aLines.size());
        return this.m_aLines;
    }

    public void setLines(List<GoodsReturnLine> value) {

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
         public String getStore() {
        return store;
    }

    /**
     * @param store the store to set
     */
    public void setStore(String store) {
        this.store = store;
    }

     public String printStore(){
        return getStore();
    }
}
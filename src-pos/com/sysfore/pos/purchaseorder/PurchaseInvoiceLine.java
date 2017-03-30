//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2009 Openbravo, S.L.
//    http://www.openbravo.com/product/pos
//
//    This file is part of Openbravo POS.
//
//    Openbravo POS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Openbravo POS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.

package com.sysfore.pos.purchaseorder;

import com.openbravo.format.Formats;
import com.openbravo.pos.ticket.ProductInfoExt;
import com.openbravo.pos.util.StringUtils;

/**
 *
 * @author mateen
 */
public class PurchaseInvoiceLine {
    //implements SerializableWrite, SerializableRead, Externalizable, Serializable{
    
    private double m_dMultiply;
    private double m_Discount;
    private double m_dPrice;
    
    private String m_sProdID;
    private String m_sProdName;
    private String taxes;
    private String taxid;
    private double total;
    private String uom;
    
    //private List<PurchaseOrderLine> list = new ArrayList<PurchaseOrderLine>();
 
    /** Creates a new instance of PurchaseOrder Line */
    public PurchaseInvoiceLine(ProductInfoExt oProduct) {
        m_sProdID = oProduct.getID();
        m_sProdName = oProduct.getName();
        m_dMultiply = oProduct.getMultiply();
        m_dPrice = oProduct.getPriceBuy();
        taxes = oProduct.getTaxCategoryID();
        taxid = oProduct.getCategoryID();
        uom = oProduct.getUom();
    }
     public PurchaseInvoiceLine(ProductInfoExt oProduct, double discount) {
        m_sProdID = oProduct.getID();
        m_sProdName = oProduct.getName();
        m_dMultiply = oProduct.getMultiply();
        m_dPrice = oProduct.getPriceBuy();
        taxes = oProduct.getTaxCategoryID();
        taxid = oProduct.getCategoryID();
        m_Discount = discount;
         uom = oProduct.getUom();
    }
    
    public PurchaseInvoiceLine(ProductInfoExt oProduct, double dpor, double dprice, String taxid) {
        m_sProdID = oProduct.getID();
        m_sProdName = oProduct.getName();
        m_dMultiply = dpor;
        m_dPrice = dprice;
        taxes = oProduct.getTaxCategoryID();
        this.taxid = taxid;
        uom = oProduct.getUom();
    }
    
    public String getProductID() {
        return m_sProdID;
    }    
      public String getUom() {
        return uom;
    }
    public void setUom(String uom) {
            this.uom = uom;
    }
    public String getProductName() {
        return m_sProdName;
    } 
    public void setProductName(String sValue) {
        if (m_sProdID == null) {
            m_sProdName = sValue;
        }
    }
    public double getMultiply() {
        return m_dMultiply;
    }
    
    public void setMultiply(double dValue) {
        m_dMultiply = dValue;
    }
    public double getDiscount() {
        return m_Discount;
    }
    public double getDiscountRate(){
       return (getDiscount()*0.01);
    }
    public void setDiscount(double dValue) {
        m_Discount = dValue;
    }
    public double getPrice() {
        return m_dPrice;
    }


    public double getTaxRate() {
///       String rate =
        return m_dPrice;
    }

      public double getTotalValue() {
        return m_dPrice;
    }
    
    public void setPrice(double dValue) {
        m_dPrice = dValue;
    }

     public double getTotal() {
         total = getMultiply() * getPrice();
        return total;
    }

    public void setTotal(double dValue) {
        total = dValue;
    }
    
    public double getSubValue() {
        return m_dMultiply * m_dPrice;
    }
    
    public String printName() {
        return StringUtils.encodeXML(m_sProdName);
    }
     public String printTotal() {
        return Formats.CURRENCY.formatValue(new Double(getPrice()*m_dMultiply));
    }
    public String printPrice() {
        if (m_dMultiply == 1.0) {
            return "";
        } else {
            return Formats.CURRENCY.formatValue(new Double(getPrice()));
        }
    }
    
    public String printMultiply() {
        return Formats.DOUBLE.formatValue(new Double(m_dMultiply));
    }
    
    public String printSubValue() {
        return Formats.CURRENCY.formatValue(new Double(getSubValue()));
    }
     public String printProductPrice() {
         return Formats.CURRENCY.formatValue(new Double(getPrice()));

    }
    public String printDiscount() {
         return Formats.DOUBLE.formatValue(getDiscount())+"%";

    }
    /**
     * @return the m_dTaxes
     */
    public String getTaxes() {
        return taxes;
    }

    /**
     * @param m_dTaxes the m_dTaxes to set
     */
    public void setTaxes(String taxes) {
        this.taxes = taxes;
    }
    public String printTaxid() {
        return getTaxes();
    }
    /**
     * @return the taxid
     */
    public String getTaxid() {
        return taxid;
    }

    /**
     * @param taxid the taxid to set
     */
    public void setTaxid(String taxid) {
        this.taxid = taxid;
    }

}

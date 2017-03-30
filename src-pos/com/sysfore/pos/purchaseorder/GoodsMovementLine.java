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
public class GoodsMovementLine {
    //implements SerializableWrite, SerializableRead, Externalizable, Serializable{
    
    private double m_dMultiply;    
    private double m_dPrice;
    
    private String m_sProdID;
    private String m_sProdName;
    private String taxes;
    private String taxid;
    private String uom;
    private Double units;
    
    //private List<GoodsReceiptsLine> list = new ArrayList<GoodsReceiptsLine>();
 
    /** Creates a new instance of PurchaseOrder Line */
    public GoodsMovementLine(ProductInfoExt oProduct) {
        m_sProdID = oProduct.getID();
        m_sProdName = oProduct.getName();
        m_dMultiply = oProduct.getMultiply();
        m_dPrice = oProduct.getPriceSell();
        taxes = oProduct.getTaxCategoryID();
        taxid = oProduct.getCategoryID();
        uom = oProduct.getUom();
        
    }
    public GoodsMovementLine(ProductInfoExt oProduct,Double units) {
        m_sProdID = oProduct.getID();
        m_sProdName = oProduct.getName();
        m_dMultiply = oProduct.getMultiply();
        m_dPrice = oProduct.getPriceSell();
        taxes = oProduct.getTaxCategoryID();
        taxid = oProduct.getCategoryID();
        uom = oProduct.getUom();
        this.units=units;
        
    }
    
    public GoodsMovementLine(ProductInfoExt oProduct, double dpor, double dprice, String taxid,Double units) {
        m_sProdID = oProduct.getID();
        m_sProdName = oProduct.getName();
        m_dMultiply = dpor;
        m_dPrice = dprice;
        taxes = oProduct.getTaxCategoryID();
        this.taxid = taxid;
        this.uom = oProduct.getUom();
        this.units=units;
    }
    
    public String getProductID() {
        return m_sProdID;
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
    
     public double getUnits() {
        return units;
    }
    
    public void setUnits(double dunits) {
        units = dunits;
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
public String getUom() {
        return uom;
    }

    /**
     * @param taxid the taxid to set
     */
    public void setUom(String uom) {

        this.uom = uom;
    }
}

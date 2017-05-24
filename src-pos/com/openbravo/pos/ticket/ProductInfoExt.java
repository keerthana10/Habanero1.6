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

package com.openbravo.pos.ticket;

import java.awt.image.BufferedImage;
import com.openbravo.data.loader.DataRead;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.ImageUtils;
import com.openbravo.data.loader.SerializerRead;
import com.openbravo.format.Formats;
import java.util.Properties;

/**
 *
 * @author adrianromero
 *
 */
public class ProductInfoExt {

    private static final long serialVersionUID = 7587696873036L;

    protected String m_ID;
    protected String m_sRef;
    protected String m_sCode;
    protected String m_sName;
    protected boolean m_bCom;
    protected boolean m_bScale;
    protected String categoryid;
    protected String taxcategoryid;
    protected String attributesetid;
    protected double m_dPriceBuy;
    protected double m_dPriceSell;
    protected BufferedImage m_Image;
    private double multiply;
    protected Properties attributes;
    protected String itemCode;
    protected double mrp;
    private double discount;
    private String uom;
    private String productType;
    private String productionAreaType;
    private String servicechargeid;
    private String servicetaxid;
    private String parentCatId;
    private String preparationTime;
    private String swachBharatTaxId;
     private String comboProduct;

    /** Creates new ProductInfo */
    public ProductInfoExt() {
        m_ID = null;
        m_sRef = "0000";
        m_sCode = "0000";
        m_sName = null;
        m_bCom = false;
        m_bScale = false;
        categoryid = null;
        taxcategoryid = null;
        attributesetid = null;
        m_dPriceBuy = 0.0;
        m_dPriceSell = 0.0;
        m_Image = null;
         multiply = 1;
        attributes = new Properties();
        itemCode = null;
        mrp = 0.0;
        uom = null;
        productType=null;
        productionAreaType=null;
        servicechargeid=null;
        servicetaxid=null;
        parentCatId=null;
        preparationTime=null;
        swachBharatTaxId=null;
        comboProduct=null;
    }

    public final String getID() {
        return m_ID;
    }

    public final void setID(String id) {
        m_ID = id;
    }

    public final String getReference() {
        return m_sRef;
    }

    public final void setReference(String sRef) {
        m_sRef = sRef;
    }
    public final String getItemCode() {
        return itemCode;
    }

    public final void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    public final String getCode() {
        return m_sCode;
    }

    public final void setCode(String sCode) {
        m_sCode = sCode;
    }

    public final String getName() {
        return m_sName;
    }

    public final void setName(String sName) {
        m_sName = sName;
    }

    public final boolean isCom() {
        return m_bCom;
    }

    public final void setCom(boolean bValue) {
        m_bCom = bValue;
    }

    public final boolean isScale() {
        return m_bScale;
    }

    public final void setScale(boolean bValue) {
        m_bScale = bValue;
    }

    public final String getCategoryID() {
        return categoryid;
    }

    public final void setCategoryID(String sCategoryID) {
        categoryid = sCategoryID;
    }

    public final String getTaxCategoryID() {
        return taxcategoryid;
    }

    public final void setTaxCategoryID(String value) {
        taxcategoryid = value;
    }

    public final String getAttributeSetID() {
        return attributesetid;
    }
    public final void setAttributeSetID(String value) {
        attributesetid = value;
    }

    public final double getPriceBuy() {
        return m_dPriceBuy;
    }

    public final void setPriceBuy(double dPrice) {
        m_dPriceBuy = dPrice;
    }
     public final double getMrp() {
        return mrp;
    }

    public final void setMrp(double dMrp) {
        mrp = dMrp;
    }

    public final double getPriceSell() {
        return m_dPriceSell;
    }

    public final void setPriceSell(double dPrice) {
        m_dPriceSell = dPrice;
    }

    public final double getPriceSellTax(TaxInfo tax) {
        return m_dPriceSell * (1.0 + tax.getRate());
    }

    public String printPriceSell() {
        return Formats.CURRENCY.formatValue(new Double(getPriceSell()));
    }

    public String printPriceSellTax(TaxInfo tax) {
        return Formats.CURRENCY.formatValue(new Double(getPriceSellTax(tax)));
    }
    
    public BufferedImage getImage() {
        return m_Image;
    }
    public void setImage(BufferedImage img) {
        m_Image = img;
    }
    
    public String getProperty(String key) {
        return attributes.getProperty(key);
    }
    public String getProperty(String key, String defaultvalue) {
        return attributes.getProperty(key, defaultvalue);
    }
    public void setProperty(String key, String value) {
        attributes.setProperty(key, value);
    }
    public Properties getProperties() {
        return attributes;
    }
 public double getMultiply() {
        return multiply;
    }
 public final String getUom() {
        return uom;
    }

    public final void setUom(String uom) {
       this.uom = uom;
    }
     public final String getProductType() {
        return productType;
    }

    public final void setProductType(String productType) {
        this.productType = productType;
    }
    /**
     * @param multiply the multiply to set
     */
    public void setMultiply(double multiply) {
        this.multiply = multiply;
    }
    public static SerializerRead getSerializerRead() {
        return new SerializerRead() { public Object readValues(DataRead dr) throws BasicException {
            ProductInfoExt product = new ProductInfoExt();
            product.m_ID = dr.getString(1);
            product.m_sRef = dr.getString(2);
            product.m_sCode = dr.getString(3);
            product.m_sName = dr.getString(4);
            product.m_bCom = dr.getBoolean(5).booleanValue();
            product.m_bScale = dr.getBoolean(6).booleanValue();
            product.m_dPriceBuy = dr.getDouble(7).doubleValue();
            product.m_dPriceSell = dr.getDouble(8).doubleValue();
            product.taxcategoryid = dr.getString(9);
            product.categoryid = dr.getString(10);
            product.attributesetid = dr.getString(11);
            product.m_Image = ImageUtils.readImage(dr.getBytes(12));
            product.attributes = ImageUtils.readProperties(dr.getBytes(13));
            product.itemCode = dr.getString(14);
            product.mrp = dr.getDouble(15).doubleValue();
            product.uom = dr.getString(16);
            product.productType = dr.getString(17);
            product.productionAreaType=dr.getString(18);
            product.servicechargeid=dr.getString(19);
            product.servicetaxid=dr.getString(20);
            product.parentCatId=dr.getString(21);
            product.preparationTime=dr.getString(22);
            product.swachBharatTaxId=dr.getString(23);
            product.comboProduct=dr.getString(24);
            return product;
        }};
    }

    @Override
    public final String toString() {
        return m_sRef + " - " + m_sName;
    }

    /**
     * @return the productionAreaType
     */
    public String getProductionAreaType() {
        return productionAreaType;
    }

    /**
     * @param productionAreaType the productionAreaType to set
     */
    public void setProductionAreaType(String productionAreaType) {
        this.productionAreaType = productionAreaType;
    }
    
       public final String getServiceChargeID() {
        return servicechargeid;
    }

    public final void setServiceChargeID(String value) {
        servicechargeid = value;
    }
    
      public final String getServiceTaxID() {
        return servicetaxid;
    }

    public final void setServiceTaxID(String value) {
        servicetaxid = value;
    }

    /**
     * @return the parentCatId
     */
    public String getParentCatId() {
        return parentCatId;
    }

    /**
     * @param parentCatId the parentCatId to set
     */
    public void setParentCatId(String parentCatId) {
        this.parentCatId = parentCatId;
    }

    /**
     * @return the preparationTime
     */
    public String getPreparationTime() {
        return preparationTime;
    }

    /**
     * @param preparationTime the preparationTime to set
     */
    public void setPreparationTime(String preparationTime) {
        this.preparationTime = preparationTime;
    }
       public final String getSwachBharatTaxId() {
        return swachBharatTaxId;
    }

    public final void setSwachBharatTaxId(String value) {
        swachBharatTaxId = value;
    }

    /**
     * @return the comboProduct
     */
    public String getComboProduct() {
        return comboProduct;
    }

    /**
     * @param comboProduct the comboProduct to set
     */
    public void setComboProduct(String comboProduct) {
        this.comboProduct = comboProduct;
    }

}

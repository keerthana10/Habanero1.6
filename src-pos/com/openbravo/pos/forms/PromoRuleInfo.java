/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.forms;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;
import java.io.Serializable;

/**
 *
 * @author preethi
 */
public class PromoRuleInfo implements SerializableRead, SerializableWrite, Serializable  {
   
    private String promotypeId;
    private double buyQty;
    private double getQty;
    private double billAmount;
    private double minValue;
    private double value;
    private String isSku;
    private String isPromProduct;
    private String isCrossProduct;
    private String isMultiplePromoProduct;
    private String isMultipleCrossProduct;
    private String promoId;
    private String campaignId;
   
  public PromoRuleInfo() {
    }

 public void readValues(DataRead dr) throws BasicException {

       promotypeId = dr.getString(1);
       buyQty = dr.getDouble(2);
       getQty = dr.getDouble(3);
       billAmount = dr.getDouble(4);
       minValue = dr.getDouble(5);
       value = dr.getDouble(6);
       isSku = dr.getString(7);
       isPromProduct = dr.getString(8);
       isCrossProduct = dr.getString(9);
       isMultiplePromoProduct = dr.getString(10);
       isMultipleCrossProduct = dr.getString(11);
       promoId = dr.getString(12);
       campaignId = dr.getString(13);

    }
    public void writeValues(DataWrite dp) throws BasicException {


        dp.setString(1, promotypeId);
        dp.setDouble(2, buyQty);
        dp.setDouble(3, getQty);
        dp.setDouble(4, billAmount);
        dp.setDouble(5, minValue);
        dp.setDouble(6, value);
        dp.setString(7, isSku);
        dp.setString(8, isPromProduct);
        dp.setString(9, isCrossProduct);
        dp.setString(10,isMultiplePromoProduct);
        dp.setString(11,isMultipleCrossProduct);
        dp.setString(12, promoId);
        dp.setString(13, campaignId);

    }

    public String getpromotypeId(){
        return promotypeId;
    }
    public void setpromotypeId(String promotypeId){
        this.promotypeId = promotypeId;
    }
    public double getBuyQty(){
        return buyQty;
    }
    public void setBuyQty(double buyQty){
        this.buyQty = buyQty;
    }
    public double getQty(){
        return getQty;
    }
    public void setQty(double getQty){
        this.getQty = getQty;
    }
    public double getBillAmount(){
        return billAmount;
    }
    public void setBillAmount(double billAmount){
        this.billAmount = billAmount;
    }
    public double getMinValue(){
        return minValue;
    }
   public void setMinValue(double minValue){
        this.minValue = minValue;
    }
   public double getValue(){
        return value;
    }
   public void setValue(double value){
        this.value = value;
    }
   public String getIsSku(){
        return isSku;
    }
    public void setIsSku(String isSku){
        this.isSku = isSku;
    }
      public String getIsPromProduct(){
        return isPromProduct;
    }
    public void setIsPromProduct(String isPromProduct){
        this.isPromProduct = isPromProduct;
    }
      public String getIsCrossProduct(){
        return isCrossProduct;
    }
    public void setIsCrossProduct(String isCrossProduct){
        this.isCrossProduct = isCrossProduct;
    }
      public String getIsMultiplePromoProduct(){
        return isMultiplePromoProduct;
    }
    public void setIsMultiplePromoProduct(String isMultiplePromoProduct){
        this.isMultiplePromoProduct = isMultiplePromoProduct;
    }
      public String getIsMultipleCrossProduct(){
        return isMultipleCrossProduct;
    }
    public void setIsMultipleCrossProduct(String isMultipleCrossProduct){
        this.isMultipleCrossProduct = isMultipleCrossProduct;
    }
     public String getpromoId(){
         
        return promoId;
    }
    public void setpromoId(String promoId){
        this.promoId = promoId;
    }
    public String getCampaignId(){
        return campaignId == null ? "" : campaignId;
    }
    public void setCampaignId(String campaignId){
        this.campaignId = campaignId;
    }

}
 
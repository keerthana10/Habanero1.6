/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sysfore.pos.cashmanagement;

import com.openbravo.basic.BasicException;
import com.openbravo.pos.forms.AppView;
import com.sysfore.pos.purchaseorder.PurchaseOrderReceipts;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author mateen
 */
public class CurrencyConfigLoader {

    private String country;
    private String language;
    private List<String> currList;
    private PurchaseOrderReceipts m_dlPOReceipts;
    private AppView app;
    
    public CurrencyConfigLoader(String country, String language, AppView app) {
        this.country = country;
        this.language = language;
        this.app = app;
        m_dlPOReceipts = (PurchaseOrderReceipts) app.getBean("com.sysfore.pos.purchaseorder.PurchaseOrderReceipts");
    }

    public CurrencyConfigLoader(AppView app){
        this.country = Locale.getDefault().getCountry();
        this.language = Locale.getDefault().getLanguage();
        this.app = app;
        this.m_dlPOReceipts = (PurchaseOrderReceipts) app.getBean("com.sysfore.pos.purchaseorder.PurchaseOrderReceipts");
    }

    public List<CurrencyInfo> loadCurrencyArray() throws BasicException {
        currList = new ArrayList<String>();
        return m_dlPOReceipts.getCurrencyInfobyCountry(country);
       
    }
}

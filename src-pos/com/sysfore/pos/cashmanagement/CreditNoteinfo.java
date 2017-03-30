/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sysfore.pos.cashmanagement;

/**
 *
 * @author mateen
 */
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;
import com.openbravo.format.Formats;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author adrianromero
 */
public class CreditNoteinfo implements SerializableRead, SerializableWrite {

    private static final long serialVersionUID = 7640633837719L;
    private String id;
    private String creditNote;
    private String customer;
    private String amount;
    private String status;
    private Date validity;
    private Date created;
    /** Creates a new instance of DiscountRateinfo */
    public CreditNoteinfo() {
    }

    public void readValues(DataRead dr) throws BasicException {
        id = dr.getString(1);
        creditNote = dr.getString(2);
        customer = dr.getString(3);
        amount =dr.getString(4);
        status=dr.getString(5);
        validity = dr.getTimestamp(6);
        created = dr.getTimestamp(7);
    }
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, id);
        dp.setString(2, creditNote);
        dp.setString(3, customer);
        dp.setString(4, amount);
        dp.setString(5, status);
        dp.setTimestamp(6, validity);
        dp.setTimestamp(7,created);
    }
    public String getId() {
        return id;
    }

    public String getcreditNote() {
        return creditNote;
    }

    public String getcustomer() {
        return customer;
    }


    /**
     * @return the name
     */
    public String getAmount() {
        return amount;
    }

     public Date getValidity() {
        return validity;
    }
        public Date getCreated() {
        return created;
    }
    public String getDateForPrint() {
     SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
      return sdf.format(created).toString();
    }

     public String getValidDateForPrint() {
     SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
      return sdf.format(validity).toString();
    }


    /**
     * @param name the name to set
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getstatus() {
       return status;
    }


    public String printCreditNote() {

      return getcreditNote();
    }

    public String printDateForPrint() {

      return getDateForPrint();
    }
   
     public String printcustomer() {

      return getcustomer();
    }


 public String printAmount() {
        return Formats.CURRENCY.formatValue(new Double(getAmount()));
    }
 public String printValidDateForPrint() {
     //SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
      return getValidDateForPrint();
    }
}

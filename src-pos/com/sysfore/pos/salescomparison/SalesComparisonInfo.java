/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sysfore.pos.salescomparison;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;
import com.openbravo.format.Formats;
import java.util.Date;

/**
 *
 * @author preethi
 */
public class SalesComparisonInfo implements SerializableRead, SerializableWrite{
    private double  sourceAmt;
    private double destinationAmt;
 
    public void readValues(DataRead dr) throws BasicException {
       sourceAmt = dr.getDouble(1);
       destinationAmt = dr.getDouble(2);
      

    }

    public void writeValues(DataWrite dp) throws BasicException {
    
        dp.setDouble(1, sourceAmt);
        dp.setDouble(2, destinationAmt);
      
    }
  

    public double getSourceAmt() {
        return sourceAmt;
    }

    public void setSourceAmt(double sourceAmt) {
        this.sourceAmt = sourceAmt;
    }
    public double getDestinationAmt() {
        return destinationAmt;
    }
    public void setDestinationAmt(double destinationAmt) {
        this.destinationAmt = destinationAmt;
    }

    public double getComparison() {
       double comparison = getSourceAmt()-getDestinationAmt();
        return comparison;
    }

    public double getPercentage() {
        double percentage;
        if(getComparison() < 0){
            percentage = (getComparison() /getDestinationAmt())*100;
        }else{
            percentage = (getComparison() /getSourceAmt())*100;
        }

        return percentage;
    }

     public String printSourceAmt() {
        return Formats.DoubleValue.formatValue(new Double(sourceAmt));
    }
      public String printDestinationAmt() {
        return Formats.DoubleValue.formatValue(new Double(destinationAmt));
    }
      public String printComparison() {
        return Formats.DoubleValue.formatValue(new Double(getComparison()));
    }
      public String printPercentage() {
        return Formats.DoubleValue.formatValue(new Double(getPercentage() ));
    }
}

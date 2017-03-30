/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.util.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author shilpa
 */
public class DateFormats {
      static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      
      public static String DateToString(Date input){
           //to convert Date to String, use format method of SimpleDateFormat class.
                String strDate = dateFormat.format(input);
          return strDate;
          
      }
      
       public static Date StringToDateTime(String input){
          SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         Date date=null;
          try
        {
             date = simpleDateFormat.parse(input);

            System.out.println("date : "+simpleDateFormat.format(date));
        }
        catch (ParseException ex)
        {
            System.out.println("Exception "+ex);
        }
          return date;
      }
}

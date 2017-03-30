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

import java.io.Serializable;
import com.openbravo.data.loader.IKeyed;

/**
 *
 * @author adrianromero
 */
public class ServiceChargeInfo implements Serializable, IKeyed {
    
    private static final long serialVersionUID = -2705212098856473043L;
    private String id;
    private String name;
    private double rate;
    
    
    
    /** Creates new ServiceChargeInfo */
    public ServiceChargeInfo(String id,String name,double rate) {
        this.id = id;
        this.name = name;
        this.rate = rate;
       
       
    }
    
    public Object getKey() {
        return id;
    }
    
    public void setID(String value) {
        id = value;
    }
    
    public String getId() {
        return id;
    }

    public String getName() {
    
        //Splitting the name vat 
        String[] taxName = name.split("_");
        name=taxName[0];
        return name;
    }
    
    public void setName(String value) {
        name = value;
    }




    public double getRate() {
        return rate;
    }
    
    public void setRate(double value) {
        rate = value;
    }

   
  
    
   @Override
    public String toString(){
        return name;
    }
}

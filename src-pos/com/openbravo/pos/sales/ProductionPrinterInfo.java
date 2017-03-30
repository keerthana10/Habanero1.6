/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author preethi
 */

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

package com.openbravo.pos.sales;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;
import com.openbravo.format.Formats;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author adrianromero
 */
public class ProductionPrinterInfo implements SerializableRead, SerializableWrite {

    private static long serialVersionUID = 7640633837719L;

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }
    private String productionAreaType;
    private String productionArea;
    private String path;
    private String name;
    //private List<kotInfo> productname;


    /** Creates a new instance of SharedTicketInfo */

     public ProductionPrinterInfo() {


    }

     public void writeExternal(ObjectOutput out) throws IOException {
        // esto es solo para serializar tickets que no estan en la bolsa de tickets pendientes
        out.writeObject(productionAreaType);
        out.writeObject(productionArea);
        out.writeObject(path);
        out.writeObject(getName());
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        // esto es solo para serializar tickets que no estan en la bolsa de tickets pendientes
        productionAreaType = (String) in.readObject();
        productionArea = (String) in.readObject();
        path = null;
        setName((String) in.readObject());;

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getProductionArea() {
        return productionArea;
    }

    public void setProductionArea(String productionArea) {
        this.productionArea = productionArea;
    }

    public String getProductionAreaType() {
        return productionAreaType;
    }

    public void setProductionAreaType(String productionAreaType) {
        this.productionAreaType = productionAreaType;
    }


    /**
     * @return the id
     */


    public void readValues(DataRead dr) throws BasicException {
        productionAreaType = dr.getString(1);
        productionArea = dr.getString(2);
        path = dr.getString(3);
        setName(dr.getString(4));

    }

    public void writeValues(DataWrite dp) throws BasicException {

        dp.setString(1, productionAreaType);
        dp.setString(2, productionArea);
        dp.setString(3, path);
        dp.setString(4, getName());

    }
 public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

}

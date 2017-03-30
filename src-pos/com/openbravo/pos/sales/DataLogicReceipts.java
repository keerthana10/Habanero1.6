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

import java.util.List;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataParams;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.data.loader.SerializerWriteBasicExt;
import com.openbravo.data.loader.SerializerWriteParams;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.Session;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.Transaction;
import com.openbravo.pos.forms.BeanFactoryDataSingle;
import com.openbravo.pos.inventory.BeanInfo;
import com.openbravo.pos.ticket.RetailTicketInfo;
import com.openbravo.pos.ticket.RetailTicketLineInfo;
import com.openbravo.pos.ticket.TicketInfo;
import com.sysfore.pos.panels.CardTypeInfo;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adrianromero
 */
public class DataLogicReceipts extends BeanFactoryDataSingle {
    
    private Session s;
    
    /** Creates a new instance of DataLogicReceipts */
    public DataLogicReceipts() {
    }
    
    public void init(Session s){
        this.s = s;
    }     
    public final TicketInfo getSharedTicket(String Id) throws BasicException {
        
        if (Id == null) {
            return null; 
        } else {
            Object[]record = (Object[]) new StaticSentence(s
                    , "SELECT CONTENT FROM SHAREDTICKETS WHERE ID = ?"
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.SERIALIZABLE})).find(Id);
            return record == null ? null : (TicketInfo) record[0];
        }
    }
     public final RetailTicketInfo getRetailSharedTicket(String Id) throws BasicException {
        System.out.println("retriving content from  db"+Id);
        if (Id == null) {
            return null;
        } else {
            Object[]record = (Object[]) new StaticSentence(s
                    , "SELECT CONTENT FROM SHAREDTICKETS WHERE ID = ?"
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.SERIALIZABLE})).find(Id);
            return record == null ? null : (RetailTicketInfo) record[0];
        }
    }
    
    public final List<SharedTicketInfo> getSharedTicketList() throws BasicException {
        
        return (List<SharedTicketInfo>) new StaticSentence(s
                , "SELECT ID, NAME,ISPRINTED,ISMODIFIED FROM SHAREDTICKETS ORDER BY ID"
                , null
                , new SerializerReadClass(SharedTicketInfo.class)).list();
    }
    
    public final void updateSharedTicket(final String id, final TicketInfo ticket) throws BasicException {
         
        Object[] values = new Object[] {id, ticket.getName(), ticket};
        Datas[] datas = new Datas[] {Datas.STRING, Datas.STRING, Datas.SERIALIZABLE};
        new PreparedSentence(s
                , "UPDATE SHAREDTICKETS SET NAME = ?, CONTENT = ? WHERE ID = ?"
                , new SerializerWriteBasicExt(datas, new int[] {1, 2, 0})).exec(values);
    }
    public final void updateSharedTicket(final String id, final RetailTicketInfo ticket) throws BasicException {
        System.out.println("in updateSharedTicket method"+ticket.isTicketOpen());  
        Object[] values = new Object[] {id, ticket.getName(), ticket,ticket.getSplitSharedId(), ticket.isPrinted(), ticket.isListModified()};
        Datas[] datas = new Datas[] {Datas.STRING, Datas.STRING, Datas.SERIALIZABLE, Datas.STRING, Datas.BOOLEAN, Datas.BOOLEAN};
        new PreparedSentence(s
                , "UPDATE SHAREDTICKETS SET NAME = ?, CONTENT = ?, ISPRINTED = ?, ISMODIFIED = ? WHERE ID = ? AND SPLITID=? "
                , new SerializerWriteBasicExt(datas, new int[] {1, 2, 4, 5, 0,3})).exec(values);
    }
    
    
    public final void insertSharedTicket(final String id, final TicketInfo ticket) throws BasicException {
        
        Object[] values = new Object[] {id, ticket.getName(), ticket};
        Datas[] datas = new Datas[] {Datas.STRING, Datas.STRING, Datas.SERIALIZABLE};
        
        new PreparedSentence(s
            , "INSERT INTO SHAREDTICKETS (ID, NAME,CONTENT) VALUES (?, ?, ?)"
            , new SerializerWriteBasicExt(datas, new int[] {0, 1, 2})).exec(values);
    }
    public final void insertTableCovers(final String id, final String tableid, int covers, String splitId) throws BasicException {

        Object[] values = new Object[] {id, tableid, covers,splitId};
        Datas[] datas = new Datas[] {Datas.STRING, Datas.STRING, Datas.INT,Datas.STRING};

        new PreparedSentence(s
            , "INSERT INTO TABLECOVERS (ID, TABLEID, NOOFCOVERS,SPLITID) VALUES (?, ?, ?, ?)"
            , new SerializerWriteBasicExt(datas, new int[] {0, 1, 2, 3})).exec(values);
    }
    public final void insertRetailSharedTicket(final String id, final RetailTicketInfo ticket) throws BasicException {
         String splitId= UUID.randomUUID().toString().replaceAll("-", "");
         ticket.setSplitSharedId(splitId);
         Object[] values = new Object[] {id, ticket.getName(), ticket,ticket.getSplitSharedId(),ticket.isPrinted(),ticket.isModified()};
        Datas[] datas = new Datas[] {Datas.STRING, Datas.STRING, Datas.SERIALIZABLE,Datas.STRING,Datas.BOOLEAN,Datas.BOOLEAN};

        new PreparedSentence(s
            , "INSERT INTO SHAREDTICKETS (ID, NAME,CONTENT,SPLITID,ISPRINTED,ISMODIFIED) VALUES (?, ?, ?,?,?,?)"
            , new SerializerWriteBasicExt(datas, new int[] {0, 1, 2,3,4,5})).exec(values);
    }
    
    public final void deleteSharedTicket(final String id) throws BasicException {

        new StaticSentence(s
            , "DELETE FROM SHAREDTICKETS WHERE ID = ?"
            , SerializerWriteString.INSTANCE).exec(id);      
    }
    


 public String getRolebyName(String user) throws BasicException {
        if (user == null) {
            return null;
        } else {
            Object[] record = (Object[]) new StaticSentence(s, "SELECT R.NAME AS rolename FROM ROLES R,PEOPLE P WHERE P.ROLE=R.ID AND P.NAME=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(user);
            return record == null ? "" : record[0].toString();
        }
    }
 public List<DiscountRateinfo> getDiscountList() throws BasicException {

        return (List<DiscountRateinfo>) new StaticSentence(s, "SELECT ID, RATE, NAME FROM DISCOUNTRATE ", null, new SerializerReadClass(DiscountRateinfo.class)).list();
    }
 public List<DiscountReasonInfo> getDiscountReason() throws BasicException {

        return (List<DiscountReasonInfo>) new StaticSentence(s, "SELECT ID, REASON FROM DISCOUNTREASON ", null, new SerializerReadClass(DiscountReasonInfo.class)).list();
    }
 public String getDiscountLine(String id) throws BasicException {

        Object[] record = (Object[]) new StaticSentence(s, "SELECT RATE FROM DISCOUNTRATE WHERE NAME = ?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(id);
        return record == null ? null : (String) record[0];
    }
 public List<Reasoninfo> getReasonList() throws BasicException {
          return (List<Reasoninfo>) new StaticSentence(s, "SELECT ID, REASON, STATUS FROM KOTREASON ", null, new SerializerReadClass(Reasoninfo.class)).list();
    }

   public String getReasonId(String reasonItem) throws BasicException {
        System.out.println("enrtr "+reasonItem);
        Object[] record = (Object[]) new StaticSentence(s, "SELECT ID FROM KOTREASON WHERE REASON = ?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(reasonItem);
         return record == null ? null : (String) record[0];
    }
//    public void updateKotCancel(String productId, String id, String isCancelled,String reason,double qty,String reasonId) {
//
//           Object[] values = new Object[] {productId,id,qty,isCancelled,reason,reasonId};
//
//        Datas[] datas = new Datas[] {Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.STRING,  Datas.STRING,  Datas.STRING};
//            try {
//                new PreparedSentence(s, "UPDATE KOT SET REASONID=? ,REASON = ?, ISCANCELLED=?,ISPRINTED='N', QTY=? WHERE TICKET = ? AND PRODUCTID = ?", new SerializerWriteBasicExt(datas, new int[]{5, 4, 3, 2, 1, 0})).exec(values);
//            } catch (BasicException ex) {
//                Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
//
//         }
//
//
//    }

     public void updateNoOfCovers(String tableid,int noofcovers, String splitId) {

           Object[] values = new Object[] {tableid,noofcovers,splitId};

        Datas[] datas = new Datas[] {Datas.STRING, Datas.INT, Datas.STRING};
            try {
                new PreparedSentence(s, "UPDATE TABLECOVERS SET NOOFCOVERS=? WHERE TABLEID = ? AND SPLITID=?", new SerializerWriteBasicExt(datas, new int[]{ 1, 0, 2})).exec(values);
            } catch (BasicException ex) {
                Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);

         }
    }

       public void updateTableName(String tableid,String newTableid,String splitId) {

           Object[] values = new Object[] {tableid,newTableid,splitId};

        Datas[] datas = new Datas[] {Datas.STRING, Datas.STRING,Datas.STRING};
            try {
                new PreparedSentence(s, "UPDATE TABLECOVERS SET TABLEID=?, SPLITID=? WHERE TABLEID = ? ", new SerializerWriteBasicExt(datas, new int[]{ 1,2, 0})).exec(values);
            } catch (BasicException ex) {
                Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);

         }


    }
//     public void updateKotCancel(String productId, String id, String isCancelled,String reason) {
//
//           Object[] values = new Object[] {productId,id,isCancelled,reason};
//
//        Datas[] datas = new Datas[] {Datas.STRING, Datas.STRING,  Datas.STRING,  Datas.STRING};
//            try {
//                new PreparedSentence(s, "UPDATE KOT SET REASON = ?, ISCANCELLED=?,ISPRINTED='N' WHERE TICKET = ? AND PRODUCTID = ?", new SerializerWriteBasicExt(datas, new int[]{3, 2, 1, 0})).exec(values);
//            } catch (BasicException ex) {
//                Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
//
//         }
//
//
//    }
      public final List<KotTicketListInfo> getKotTicketList(String ticketid) throws BasicException {

        return (List<KotTicketListInfo>) new StaticSentence(s
                , "SELECT DISTINCT KOTID FROM KOT  WHERE TICKET='"+ticketid+"' AND ISCANCELLED='N'"
                , null
                , new SerializerReadClass(KotTicketListInfo.class)).list();
    }

       public String getkotTicketId(String isPrinted) throws BasicException {

             Object[] record = (Object[]) new StaticSentence(s, "SELECT MAX(KOTID) FROM KOT WHERE ISPRINTED='"+isPrinted+"'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(isPrinted);
            return record == null ? "" : (String) record[0];
        }

     public int getTableCovers(String tableId,String splitId) throws BasicException {
        Object[] record = null;

        record = (Object[]) new StaticSentence(s
                    ,"SELECT NOOFCOVERS FROM TABLECOVERS WHERE TABLEID='"+tableId+"' AND SPLITID='"+splitId+"'  "
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            int i = Integer.parseInt(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);


    }

   

      public int getkotIsprinted(String productid, String ticketId) throws BasicException {
        Object[] record = null;

      record = (Object[]) new StaticSentence(s
                    ,"SELECT COUNT(*) FROM KOT WHERE PRODUCTID='"+productid+"' AND TICKET='"+ticketId+"' AND ISPRINTED='Y' AND ISCANCELLED='N'"
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            int i = Integer.parseInt(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);


    }
 public int getkotprinted(String ticketId) throws BasicException {
        Object[] record = null;

      record = (Object[]) new StaticSentence(s
                    ,"SELECT COUNT(*) FROM KOT WHERE TICKET='"+ticketId+"' AND ISPRINTED='Y'"
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            int i = Integer.parseInt(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);


    }

         public double getkotQty(String productid, String ticketId) throws BasicException {
        Object[] record = null;

      record = (Object[]) new StaticSentence(s
                    ,"SELECT sum(QTY) FROM KOT WHERE PRODUCTID='"+productid+"' AND TICKET='"+ticketId+"' AND ISPRINTED='Y'"
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
              double i =Double.parseDouble(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);


    }
     public void updateKotIsprinted(String id) throws BasicException {
          System.out.println("eneter id"+id);
          String isPrinted="Y";
        new StaticSentence(s, "UPDATE KOT SET ISPRINTED='"+isPrinted+"' WHERE TICKET = ?", SerializerWriteString.INSTANCE).exec(id);
    }

 public void insertKot(RetailTicketInfo m_oTicket, String isPrinted, String ticketId, int kotid, int a,String person) {

         for (RetailTicketLineInfo l : m_oTicket.getLines()) {
           Object[] values = new Object[] {UUID.randomUUID().toString(), ticketId, m_oTicket.getDate(),m_oTicket.getTicketId(),l.getProductID(), isPrinted, l.getMultiply(), kotid,m_oTicket.getPlaceId(),l.getInstruction(),person};
        Datas[] datas = new Datas[] {Datas.STRING, Datas.STRING, Datas.TIMESTAMP, Datas.INT,Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.INT, Datas.DOUBLE,Datas.STRING,Datas.STRING};
            try {
                new PreparedSentence(s, "INSERT INTO KOT (ID,TICKET,DATENEW,BILLNO, PRODUCTID,ISPRINTED, QTY, KOTID,TABLEID,INSTRUCTION,PERSON) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8,9,10})).exec(values);
            } catch (BasicException ex) {
                Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
    }
     public void insertPrintedKot(String ticketid, Date dateNew,int billNo, String productId, String isPrinted, double qty, int kotid,String tableId,String instruction,String person) {
        System.out.println("isPrinted 2"+isPrinted);
           Object[] values = new Object[] {UUID.randomUUID().toString(), ticketid,dateNew, billNo,productId, isPrinted, qty, kotid,tableId,instruction,person};
        Datas[] datas = new Datas[] {Datas.STRING, Datas.STRING, Datas.TIMESTAMP, Datas.INT,Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.INT, Datas.STRING,Datas.STRING,Datas.STRING};
            try {
             System.out.println("isPrinted 2 final"+isPrinted);   
                new PreparedSentence(s, "INSERT INTO KOT (ID,TICKET,DATENEW,BILLNO, PRODUCTID,ISPRINTED, QTY, KOTID, TABLEID,INSTRUCTION,PERSON) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8,9,10})).exec(values);
            } catch (BasicException ex) {
                Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
            }

    }
//public void insertCancelledKot(String ticketid, Date dateNew,int billNo, String productId, String isPrinted, double qty, int kotid, String isCancel, String reason,String reasonId,String tableId,String person) {
//
//           Object[] values = new Object[] {UUID.randomUUID().toString(), ticketid,dateNew, billNo, productId, isPrinted, qty, kotid,isCancel,reason,reasonId,tableId,person};
//        Datas[] datas = new Datas[] {Datas.STRING, Datas.STRING,Datas.TIMESTAMP, Datas.INT, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.INT,Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING};
//            try {
//                new PreparedSentence(s, "INSERT INTO KOT (ID,TICKET,DATENEW,BILLNO, PRODUCTID,ISPRINTED, QTY, KOTID,ISCANCELLED,REASON,REASONID,TABLEID,PERSON) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?,?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,12})).exec(values);
//            } catch (BasicException ex) {
//                Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//    }

public void insertCancelledKot(String ticketid, Date dateNew,int billNo, String productId, String isPrinted, double qty, int kotid, String isCancel, String reason,String reasonId,String tableId,String person, Date accountDate) {

           Object[] values = new Object[] {UUID.randomUUID().toString(), ticketid,dateNew, billNo, productId, isPrinted, qty, kotid,isCancel,reason,reasonId,tableId,person,accountDate};
        Datas[] datas = new Datas[] {Datas.STRING, Datas.STRING,Datas.TIMESTAMP, Datas.INT, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.INT,Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.TIMESTAMP};
            try {
                new PreparedSentence(s, "INSERT INTO KOT (ID,TICKET,DATENEW,BILLNO, PRODUCTID,ISPRINTED, QTY, KOTID,ISCANCELLED,REASON,REASONID,TABLEID,PERSON,ACCOUNTDATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?,?,?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,12,13})).exec(values);
            } catch (BasicException ex) {
                Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
            }

    }

     public final List<kotInfo> getKot(String ticketId) throws BasicException {

        return (List<kotInfo>) new StaticSentence(s
                , "SELECT k.ID,k.TICKET, k.PRODUCTID,k.ISPRINTED,k.QTY,k.KOTID,p.NAME,k.INSTRUCTION FROM KOT k, PRODUCTS p where p.ID=k.PRODUCTID and k.TICKET = '"+ticketId+"' and k.ISPRINTED='N' ORDER BY ID"
                , null
                , new SerializerReadClass(kotInfo.class)).list();
    }
     public final List<kotPrintedInfo> getisPrintedKot(String ticketId) throws BasicException {

        return (List<kotPrintedInfo>) new StaticSentence(s
                , "SELECT PRODUCTID,sum(QTY),ISCANCELLED FROM KOT where TICKET = '"+ticketId+"' and ISPRINTED='Y' GROUP BY PRODUCTID"
                , null
                , new SerializerReadClass(kotPrintedInfo.class)).list();
    }

     //Query: To Check if there is any record in KOT table for given bill No., i.e any item has been sent to KOT yet?
     public final String isKotEntered(String billNo) throws BasicException{
        Datas[] datatype = new Datas []{Datas.STRING};
        String sqlQuery = "SELECT COUNT(ID) FROM KOT WHERE BILLNO = ?";
        String isPrinted = null;
        try {
            //first create an array of Objects , consisting of all the results returned by StaticSentence
            //here only one object will be returned always which is count of records in KOT Table.
            Object[] record = (Object[]) new StaticSentence(s, sqlQuery, SerializerWriteString.INSTANCE, new SerializerReadBasic(datatype)).find(billNo);
            isPrinted = (String) record[0]; //typecast the first element of object array into String and pass reference to isPrinted variable.
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }

        return isPrinted == null? null : isPrinted; //return isPrinted as String , giving count of KOT records.
    }

    public final void deleteKot(final String id) throws BasicException {

        new StaticSentence(s
            , "DELETE FROM KOt WHERE TICKET = ?"
            , SerializerWriteString.INSTANCE).exec(id);
    }
    public void insertToken(int tokenId,String ticketid,Date dateNew,String USER_ID,int active) {
  System.out.println("m_oTicket.getTicketId() in query"+tokenId);
           Object[] values = new Object[] {tokenId,USER_ID,dateNew,active,ticketid};
        Datas[] datas = new Datas[] {Datas.INT,Datas.STRING,Datas.TIMESTAMP,Datas.INT,Datas.STRING};
            try {
                new PreparedSentence(s, "INSERT INTO tbl_token (TOKEN_ID,USER_ID,CREATEDDATE,MODIFIEDDATE,ACTIVE,TICKETID) VALUES (?, ?, ?, ?, ?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 2, 3, 4})).exec(values);
            } catch (BasicException ex) {
                Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
            }

    }
        public void insertTableOrder(int tokenId,String USER_ID,String tableId,double amount, double tax, double total,int preStatus, int active,String ticketid, Date orderDate) {

           Object[] values = new Object[] {tokenId,tableId,USER_ID,amount,tax,total,preStatus,active,ticketid,orderDate};
        Datas[] datas = new Datas[] {Datas.INT,Datas.STRING,Datas.STRING,Datas.DOUBLE,Datas.DOUBLE,Datas.DOUBLE,Datas.INT,Datas.INT,Datas.STRING, Datas.TIMESTAMP};
            try {
                new PreparedSentence(s, "INSERT INTO TBL_ORDER (TOKEN_ID,MST_TABLE_ID,USER_ID,AMOUNT,TAX,TOTAL,PREPARESTATUS,ACTIVE,TICKETID,ORDERTIME,DELIVERYTIME) VALUES (?, ?, ?, ?, ?, ?, ?,?,?, ?,?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4,5,6,7,8, 9, 9})).exec(values);
            } catch (BasicException ex) {
                Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
            }

    }
        
//        public void insertTableOrderSample(int tokenId,String USER_ID,String tableId,String amount, double tax, double total,int preStatus, int active,String ticketid, Date orderDate) {
//
//           Object[] values = new Object[] {tokenId,tableId,USER_ID,amount,tax,total,preStatus,active,ticketid,orderDate};
//        Datas[] datas = new Datas[] {Datas.INT,Datas.STRING,Datas.STRING,Datas.STRING,Datas.DOUBLE,Datas.DOUBLE,Datas.INT,Datas.INT,Datas.STRING, Datas.TIMESTAMP};
//            try {
//                new PreparedSentence(s, "INSERT INTO TBL_ORDER (TOKEN_ID,MST_TABLE_ID,USER_ID,AMOUNT,TAX,TOTAL,PREPARESTATUS,ACTIVE,TICKETID,ORDERTIME,DELIVERYTIME) VALUES (?, ?, ?, ?, ?, ?, ?,?,?, ?,?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4,5,6,7,8, 9, 9})).exec(values);
//            } catch (BasicException ex) {
//                Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//    }
        public void insertTableOrderLines(String tblOrderId,String tokenId,String productId, double qty,double price, int bLink,int preStatus,int active, Date orderDate) {

           Object[] values = new Object[] {tblOrderId,tokenId, productId,qty, price,bLink, preStatus, active,orderDate};
        Datas[] datas = new Datas[] {Datas.STRING,Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE,Datas.INT, Datas.INT, Datas.INT, Datas.TIMESTAMP};
            try {
                new PreparedSentence(s, "INSERT INTO TBL_ORDERITEM (ORDERITEM_ID,TOKEN_ID,ITEM_ID,QUANTITY,PRICE, BLINK ,PREPARESTATUS , ACTIVE,ORDERTIME) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5, 6, 7,8})).exec(values);
            } catch (BasicException ex) {
                Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
            }

    }

         public final void updateOrderItem(final String id, final String productId,String tbl_orderId) throws BasicException {
         System.out.println(tbl_orderId+"before updating tbl_order") ;  
        Object[] values = new Object[] {id, productId,tbl_orderId};
        Datas[] datas = new Datas[] {Datas.STRING, Datas.STRING,Datas.STRING};
        new PreparedSentence(s
                , "UPDATE TBL_ORDERITEM SET PREPARESTATUS = 3 WHERE TOKEN_ID = ? and ITEM_ID=? and ORDERITEM_ID=?"
                , new SerializerWriteBasicExt(datas, new int[] {0, 1,2})).exec(values);
    }
     public final void updateOrder(final String id) throws BasicException {

        Object[] values = new Object[] {id};
        Datas[] datas = new Datas[] {Datas.STRING};
        new PreparedSentence(s
                , "UPDATE TBL_ORDER SET PREPARESTATUS = 3 WHERE TICKETID = ? "
                , new SerializerWriteBasicExt(datas, new int[] {0})).exec(values);
    }
     //newly added
      public final void updateOrderBack(final String id) throws BasicException {

        Object[] values = new Object[] {id};
        Datas[] datas = new Datas[] {Datas.STRING};
        new PreparedSentence(s
                , "UPDATE TBL_ORDER SET PREPARESTATUS = 0 WHERE TICKETID = ? "
                , new SerializerWriteBasicExt(datas, new int[] {0})).exec(values);
    }
         public int getTokenId() throws BasicException {
        Object[] record = null;

      record = (Object[]) new StaticSentence(s
                    ,"SELECT MAX(TOKEN_ID) FROM tbl_token"
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            int i = Integer.parseInt(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);


    }
         public int getTokenBasedOnTicket(String ticketId) throws BasicException {
        Object[] record = null;

      record = (Object[]) new StaticSentence(s
                    ,"SELECT TOKEN_ID FROM tbl_token WHERE TICKETID='"+ticketId+"'"
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            int i = Integer.parseInt(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);


    }
          public int getTicketIdCount(String ticketId) throws BasicException {
        Object[] record = null;

      record = (Object[]) new StaticSentence(s
                    ,"SELECT COUNT(*) FROM tbl_token WHERE TICKETID='"+ticketId+"'"
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            int i = Integer.parseInt(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);


    }


public String getTokenId(String tableid) throws BasicException {
        if (tableid == null) {
            return null;
        } else {
            Object[] record = (Object[]) new StaticSentence(s, "SELECT TOKEN_ID FROM tbl_order WHERE MST_TABLE_ID=? AND (PAIDSTATUS=0 or PAIDSTATUS IS NULL)", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(tableid);
            return record == null ? "" : record[0].toString();
        }
    }
 public final void updateOrderSharedTicket(final String id, final RetailTicketInfo ticket) throws BasicException {

        Object[] values = new Object[] {id, ticket};
        Datas[] datas = new Datas[] {Datas.STRING, Datas.SERIALIZABLE};
        new PreparedSentence(s
                , "UPDATE SHAREDTICKETS SET CONTENT = ? WHERE ID = ?"
                , new SerializerWriteBasicExt(datas, new int[] {1, 0})).exec(values);
    }
public String getRoleByUser(String id) throws BasicException {

            Object[] record=null;

        record = (Object[]) new StaticSentence(s, "select name from roles where id=?"
        , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find(id);

            return record == null ? "" : (String) record[0];
        }
public String getFloorId(String tableid) throws BasicException {
        if (tableid == null) {
            return null;
        } else {
            Object[] record = (Object[]) new StaticSentence(s, "SELECT FLOOR FROM PLACES WHERE ID=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(tableid);
            return record == null ? "" : record[0].toString();
        }
    }
//public String getTakeAwayFloorId(String floor) throws BasicException {
//        if (floor == null) {
//            return null;
//        } else {
//            Object[] record = (Object[]) new StaticSentence(s, "SELECT ID FROM FLOORS WHERE NAME=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(floor);
//            return record == null ? "" : record[0].toString();
//        }
//    }

public List<BeanInfo> getTakeAwayInfoList() throws BasicException {

        return (List<BeanInfo>) new StaticSentence(s, "SELECT ID,FLOOR FROM PLACES WHERE NAME='takeaway' ", null, new SerializerReadClass(BeanInfo.class)).list();
    }
     
 public final List<kotInfo> getRetailKot(String ticketId,String productionArea) throws BasicException {

        return (List<kotInfo>) new StaticSentence(s
                , "SELECT k.ID,k.TICKET, k.PRODUCTID,k.ISPRINTED,k.QTY,k.KOTID,p.NAME,k.INSTRUCTION FROM KOT k, PRODUCTS p where p.ID=k.PRODUCTID and k.TICKET = '"+ticketId+"' and k.ISPRINTED='N' AND P.PRODUCTIONAREATYPE='"+productionArea+"' ORDER BY ID"
                , null
                , new SerializerReadClass(kotInfo.class)).list();
    }
// public final List<ProductionPrinterInfo> getPrinterInfo(String session) throws BasicException {
//
//        return (List<ProductionPrinterInfo>) new StaticSentence(s
//                , "SELECT DISTINCT SM.PRODUCTIONAREATYPE,SM.PRODUCTIONAREA,PC.PATH FROM SECTIONMAPPING SM, PRODUCTIONAREA PA, PRINTERCONFIG PC "+
//                "WHERE SM.PRODUCTIONAREATYPE=PA.PRODUCTIONAREATYPE AND PA.RESTUARANTPRINTER=PC.ID AND  SM.SECTION= '"+session+"'"
//                , null
//                , new SerializerReadClass(ProductionPrinterInfo.class)).list();
//    }
public final List<ProductionPrinterInfo> getPrinterInfo(String session) throws BasicException {

        return (List<ProductionPrinterInfo>) new StaticSentence(s
                , "SELECT DISTINCT SM.PRODUCTIONAREATYPE,SM.PRODUCTIONAREA,PC.PATH,PAT.NAME FROM SECTIONMAPPING SM, PRODUCTIONAREA PA, PRINTERCONFIG PC,PRODUCTIONAREATYPE PAT "+
                "WHERE SM.PRODUCTIONAREATYPE=PA.PRODUCTIONAREATYPE AND PA.RESTUARANTPRINTER=PC.ID AND PAT.ID=PA.PRODUCTIONAREATYPE AND  SM.SECTION= '"+session+"'"
                , null
                , new SerializerReadClass(ProductionPrinterInfo.class)).list();
    }
//    public final List<SharedSplitTicketInfo> getRetailSharedSplitTicket(String id) throws BasicException {
//        System.out.println("id in getRetailSharedSplitTicket"+id);
//       if (id == null) {
//            return null;
//        } else {
//            return (List<SharedSplitTicketInfo>) new StaticSentence(s
//                    , "SELECT ID,NAME,CONTENT FROM SHAREDTICKETS WHERE ID = '"+id+"'"
//                    , null
//                , new SerializerReadClass(SharedSplitTicketInfo.class)).list();
//        }
//    }
    
    public final List<SharedSplitTicketInfo> getRetailSharedSplitTicket(String id) throws BasicException {
        
        return (List<SharedSplitTicketInfo>) new StaticSentence(s
                , "SELECT ID, NAME,SPLITID FROM SHAREDTICKETS WHERE ID='"+id+"'"
                , null
                , new SerializerReadClass(SharedSplitTicketInfo.class)).list();
    }
     
     
    public final void updateSharedSplitTicket(final String id, final RetailTicketInfo ticket) throws BasicException {
      // System.out.println("updateSharedSplitTicket"+ticket.getSplitSharedId());      
        Object[] values = new Object[] {id, ticket.getName(), ticket,ticket.getSplitSharedId(), ticket.isPrinted(), ticket.isListModified()};
        Datas[] datas = new Datas[] {Datas.STRING, Datas.STRING, Datas.SERIALIZABLE,Datas.STRING, Datas.BOOLEAN, Datas.BOOLEAN};
        new PreparedSentence(s
                , "UPDATE SHAREDTICKETS SET NAME=?,CONTENT =? , ISPRINTED = ?, ISMODIFIED = ? WHERE ID = ? AND SPLITID=?"
                , new SerializerWriteBasicExt(datas, new int[] {1,2, 4, 5,0,3})).exec(values);
    }
    
    public final RetailTicketInfo getRetailSharedTicketSplit(String Id,String splitid) throws BasicException {

        if (Id == null) {
            return null;
        } else {
            Object[]record = (Object[]) new StaticSentence(s
                    , "SELECT CONTENT FROM SHAREDTICKETS WHERE ID = ? AND SPLITID='"+splitid+"'"
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.SERIALIZABLE})).find(Id);
            return record == null ? null : (RetailTicketInfo) record[0];
        }
    }
    
    
//   public String getRetailSharedTicketName(String id) throws BasicException {
//       if (id == null) {
//            return null;
//        } else { 
//      Object[] record = (Object[]) new StaticSentence(s, "SELECT NAME FROM SHAREDTICKETS WHERE ID=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(id);
//            return record == null ? "" : record[0].toString();
//      }
//    }
   
   public final SharedTicketNameInfo getRetailSharedTicket1(String Id) throws BasicException {

        if (Id == null) {
            return null;
        } else {
            Object[]record = (Object[]) new StaticSentence(s
                    , "SELECT ID,NAME,CONTENT FROM SHAREDTICKETS WHERE ID = ?"
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.SERIALIZABLE})).find(Id);
            return record == null ? null : (SharedTicketNameInfo) record[0];
        }
    }
   
   public final void deleteSharedSplitTicket(final String id,String splitId) throws BasicException {

        new StaticSentence(s
            , "DELETE FROM SHAREDTICKETS WHERE ID = ? AND SPLITID='"+splitId+"'"
            , SerializerWriteString.INSTANCE).exec(id);      
    }
   
//  public final void updateSplitOrderItem(final RetailTicketInfo ticket, final String productId,String tbl_orderId) throws BasicException {
//         System.out.println(tbl_orderId+"before updating tbl_order") ;  
//        Object[] values = new Object[] {ticket.getId(),ticket.getParentId(),productId,tbl_orderId};
//        Datas[] datas = new Datas[] {Datas.STRING,Datas.STRING, Datas.STRING,Datas.STRING};
//        new PreparedSentence(s
//                , "UPDATE TBL_ORDERITEM SET TOKEN_ID = ? WHERE TOKEN_ID = ? and ITEM_ID=? and ORDERITEM_ID=?"
//                , new SerializerWriteBasicExt(datas, new int[] {0,1,2,3})).exec(values);
//    }
  
   
   //this method to delete the line which is sent to kot
     public final void deleteFromTbl_orderItem(final String id) throws BasicException {

        new StaticSentence(s
            , "DELETE FROM TBL_ORDERITEM WHERE ORDERITEM_ID = ? "
            , SerializerWriteString.INSTANCE).exec(id);      
    }
     
   //this method to update the line in tbl_orderitem which is sent to kot
      public final void updateTbl_orderItem(final double qty,final String id) throws BasicException {

        Object[] values = new Object[] {id, qty};
        Datas[] datas = new Datas[] {Datas.STRING, Datas.DOUBLE};
        new PreparedSentence(s
                , "UPDATE TBL_ORDERITEM SET QUANTITY = ? WHERE ORDERITEM_ID = ?"
                , new SerializerWriteBasicExt(datas, new int[] {1, 0})).exec(values);
    }
     
     
     
     //below methods are called only in case of split bill
     
     public final void deleteTbl_orderItem(final String id) throws BasicException {

        new StaticSentence(s
            , "DELETE FROM TBL_ORDERITEM WHERE TOKEN_ID = ? "
            , SerializerWriteString.INSTANCE).exec(id);      
    }
     public final void deleteTbl_order(final String id) throws BasicException {

        new StaticSentence(s
            , "DELETE FROM TBL_ORDER WHERE TICKETID = ? "
            , SerializerWriteString.INSTANCE).exec(id);      
    }
     
    public final void deleteTokenTicketId(String ticketId) throws BasicException {
      new StaticSentence(s
            , "DELETE FROM TBL_TOKEN WHERE TICKETID = ? "
            , SerializerWriteString.INSTANCE).exec(ticketId);      

    }
    
     public final synchronized void saveToken(final RetailTicketInfo ticket,final int prepStatus) 
					throws BasicException {
         Transaction t = new Transaction(s) {       

          @Override
          protected Object transact() throws BasicException {
            new PreparedSentence(s
                    , "INSERT INTO tbl_token (TOKEN_ID,USER_ID,CREATEDDATE,MODIFIEDDATE,ACTIVE,TICKETID) VALUES (?, ?, ?, ?, ?, ?)"
                    , SerializerWriteParams.INSTANCE
                    ).exec(new DataParams() { public void writeValues() throws BasicException {
                      setInt(1, ticket.getTicketId());
                      setString(2, ticket.getUser().getId());
                      setTimestamp(3, ticket.getDate());
                      setTimestamp(4, ticket.getDate());
                      setInt(5, 1);
                      setString(6, String.valueOf(ticket.getOrderId()));
                      
                     }}); 
            new PreparedSentence(s
                    , "INSERT INTO TBL_ORDER (TOKEN_ID,MST_TABLE_ID,USER_ID,AMOUNT,TAX,TOTAL,PREPARESTATUS,ACTIVE,TICKETID,ORDERTIME,DELIVERYTIME) VALUES (?, ?, ?, ?, ?, ?, ?,?,?, ?,?)"
                    , SerializerWriteParams.INSTANCE
                    ).exec(new DataParams() { public void writeValues() throws BasicException {
                      setInt(1, ticket.getTicketId());
                      setString(2, ticket.getPlaceId());
                      setString(3, ticket.getUser().getId());
                      setDouble(4, 0.0);
                      setDouble(5, 0.0);
                      setDouble(6, 0.0);
                      setInt(7, prepStatus);
                      setInt(8, 1);
                      setString(9, String.valueOf(ticket.getOrderId()));
                      setTimestamp(10, ticket.getDate());
                      setTimestamp(11, ticket.getDate());
                      }}); 
            return null;                                       
        }
             
         };
      
        t.execute(); 
    }
     
    //select kot no from ticketlines table
     public String getkotNo() throws BasicException {

             Object[] record = (Object[]) new StaticSentence(s, "SELECT MAX(KOTID) FROM TICKETLINES ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{})).find();
            return record == null ? "" : (String) record[0];
        }
     
      public final Integer getNextKotIndex() throws BasicException {
        return (Integer) s.DB.getSequenceSentence(s, "KOTNUM").find();
    }
      
      public int getSharedTicketCount() throws BasicException {
        Object[] record = null;
      record = (Object[]) new StaticSentence(s
                    ,"SELECT COUNT(*) FROM SHAREDTICKETS"
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            int i = Integer.parseInt(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);
    }
      //Fetching categories to calculate product based discount
     public List<BeanInfo> getCategoriesList() throws BasicException {

        return (List<BeanInfo>) new StaticSentence(s, "SELECT ID,NAME FROM CATEGORIES WHERE PARENTID IS NULL AND ISDISCOUNTAPPLICABLE ='Y' ", null, new SerializerReadClass(BeanInfo.class)).list();
    }
     
     //newly added method to parent category Id
      public String getParentCategory(String catId) throws BasicException {

        Object[] record = (Object[]) new StaticSentence(s, "SELECT PARENTID FROM CATEGORIES WHERE ID = ?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(catId);
        return record == null ? null : (String) record[0];
    }

      public List<CardTypeInfo> getCardType() throws BasicException {

        return (List<CardTypeInfo>) new StaticSentence(s, "SELECT ID, CARDTYPE FROM CARDTYPEMASTERS ", null, new SerializerReadClass(CardTypeInfo.class)).list();
    }
       public List<CardTypeInfo> getOtherPaymentType() throws BasicException {

        return (List<CardTypeInfo>) new StaticSentence(s, "SELECT ID, PAYMENTTYPE FROM OTHERPAYMENTTYPE ", null, new SerializerReadClass(CardTypeInfo.class)).list();
    }
       public final void deleteTableCovers(final String id,String splitId) throws BasicException {

        new StaticSentence(s
            , "DELETE FROM TABLECOVERS WHERE TABLEID = ? AND SPLITID='"+splitId+"'"
            , SerializerWriteString.INSTANCE).exec(id);      
    }
}

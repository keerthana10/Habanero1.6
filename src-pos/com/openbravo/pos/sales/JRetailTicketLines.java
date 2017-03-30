//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2008-2009 Openbravo, S.L.
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

import com.lowagie.text.pdf.TextField;
import com.openbravo.data.loader.LocalRes;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.ticket.RetailTicketInfo;
import com.openbravo.pos.ticket.RetailTicketLineInfo;
import java.awt.Color;
import java.awt.Desktop.Action;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.EventObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class JRetailTicketLines extends javax.swing.JPanel {

    private static Logger logger = Logger.getLogger("com.openbravo.pos.sales.JRetailTicketLines");

    private static SAXParser m_sp = null;
   
    private TicketTableModel m_jTableModel;
    //JTextField text1=new JTextField(20);

    /** Creates new form JLinesTicket */
    public JRetailTicketLines(String ticketline) {
        
        initComponents();
 //   text1 = new JTextField();
        ColumnTicket[] acolumns = new ColumnTicket[0];
        
        if (ticketline != null) {
            try {
                if (m_sp == null) {
                    SAXParserFactory spf = SAXParserFactory.newInstance();
                    m_sp = spf.newSAXParser();
                }
                ColumnsHandler columnshandler = new ColumnsHandler();
                m_sp.parse(new InputSource(new StringReader(ticketline)), columnshandler);
                acolumns = columnshandler.getColumns();

            } catch (ParserConfigurationException ePC) {
                logger.log(Level.WARNING, LocalRes.getIntString("exception.parserconfig"), ePC);
            } catch (SAXException eSAX) {
                logger.log(Level.WARNING, LocalRes.getIntString("exception.xmlfile"), eSAX);
            } catch (IOException eIO) {
                logger.log(Level.WARNING, LocalRes.getIntString("exception.iofile"), eIO);
            }
        }
               
        m_jTableModel = new TicketTableModel(acolumns);    
        m_jTicketTable.setModel(m_jTableModel);        

        //m_jTicketTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        final TableColumnModel jColumns = m_jTicketTable.getColumnModel();
        for (int i = 0; i < acolumns.length; i++) {
            if(i==4){
                jColumns.getColumn(i).setMinWidth(-1);
                jColumns.getColumn(i).setMaxWidth(-1);
                jColumns.getColumn(i).setWidth(0);

            }else{
             jColumns.getColumn(i).setPreferredWidth(acolumns[i].width);
             jColumns.getColumn(i).setResizable(false);
            }
  
//            if(i==2){
//              int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
//              InputMap inputMap = m_jTicketTable.getInputMap(condition);
//              ActionMap actionMap = m_jTicketTable.getActionMap();
//
//              // DELETE is a String constant that for me was defined as "Delete"
//              inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_1, 0), "Units");
//              actionMap.put("Units", new AbstractAction() {
//                 public void actionPerformed(ActionEvent e) {
//                     System.out.println("entr---1 ");// TODO: do deletion action here
//                 }
//              });
//            }
//
        }       
        
        m_jScrollTableTicket.getVerticalScrollBar().setPreferredSize(new Dimension(30, 30));
         m_jScrollTableTicket.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        m_jTicketTable.getTableHeader().setReorderingAllowed(false);
        m_jScrollTableTicket.setBorder(null);
          m_jScrollTableTicket.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        m_jTicketTable.setDefaultRenderer(Object.class, new TicketCellRenderer(acolumns));
        m_jTicketTable.setRowHeight(25);
        m_jTicketTable.setBorder(null);
        m_jTicketTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

       AbstractAction doSelectItemGrid = new AbstractAction() {

       public void actionPerformed(ActionEvent e) {
            setSelectedIndex(-1);
        //    m_jPrice.setFocusable(true);
            }
        };

        // reseteo la tabla...
        m_jTableModel.clear();
    }
    
    public void addListSelectionListener(ListSelectionListener l) {        
        m_jTicketTable.getSelectionModel().addListSelectionListener(l);
    }
    public void removeListSelectionListener(ListSelectionListener l) {
        m_jTicketTable.getSelectionModel().removeListSelectionListener(l);
    }
    
    public void clearTicketLines() {                   
        m_jTableModel.clear();
    }
    
    public void setTicketLine(int index, RetailTicketLineInfo oLine){
        
        m_jTableModel.setRow(index, oLine);  
    }
    
    public void addTicketLine(RetailTicketLineInfo oLine) {
   
        m_jTableModel.addRow(oLine);
//        System.out.println("oLine.getPreparationStatus();---"+oLine.getPreparationStatus());
        // Selecciono la que acabamos de anadir.            
        setSelectedIndex(m_jTableModel.getRowCount() - 1);

    }    
   
    public void insertTicketLine(int index, RetailTicketLineInfo oLine) {
        m_jTableModel.insertRow(index, oLine); 
        // Selecciono la que acabamos de anadir.            
        setSelectedIndex(index);   
    }     
    public void removeTicketLine(int i){

        m_jTableModel.removeRow(i);

        // Escojo una a seleccionar
        if (i >= m_jTableModel.getRowCount()) {
            i = m_jTableModel.getRowCount() - 1;
        }

        if ((i >= 0) && (i < m_jTableModel.getRowCount())) {
            // Solo seleccionamos si podemos.
            setSelectedIndex(i);
        }
    }
    
    public void setSelectedIndex(int i){
        
        // Seleccionamos
        m_jTicketTable.getSelectionModel().setSelectionInterval(i, i);

        // Hacemos visible la seleccion.
        Rectangle oRect = m_jTicketTable.getCellRect(i, 0, true);
        m_jTicketTable.scrollRectToVisible(oRect);
    }
    
    public int getSelectedIndex() {
        return m_jTicketTable.getSelectionModel().getMinSelectionIndex(); // solo sera uno, luego no importa...
    }
    
    public void selectionDown() {
        
        int i = m_jTicketTable.getSelectionModel().getMaxSelectionIndex();
        if (i < 0){
            i =  0; // No hay ninguna seleccionada
        } else {
            i ++;
            if (i >= m_jTableModel.getRowCount()) {
                i = m_jTableModel.getRowCount() - 1;
            }
        }

        if ((i >= 0) && (i < m_jTableModel.getRowCount())) {
            // Solo seleccionamos si podemos.
     
            setSelectedIndex(i);
        }
    }
    
    public void selectionUp() {
        
        int i = m_jTicketTable.getSelectionModel().getMinSelectionIndex();
        if (i < 0){
            i = m_jTableModel.getRowCount() - 1; // No hay ninguna seleccionada
        } else {
            i --;
            if (i < 0) {
                i = 0;
            }
        }

        if ((i >= 0) && (i < m_jTableModel.getRowCount())) {
            // Solo seleccionamos si podemos.
            setSelectedIndex(i);
        }
    }
    
    private static class TicketCellRenderer extends DefaultTableCellRenderer {
        
        private ColumnTicket[] m_acolumns;        
        
        public TicketCellRenderer(ColumnTicket[] acolumns) {
            m_acolumns = acolumns;
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
            
            JLabel aux = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            aux.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            
             int background = Integer.parseInt(table.getModel().getValueAt(row, 4).toString());
             switch(background){
              case 0:
                  aux.setBackground(Color.white);
                  break;
              case 1:
                  aux.setBackground(Color.yellow);
                  break;
              case 2:
                  aux.setBackground(Color.pink);
                  break;
              case 3:
                  aux.setBackground(Color.green);
                  break;
              default:
                  aux.setBackground(Color.red);
                  break;
          }

          table.setCellSelectionEnabled(true);
          if (table.isRowSelected(row)){
              switch(background){
              case 0:
                  setBackground(new Color(225, 225, 225));
                  break;
              case 1:
                  setBackground(new Color(255, 249, 125));
                  break;
              case 2:
                  setBackground(new Color(255, 198, 255));
                  break;
              case 3:
                  setBackground(new Color(130, 255, 130));
                  break;
              default:
                  setBackground(new Color(255, 130, 130));
                  break;
          }
        }
            aux.setHorizontalAlignment(m_acolumns[column].align);           
            return aux;
        }
    }
    
    private static class TicketTableModel extends AbstractTableModel {
        
//        private AppView m_App;
        private ColumnTicket[] m_acolumns;
        private ArrayList m_rows = new ArrayList();
        
        public TicketTableModel(ColumnTicket[] acolumns) {
            m_acolumns = acolumns;
        }
        public int getRowCount() {
            return m_rows.size();
        }
        public int getColumnCount() {
            return m_acolumns.length;
        }
        @Override
        public String getColumnName(int column) {
            return AppLocal.getIntString(m_acolumns[column].name);
            // return m_acolumns[column].name;
        }
        public Object getValueAt(int row, int column) {
            return ((String[]) m_rows.get(row))[column];
        }
  
        @Override
//        public boolean isCellEditable(int row, int column) {
//             if (column == 2 || column == 3) {
//                return true;
//
//              } else {
//                return false;
//              }
//        }
//public boolean editCellAt(int row, int column, EventObject e){
//            if(e instanceof KeyEvent){
//                if(column==2){
//                int i = ((KeyEvent) e).getModifiers();
//                String s = KeyEvent.getModifiersExText(((KeyEvent) e).getModifiers());
//                //any time Control is used, disable cell editing
//                if(i == InputEvent.CTRL_MASK){
//                    return true;
//                }}
//            }
//            return editCellAt(row, column, e);
//        }
    public void setValueAt(Object value, int row, int column) {
//      JRetailTicketLines i = (JRetailTicketLines) m_rows.get(row);
        RetailTicketLineInfo oLine = new RetailTicketLineInfo();
        if (column == 2) {
        try {

        oLine.setMultiply(Double.parseDouble("" + value));

        setRow(row, oLine);
      //    SentenceExec sent = m_dlSales.updateBuyPrice();
          // String[] value = m_jprice.getText().split("Rs.");
          // String data = value[1];

       //   sent.exec(i.getProductID(), "" + value);

        } catch (Exception e) {

        }
      }



      fireTableCellUpdated(row, column);

    }
        public void clear() {
            int old = getRowCount();
            if (old > 0) { 
                m_rows.clear();
                fireTableRowsDeleted(0, old - 1);
            }
        }
      
        public void setRow(int index, RetailTicketLineInfo oLine){
            
            String[] row = (String []) m_rows.get(index);

            for (int i = 0; i < m_acolumns.length; i++) {
                try {
                    ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                    script.put("ticketline", oLine);
                    row[i] = script.eval(m_acolumns[i].value).toString();
                } catch (ScriptException e) {
                    row[i] = null;
                } 
                fireTableCellUpdated(index, i);
            }             
        }        
        
        public void addRow(RetailTicketLineInfo oLine) {
            insertRow(m_rows.size(), oLine);
        }
        
        public void insertRow(int index, RetailTicketLineInfo oLine) {
            System.out.println("insert row----");
            String[] row = new String[m_acolumns.length];
            for (int i = 0; i < m_acolumns.length; i++) {
                try {
                    ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                    script.put("ticketline", oLine);
                    row[i] = script.eval(m_acolumns[i].value).toString();

                } catch (ScriptException e) {
                    row[i] = null;
                }  
            } 
     
            m_rows.add(index, row);
      
            fireTableRowsInserted(index, index);
        }
        
        public void removeRow(int row) {
            m_rows.remove(row);
            fireTableRowsDeleted(row, row);
        }        
    }
    
    private static class ColumnsHandler extends DefaultHandler {
        
        private ArrayList m_columns = null;
        
        public ColumnTicket[] getColumns() {
            return (ColumnTicket[]) m_columns.toArray(new ColumnTicket[m_columns.size()]);
        }
        @Override
        public void startDocument() throws SAXException { 
            m_columns = new ArrayList();
        }
        @Override
        public void endDocument() throws SAXException {}    
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
     
               if ("column".equals(qName)){
                ColumnTicket c = new ColumnTicket();
                c.name = attributes.getValue("name");
                c.width = Integer.parseInt(attributes.getValue("width"));
                String sAlign = attributes.getValue("align");
                if ("right".equals(sAlign)) {
                    c.align = javax.swing.SwingConstants.RIGHT;
                } else if ("center".equals(sAlign)) {
                    c.align = javax.swing.SwingConstants.CENTER;
                } else {
                    c.align = javax.swing.SwingConstants.LEFT;
                }
                c.value = attributes.getValue("value");
                   System.out.println("c.value-----"+ c.value+"---"+ c.name);
                  
                  m_columns.add(c);


            }
        }      
        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {}
        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {}
    }
    
    private static class ColumnTicket {
        public String name;
        public int width;
        public int align;
        public String value;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        m_jScrollTableTicket = new javax.swing.JScrollPane();
        m_jTicketTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();

        setPreferredSize(new java.awt.Dimension(300, 402));
        setLayout(new java.awt.BorderLayout());

        m_jScrollTableTicket.setBorder(null);
        m_jScrollTableTicket.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        m_jScrollTableTicket.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        m_jScrollTableTicket.setPreferredSize(new java.awt.Dimension(300, 402));

        m_jTicketTable.setFocusable(false);
        m_jTicketTable.setIntercellSpacing(new java.awt.Dimension(0, 1));
        m_jTicketTable.setRequestFocusEnabled(false);
        m_jTicketTable.setShowVerticalLines(false);
        m_jScrollTableTicket.setViewportView(m_jTicketTable);

        add(m_jScrollTableTicket, java.awt.BorderLayout.CENTER);

        jPanel1.setBackground(new java.awt.Color(222, 232, 231));
        jPanel1.setPreferredSize(new java.awt.Dimension(8, 2));
        add(jPanel1, java.awt.BorderLayout.LINE_START);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane m_jScrollTableTicket;
    private javax.swing.JTable m_jTicketTable;
    // End of variables declaration//GEN-END:variables
    
}

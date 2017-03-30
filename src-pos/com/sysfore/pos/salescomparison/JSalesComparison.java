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

package com.sysfore.pos.salescomparison;

import com.lowagie.text.pdf.codec.Base64.InputStream;
import com.openbravo.pos.forms.JPanelView;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.printer.DeviceTicket;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.util.JRPrinterAWT300;
import com.openbravo.pos.util.ReportUtils;
import com.sysfore.pos.stockreconciliation.DataLogicStockReceipts;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.print.PrintService;
import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;


/**
 *
 * @author adrianromero
 */
public class JSalesComparison extends JPanel implements JPanelView,BeanFactoryApp{
    private AppView m_App;
    private DataLogicSystem m_dlSystem;
    private TicketParser m_TTP;
    protected DataLogicSystem dlSystem;
     public String[] strings = {""};
     private DataLogicStockReceipts m_dlStock;
     private String[] stockDetails;
    private java.util.List<SalesComparisonInfo> salesList = null;
    private DeviceTicket m_TP;
    private static SalesTableModel m_Saleslines;
    private int stockSize = 0;
    private int rows;
    int setFlag=0;
    public TicketInfo ticketValue = null;
    public JSalesComparison() {
        
        initComponents();
  

    }
    
    public void init(AppView app) throws BeanFactoryException{
        
        m_App = app;        

        m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
        m_dlStock = (DataLogicStockReceipts)m_App.getBean("com.sysfore.pos.stockreconciliation.DataLogicStockReceipts");
        m_TTP = new TicketParser(m_App.getDeviceTicket(), m_dlSystem);
         m_TP = new DeviceTicket();
        
       //  m_panelticketedit = new JPanelTicketEdits(this);
        initComponents();

        setDefaultColumn();

        m_jSalesComparisonTable.getTableHeader().setReorderingAllowed(false);

        m_jSalesComparisonTable.setRowHeight(40);
        m_jSalesComparisonTable.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        m_jSalesComparisonTable.setIntercellSpacing(new java.awt.Dimension(0, 1));

        setVisible(true);
        m_Saleslines = new SalesTableModel();
        m_jSalesComparisonTable.setModel(m_Saleslines);
//        populateDropDown();
    

      
    }
 public void setDefaultColumn(){
      String sourceMonth = m_jSourceMonth.getSelectedItem().toString();
        String sourceYear = m_jSourceYear.getSelectedItem().toString();
         String destinationMonth = m_jDestinationMonth.getSelectedItem().toString();
         String destinationYear = m_jDestinationYear.getSelectedItem().toString();

         DefaultTableColumnModel columns = new DefaultTableColumnModel();
        TableColumn c;

        c = new TableColumn(0, 155, new DataCellRenderer(javax.swing.SwingConstants.RIGHT), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue( "Sales "+sourceMonth+"-"+sourceYear+"");

        columns.addColumn(c);
        c = new TableColumn(1, 155, new DataCellRenderer(javax.swing.SwingConstants.RIGHT), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue( "Sales "+destinationMonth+"-"+destinationYear+"");
        columns.addColumn(c);
        c = new TableColumn(2, 184, new DataCellRenderer(javax.swing.SwingConstants.RIGHT), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("Comparison(+/-)");
        columns.addColumn(c);
        c = new TableColumn(3, 183, new DataCellRenderer(javax.swing.SwingConstants.RIGHT), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("Percentage(+/-)");
        columns.addColumn(c);

//         c = new TableColumn(6, 58, new DataCellRenderer(javax.swing.SwingConstants.CENTER), new DefaultCellEditor(new JCheckBox()));
//        c.setHeaderValue("Select");
//        columns.addColumn(c);
        m_jSalesComparisonTable.setColumnModel(columns);
 }
   
    public void setSelectedIndex(int i) {

        // Seleccionamos
        m_jSalesComparisonTable.getSelectionModel().setSelectionInterval(i, i);

        // Hacemos visible la seleccion.
        Rectangle oRect = m_jSalesComparisonTable.getCellRect(i, 0, true);
        m_jSalesComparisonTable.scrollRectToVisible(oRect);
    }

    private void setCellRenderer(JTable table) {
//        table.getColumn("Select").setCellRenderer(new CustomCheckBoxEditor(table));
    }

    /**
     * @return the multiplychanged
     */
    public boolean isMultiplychanged() {
        return false;
    }




     private static class SalesTableModel extends AbstractTableModel {

        private ArrayList<SalesComparisonInfo> m_rows = new ArrayList<SalesComparisonInfo>();

        public int getRowCount() {
            return m_rows.size();
        }

        public int getColumnCount() {
            return 4;
        }

        public String getColumnName(int column) {
           switch (column)
    {
    case 0: return "Sales" ;
    case 1: return "Sales" ;
    case 2: return "Comparison" ;
    case 3: return "Percentage" ;
  
  //  case 6: return "Select" ;
    //case 2: return "Item" ;
    // ...
    }
     return null;

        }

        public Object getValueAt(int row, int column) {

            SalesComparisonInfo i = m_rows.get(row);
            switch (column) {
                case 0:
                    return  (i.getSourceAmt() );
                case 1:
                    return (i.getDestinationAmt());
                case 2:
                    return (i.getComparison());
                case 3:
                     return (i.getPercentage());
               
//                case 6:
//                    return Boolean.class;
                default:
                    return null;
            }

        }

        @Override
        public void setValueAt(Object o, int row, int column) {
            SalesComparisonInfo i = m_rows.get(row);
            if (column == 2) {


            }
        }

        @Override
        public boolean isCellEditable(int row, int column) {
           
         if (column == 2) {
           return true;
        } else {
            return false;
       }
        }

        public void clear() {
            int old = getRowCount();
            if (old > 0) {
                m_rows.clear();
                fireTableRowsDeleted(0, old - 1);
            }
        }

        public List<SalesComparisonInfo> getLines() {
            return m_rows;
        }

        public SalesComparisonInfo getRow(int index) {
            return m_rows.get(index);
        }

        public void setRow(int index, SalesComparisonInfo oLine) {

            m_rows.set(index, oLine);
            fireTableRowsUpdated(index, index);
        }

        public void addRow(SalesComparisonInfo oLine) {

            insertRow(m_rows.size(), oLine);
        }

        public void insertRow(int index, SalesComparisonInfo oLine) {

            m_rows.add(index, oLine);
            fireTableRowsInserted(index, index);
        }

        public void removeRow(int row) {
            m_rows.remove(row);
            fireTableRowsDeleted(row, row);
        }

        private void showMessage(String msg) {
            JOptionPane.showMessageDialog(null, msg);
        }
   }

  
 
 private static class DataCellRenderer extends DefaultTableCellRenderer {

        private int m_iAlignment;

        public DataCellRenderer(int align) {
            m_iAlignment = align;
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            JLabel aux = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            aux.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
           // aux.setVerti
            aux.setHorizontalAlignment(m_iAlignment);
            if (!isSelected) {
                aux.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.disabledBackground"));
            }
            return aux;
        }
    }


      private void initStockData() {


        stockDetails = new String[]{
            "Sales", "Sales", "Comparison", "Percentage"
        };
//         stockList = m_dlStock.getStockInfo(query);
        // stockSize = stockList.size();
         setStockTableModelAndHeader(m_jSalesComparisonTable, stockSize);
         setStockTableData(m_jSalesComparisonTable);
         setRows(stockSize);

    }

     private void setStockTableModelAndHeader(JTable table, int size) {
        table.getTableHeader().setPreferredSize(new Dimension(60, 25));
       // table.get
        table.setModel(new DefaultTableModel(stockDetails, size));
    }
     private void setStockTableData(JTable table) {

//        for (int col = 0; col < stockSize; col++) {
//
//            table.setValueAt(col+1, col, 0);
//       }
        for (int col = 0; col < stockSize; col++) {
            table.setValueAt(Formats.DoubleValue.formatValue(salesList.get(col).getSourceAmt()), col, 0);
        }
        for (int col = 0; col < stockSize; col++) {
            table.setValueAt(Formats.DoubleValue.formatValue(salesList.get(col).getDestinationAmt()), col, 1);
        }
         for (int col = 0; col < stockSize; col++) {
            table.setValueAt(Formats.DoubleValue.formatValue(salesList.get(col).getComparison()), col, 2);
        }
         for (int col = 0; col < stockSize; col++) {
            table.setValueAt(Formats.DoubleValue.formatValue(salesList.get(col).getPercentage()), col, 3);
        }
        
    }
   

 
    public Object getBean() {
         return this;
    }

    public JComponent getComponent() {
        return this;
    }

    public String getTitle() {
        return AppLocal.getIntString("Menu.SalesComparison");
    }

    public void activate() throws BasicException {
    
       stockDetails = new String[]{
           "Sales", "Sales", "Comparison", "Percentage"
         };
         setStockTableModelAndHeader(m_jSalesComparisonTable, 0);
         setRows(stockSize);
       //  m_jSourceYear.setSelectedItem("Cash");
  
    }

    public boolean deactivate() {
        // se me debe permitir cancelar el deactivate
        return true;
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        m_jSalesComparison = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        m_jSalesComparisonTable = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        m_jBtnSearch = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        m_jSourceYear = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        m_jDestinationYear = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        m_jDestinationMonth = new javax.swing.JComboBox();
        m_jSourceMonth = new javax.swing.JComboBox();
        m_jBtnPrint = new javax.swing.JButton();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        m_jSalesComparison.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        m_jSalesComparison.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        m_jSalesComparisonTable.setAutoCreateColumnsFromModel(false);
        m_jSalesComparisonTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        m_jSalesComparisonTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        m_jSalesComparisonTable.setCellSelectionEnabled(true);
        m_jSalesComparisonTable.setEditingColumn(2);
        m_jSalesComparisonTable.setEnabled(false);
        m_jSalesComparisonTable.setRowHeight(25);
        m_jSalesComparisonTable.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                m_jSalesComparisonTablePropertyChange(evt);
            }
        });
        jScrollPane1.setViewportView(m_jSalesComparisonTable);
        m_jSalesComparisonTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        m_jSalesComparison.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 94, 680, 310));

        jLabel3.setText("Source Month");
        m_jSalesComparison.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 13, 80, 20));

        m_jBtnSearch.setText("Execute Report");
        m_jBtnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnSearchActionPerformed(evt);
            }
        });
        m_jSalesComparison.add(m_jBtnSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 30, -1, -1));

        jLabel1.setText("Destination Year");
        m_jSalesComparison.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 50, 100, 20));

        m_jSourceYear.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024" }));
        m_jSalesComparison.add(m_jSourceYear, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 10, 80, -1));

        jLabel2.setText("Source Year");
        m_jSalesComparison.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 10, 80, 20));

        m_jDestinationYear.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024" }));
        m_jSalesComparison.add(m_jDestinationYear, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 50, 80, -1));

        jLabel4.setText("Destination Month");
        m_jSalesComparison.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 110, 20));

        m_jDestinationMonth.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December", " " }));
        m_jSalesComparison.add(m_jDestinationMonth, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 50, 80, -1));

        m_jSourceMonth.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December", " " }));
        m_jSalesComparison.add(m_jSourceMonth, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 80, -1));

        m_jBtnPrint.setText("Print");
        m_jBtnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnPrintActionPerformed(evt);
            }
        });
        m_jSalesComparison.add(m_jBtnPrint, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 30, -1, -1));

        add(m_jSalesComparison, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 32, 700, 450));
    }// </editor-fold>//GEN-END:initComponents

    private void m_jSalesComparisonTablePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_m_jSalesComparisonTablePropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jSalesComparisonTablePropertyChange
private int getRows() {
        return rows;
    }

    private void setRows(int rows) {
        this.rows = rows;
    }
    private void m_jBtnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnSearchActionPerformed
     Date sysDate = new Date();
       SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
       String currentDate = sdf.format(sysDate);
       String sourceMonth = m_jSourceMonth.getSelectedItem().toString();
       String sourceYear = m_jSourceYear.getSelectedItem().toString();
       String destinationMonth = m_jDestinationMonth.getSelectedItem().toString();
       String destinationYear = m_jDestinationYear.getSelectedItem().toString();

     setDefaultColumn();
       String query;

       stockDetails = new String[]{
           "Sales "+sourceMonth+"-"+sourceYear+"", "Sales"+destinationMonth+"-"+destinationYear+"", "Comparison(+/-)", "Percentage(+/-)"
        };
      
         salesList =  m_dlStock.getSalesComparison(sourceMonth,sourceYear,destinationMonth,destinationYear);
         stockSize = salesList.size();


         setStockTableModelAndHeader(m_jSalesComparisonTable, stockSize);
         setStockTableData(m_jSalesComparisonTable);
         setRows(stockSize);

}//GEN-LAST:event_m_jBtnSearchActionPerformed

    private void m_jBtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnPrintActionPerformed
//String document =  txtDocumentNo.getText();
         String sourceMonth = m_jSourceMonth.getSelectedItem().toString()+"-"+m_jSourceYear.getSelectedItem().toString();

         String destinationMonth = m_jDestinationMonth.getSelectedItem().toString()+"-"+m_jDestinationYear.getSelectedItem().toString();

         String resourcefile="/com/sysfore/SalesComparison";

         try {

            JasperReport jr;

            InputStream in = (InputStream) getClass().getResourceAsStream(resourcefile + ".ser");
            if (in == null) {
                // read and compile the report
                JasperDesign jd = JRXmlLoader.load(getClass().getResourceAsStream(resourcefile + ".jrxml"));
                jr = JasperCompileManager.compileReport(jd);
            } else {
                // read the compiled reporte
                ObjectInputStream oin = new ObjectInputStream(in);
                jr = (JasperReport) oin.readObject();
                oin.close();
            }

            // Construyo el mapa de los parametros.
            Map reportparams = new HashMap();
         //    reportparams.put("ARG", params);
            try {
                reportparams.put("REPORT_RESOURCE_BUNDLE", ResourceBundle.getBundle(resourcefile + ".properties"));
            } catch (MissingResourceException e) {
            }
          //  reportparams.put("document", document);
//System.out.println("enrtr---"+PurchaseOrderInfo.get(0).printAddress()+"--"+PurchaseOrderInfo.get(0).getAddress());
            Map reportfields = new HashMap();
            reportfields.put("Sales", salesList.get(0));
            reportfields.put("SourceMonth", sourceMonth);
            reportfields.put("DestinationMonth", destinationMonth);
   
            JasperPrint jp = JasperFillManager.fillReport(jr, reportparams, new JRMapArrayDataSource(new Object[] { reportfields } ));

            PrintService service = ReportUtils.getPrintService(m_App.getProperties().getProperty("machine.printer.2"));

            JRPrinterAWT300.printPages(jp, 0, jp.getPages().size() - 1, service);

        } catch (Exception e) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotloadreport"), e);
            msg.show(this);
        }
            // TODO add your handling code here:
    }//GEN-LAST:event_m_jBtnPrintActionPerformed
  
 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton m_jBtnPrint;
    private javax.swing.JButton m_jBtnSearch;
    private javax.swing.JComboBox m_jDestinationMonth;
    private javax.swing.JComboBox m_jDestinationYear;
    private javax.swing.JPanel m_jSalesComparison;
    private javax.swing.JTable m_jSalesComparisonTable;
    private javax.swing.JComboBox m_jSourceMonth;
    private javax.swing.JComboBox m_jSourceYear;
    // End of variables declaration//GEN-END:variables
    
}

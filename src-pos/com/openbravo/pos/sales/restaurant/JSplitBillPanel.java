/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.sales.restaurant;

import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.sales.DataLogicReceipts;
import com.openbravo.pos.sales.JPaymentEditor;
import com.openbravo.pos.sales.JRetailPanelTicket;
import com.openbravo.pos.sales.RetailTicketsEditor;
import com.openbravo.pos.sales.SharedSplitTicketInfo;
import com.openbravo.pos.ticket.RetailTicketInfo;
import com.openbravo.pos.ticket.RetailTicketLineInfo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Properties;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author shilpa
 */
public class JSplitBillPanel extends JDialog implements ListSelectionListener {
  
    private static boolean completed=false;
     public javax.swing.JDialog dEdior = null;
    private Properties dbp = new Properties();
    private DataLogicReceipts dlReceipts = null;
    private AppView m_app;
    public String[] strings = {""};
    public DefaultListModel model = null;
    public boolean updateMode = false;
    static Component parentLocal = null;
    static List<SharedSplitTicketInfo> tinfoLocal = null;
    static String splitId = null;
    public static String userRole = null;
    private static DataLogicReceipts localDlReceipts = null;
    private static Place c_place=null;
    public static JRetailTicketsBagRestaurantMap JRetailPanelTicket;
    private boolean enablity;
    int x = 350;
    int y = 200;
    int width = 650;
    int height = 400;
    public static String tinfotype;
    private String[]  columnCInNames = new String[]{
    "Split No.","Table No.","Date"
        };
    
        public static String showMessage(Component parent,DataLogicReceipts dlReceipts,List<SharedSplitTicketInfo> tinfo,Place place,RetailTicketsEditor m_panelticket){
        localDlReceipts = dlReceipts;
        parentLocal = parent;
        tinfoLocal = tinfo;
        c_place=place;
        String idvalue=tinfo.get(0).getId();
        String splitId =showMessage(parent,c_place, dlReceipts, 1,tinfo);
        return splitId;
        
    }
    

    
    private static String showMessage(Component parent,Place place, DataLogicReceipts dlReceipts, int x,List<SharedSplitTicketInfo> tinfo) {

        Window window = getWindow(parent);
        JSplitBillPanel myMsg;
        if (window instanceof Frame) {
            myMsg = new JSplitBillPanel((Frame) window, true);
        } else {
            myMsg = new JSplitBillPanel((Dialog) window, true);
        }
//        System.out.println("testing window 2"+tinfoLocal.size()+tinfoLocal.get(0).getContent().getName());
        String splitId=myMsg.init(place,dlReceipts,tinfo);
         return splitId;
    }
    
   private JSplitBillPanel(Frame frame, boolean b) {
       super(frame, true);
       setBounds(x, y, width, height);
    }
    private JSplitBillPanel(Dialog dialog, boolean b) {
        super(dialog, true);
        setBounds(x, y, width, height);

    }
   public String init(Place place,DataLogicReceipts dlReceipts,List<SharedSplitTicketInfo> tinfo) {
        initComponents();
        setTitle("Split Bill Selector");
        jSplitTable1.getTableHeader().setBackground(Color.ORANGE);
       setDataTableModelAndHeader(jSplitTable1,tinfo.size());
        setSplitPrimaryData(jSplitTable1, place,tinfo);
        setVisible(true);
        return splitId;
    }
    
    private void setDataTableModelAndHeader(JTable table, int size) {
        
        table.getTableHeader().setPreferredSize(new Dimension(20, 25));
        table.setModel(new DefaultTableModel(columnCInNames, size){
            public boolean isCellEditable(int row, int column) {
              return false;
            }
        });
        
       }
       private void setSplitPrimaryData(JTable jTable3,Place place, List<SharedSplitTicketInfo> sharedticket) {
      // System.out.println("testing window 4"+tinfoLocal.size()+tinfoLocal.get(0).getContent().getName());
       int ticketlinesSize=sharedticket.size();  
           for (int col = 0; col <  ticketlinesSize; col++) {
            jTable3.setRowHeight(col, 30);   
            jTable3.setValueAt( col+1, col, 0);
        }
           for (int col = 0; col <  ticketlinesSize; col++) {
              String placeName=place.getName();
              jTable3.setRowHeight(col, 30);         
            jTable3.setValueAt( placeName, col, 1);
        }
          for (int col = 0; col <  ticketlinesSize; col++) {
                jTable3.setRowHeight(col, 30);     
            jTable3.setValueAt( sharedticket.get(col).getName(), col, 2);
        } 
         jTable3.getSelectionModel().setSelectionInterval( 0, 0 );
       }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jSplitTable1 = new javax.swing.JTable();

        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                WindowClosingActionPerformed(evt);
            }
        });

        jPanel1.setLayout(new java.awt.BorderLayout());

        jSplitTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Split No.", "Table No.", "Bill No."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jSplitTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SelectSplitBillActionPerformed(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                MousePressedPerformed(evt);
            }
        });
        jScrollPane1.setViewportView(jSplitTable1);
        jSplitTable1.getColumnModel().getColumn(0).setResizable(false);
        jSplitTable1.getColumnModel().getColumn(0).setPreferredWidth(10);
        jSplitTable1.getColumnModel().getColumn(1).setResizable(false);
        jSplitTable1.getColumnModel().getColumn(1).setPreferredWidth(30);
        jSplitTable1.getColumnModel().getColumn(2).setResizable(false);
        jSplitTable1.getColumnModel().getColumn(2).setPreferredWidth(30);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    
    private void SelectSplitBillActionPerformed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SelectSplitBillActionPerformed
//        jSplitTable1.addMouseListener(new MouseAdapter() {
//          public void mouseClicked(MouseEvent e) {
//             JTable target = (JTable)e.getSource();
//              int row = target.getSelectedRow();
//              int column = target.getSelectedColumn();
//              System.out.println(e.getClickCount()+"SelectSplitBillActionPerformed" );
//         if (e.getClickCount() == 1) {
//        System.out.println("SelectSplitBillActionPerformed"+tinfoLocal.get(1).getSplitId());        
//        System.out.println("SelectSplitBillActionPerformed"+tinfoLocal.get(0).getSplitId());              
//        splitId=tinfoLocal.get(row).getSplitId();
//        System.out.println("SelectSplitBillActionPerformed"+tinfoLocal.get(row).getSplitId());
//        dispose();
//         }
//    
//  }
//});
//     jSplitTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
//        public void valueChanged(ListSelectionEvent event) {
//            // do some actions here, for example
//            // print first column value from selected row
//          int row= jSplitTable1.getSelectedRow();
//          splitId=tinfoLocal.get(row).getSplitId();
//          dispose();
//        }
//    });   
        
    }//GEN-LAST:event_SelectSplitBillActionPerformed

    private void WindowClosingActionPerformed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_WindowClosingActionPerformed
        splitId=null;
        dispose();
    }//GEN-LAST:event_WindowClosingActionPerformed

    private void MousePressedPerformed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MousePressedPerformed
        int row = jSplitTable1.getSelectedRow();
        splitId=tinfoLocal.get(row).getSplitId();
         dispose();
        
    }//GEN-LAST:event_MousePressedPerformed
private static Window getWindow(Component parent) {
        if (parent == null) {
            return new JFrame();
        } else if (parent instanceof Frame || parent instanceof Dialog) {
            return (Window) parent;
        } else {
            return getWindow(parent.getParent());
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jSplitTable1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void valueChanged(ListSelectionEvent lse) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}


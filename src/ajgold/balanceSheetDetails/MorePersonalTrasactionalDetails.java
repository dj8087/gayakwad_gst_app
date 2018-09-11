/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PersonalTrasactionalDetails.java
 *
 * Created on Sep 9, 2015, 9:45:38 PM
 */

package ajgold.balanceSheetDetails;

import com.mysql.jdbc.ResultSetMetaData;
import gayakwad.DBConnection;
import java.sql.ResultSet;
import com.implidea.DecimalFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author admin
 */
public class MorePersonalTrasactionalDetails extends javax.swing.JDialog {

    /** Creates new form PersonalTrasactionalDetails */
    Vector<PTrasction> transaction;
    public MorePersonalTrasactionalDetails(java.awt.Frame parent, boolean modal,Vector<PTrasction> transaction) {
        super(parent, modal);
        initComponents();
        this.transaction=transaction;
        java.awt.Dimension dim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-getSize().height/2);
        
      //  jTable1.setShowGrid(false);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        jTable1.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
        jTable1.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        jTable1.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        jTable1.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        //jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
       // jTable1.getColumnModel().getColumn(0).setPreferredWidth(250);


        //load transaction..
        String[][] data = new String[transaction.size()][];
    }

    DecimalFormat df_gold = new DecimalFormat("########.###");
    DecimalFormat df_money = new DecimalFormat("########.##");


    String[][] data = null;//new String[count][];
    int[] invoice_no=null;
    public void setDataOfCustomer(String cname){
        try {
            jLabel3.setText(cname);
            DBConnection con = new DBConnection();
            int count=0;
            try{
            ResultSet rs = con.executeQuery("SELECT count(id) FROM aj_transations where name='"+cname+"';");
            if(rs.next())
                count=rs.getInt(1);
            } catch (Exception ex) {
                Logger.getLogger(MorePersonalTrasactionalDetails.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(count==0){
                JOptionPane.showMessageDialog(rootPane, "No Transactions Available!");
                dispose();
                return;
            }
            data = new String[count][];
            invoice_no=new int[count];
            int index = 0;
            ResultSet rs = con.executeQuery("SELECT * FROM aj_transations where name='"+cname+"' order by id desc;");
            PersonalTransactions pt = new PersonalTransactions();
            while(rs.next()){
                data[index] = new String[6];
                int tid = rs.getInt(1);//id
                rs.getString(2); //name
                String t_type = rs.getString(3);
                float amt = rs.getFloat(4);
                Date dt = rs.getDate(5);
                String date = ""+(dt.getYear()+1900)+"-"+(dt.getMonth()+1)+"-"+dt.getDate();
                System.out.println("["+date+"]");

                

                invoice_no[index] = rs.getInt(6);
                String cmmt = rs.getString(7);

                pt.addNewTransaction(tid, dt, t_type, amt, cmmt, invoice_no[index]);
              
                data[index][0] = ""+dt.getDate()+"-"+(dt.getMonth()+1)+"-"+(dt.getYear()+1900);
                if(t_type.equals("GOLD")){
                    if(amt>0){
                        data[index][1] = ""+df_money.format(amt);
                    }else{

                        data[index][2] = df_gold.format(Math.abs(amt));
                    }
                }else{
                    if(amt>0){
                        data[index][3] = ""+df_money.format(amt);
                    }else{

                        data[index][4] = df_gold.format(Math.abs((amt)));
                    }
                }
                data[index][5] = cmmt;
                index++;
            }

             jTable1.setModel(new javax.swing.table.DefaultTableModel(data,
            new String [] {
                "Date", "Gold-Jama", "Gold-Nave", "Money-Jama", "Money-Nave", "Comment"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });

    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
    jTable1.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
    jTable1.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
    jTable1.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
    jTable1.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);

        } catch (Exception ex) {
            Logger.getLogger(MorePersonalTrasactionalDetails.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 102, 102));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Transaction Type", "Amount", "Invoice No", "Comment"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setGridColor(new java.awt.Color(0, 0, 0));
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Customer Transaction At:");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 24));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Name:");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 24));
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Dheeraj Maralkar");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 936, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 367, Short.MAX_VALUE)
                .addContainerGap())
        );

        jMenu3.setText("Options");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Exit");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem1);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // exit
        dispose();
}//GEN-LAST:event_jMenuItem1ActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
         
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * BalanceSheet.java
 *
 * Created on Sep 3, 2015, 11:28:26 AM
 */

package ajgold.balanceSheetDetails;

import ajgold.*;
import gayakwad.DBConnection;
import java.awt.Frame;
import java.sql.ResultSet;
import com.implidea.DecimalFormat;
import gayakwad.AllConstants;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import raw.RawItemCost;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
/**
 *
 * @author Shree
 */
public class CustomerBalanceSheet extends javax.swing.JDialog {

    boolean customer = false;
    Frame parent;
    /** Creates new form BalanceSheet */
    public CustomerBalanceSheet(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.parent=parent;
        initComponents();
        java.awt.Dimension dim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-getSize().height/2);
        jTable1.setDefaultRenderer(Object.class, new MyDefaultTableCellRenderer());
        jTable2.setDefaultRenderer(Object.class, new MyDefaultTableCellRenderer());
    }

    


    class Cust_Bal_Sheet{
        final String name,gold,money;

        public Cust_Bal_Sheet(String name, String gold, String money) {
            this.name = name;
            this.gold = gold;
            this.money = money;
            System.out.println("New Cust:["+name+"]["+gold+"]["+money+"]");
        }

        public String[] toArr(){
            return new String[]{name,gold,money};
        }
        
    }
    DecimalFormat df_gold = new DecimalFormat("########.###");
    DecimalFormat df_money = new DecimalFormat("########");

    float total_nave = 0,total_jama=0;
    Vector<Cust_Bal_Sheet> nave = new Vector<Cust_Bal_Sheet>();
    Vector<Cust_Bal_Sheet> jama = new Vector<Cust_Bal_Sheet>();

    public void loadBalanceSheetForWorker(){
        //SELECT name,gold_ac,money_ac FROM worker_mst natural join worker_ac;


        jLabel1.setText("****Worker Balance Sheet****");

           try {
            DBConnection con = new DBConnection();
            ResultSet rs;
            rs = con.executeQuery("SELECT name,gold_ac,money_ac,advance FROM worker_mst natural join worker_ac order by name;");
            loadBalSheetFromRs(rs,1000);
        }catch(Exception e){
            
        }
    }


    private void loadBalSheetFromRs(ResultSet rs,int divide) throws Exception{
        int index = 0;
         while(rs.next()){
                String name = rs.getString(1);
                float gold_ac = rs.getFloat(2);
                float money = rs.getFloat(3);
                float advance = rs.getFloat(4);
                money = money+advance;

                if(gold_ac==0 && money==0)  continue;
                else if(gold_ac < 0 && money < 0)
                {
                    System.out.println("A1");
                    nave.add(new Cust_Bal_Sheet(name, df_gold.format(Math.abs(gold_ac/divide)),  df_money.format(Math.abs(money))));
                }
                else if(gold_ac<0){
                    System.out.println("A2");
                    nave.add(new Cust_Bal_Sheet(name, df_gold.format(Math.abs(gold_ac/divide)),  "0"));
                    if(money!=0)
                        jama.add(new Cust_Bal_Sheet(name,"0",  df_money.format(Math.abs(money))));
                }else if(money<0){
                    System.out.println("A3");
                    if(gold_ac!=0)
                        jama.add(new Cust_Bal_Sheet(name, df_gold.format(Math.abs(gold_ac/divide)),  "0"));
                    nave.add(new Cust_Bal_Sheet(name,"0",  df_money.format(Math.abs(money))));
                }else {

                    System.out.println("A4");
                    jama.add(new Cust_Bal_Sheet(name, df_gold.format(Math.abs(gold_ac/divide)),  df_money.format(Math.abs(money))));
                }
            }

            //jama data[]
            {
                //int[] rmv = new int[jama.size()];
                removeZero(jama);
                int count = jama.size();
                float total_gold=0,total_money=0;
                String[][] jama_data = new String[count+1][];    //+1 for total
                for(index=0;index<count;index++){
                    jama_data[index] = new String[3];
                    jama_data[index][0] =  jama.get(index).name;
                    jama_data[index][1] =  jama.get(index).gold;
                    jama_data[index][2] =  jama.get(index).money;
                    total_gold+= Float.parseFloat(jama.get(index).gold);
                    total_money+= Float.parseFloat(jama.get(index).money);
                }
                jama_data[jama_data.length-1] = new String[3];
                jama_data[jama_data.length-1][0] = "Total";
                jama_data[jama_data.length-1][1] = df_gold.format(total_gold);
                jama_data[jama_data.length-1][2] = df_money.format(total_money);

                       jTable1.setModel(new javax.swing.table.DefaultTableModel(jama_data,new String [] { "Name", "Gold", "Money" })
                        {
                            Class[] types = new Class [] {
                                java.lang.String.class, java.lang.String.class, java.lang.String.class
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


            }

                    //nave data[]
            removeZero(nave);
            int count = nave.size();
            float total_gold=0,total_money=0;
            String[][] nave_data = new String[count+1][];    //+1 for total
            for(index=0;index<count;index++){
                nave_data[index] = new String[3];
                nave_data[index][0] =  nave.get(index).name;
                nave_data[index][1] =  nave.get(index).gold;
                nave_data[index][2] =  nave.get(index).money;

                total_gold+= Float.parseFloat(nave_data[index][1]);
                total_money+= Float.parseFloat(nave_data[index][2]);
            }
            nave_data[nave_data.length-1] = new String[3];
            nave_data[nave_data.length-1][0] = "Total";
            nave_data[nave_data.length-1][1] = df_gold.format(total_gold);
            nave_data[nave_data.length-1][2] = df_money.format(total_money);
            jTable2.setModel(new javax.swing.table.DefaultTableModel(nave_data,new String [] { "Name", "Gold", "Money" })
                    {
                        Class[] types = new Class [] {
                            java.lang.String.class, java.lang.String.class, java.lang.String.class
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

    }
    public void loadBalanceSheetForCustomer(){
        //SELECT name,wt,tounch,qnty FROM raw_stock_mst r;
        jLabel1.setText("****Customer Balance Sheet****");
        customer=true;
        try {
            DBConnection con = new DBConnection();
            ResultSet rs; 
            rs = con.executeQuery("SELECT name,gold_ac,money_ac,advance FROM customer_mst where cid not in (SELECT cid FROM customer_mst where gold_ac=0 and money_ac=0) order by name;");
           loadBalSheetFromRs(rs,1);
 
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(rootPane, ex);
        }

        
    }

    private void removeZero(Vector<Cust_Bal_Sheet> jama){
        int i=0;
        while(i<jama.size() && jama.size()>0){
            Cust_Bal_Sheet cbs = jama.get(i);
            if(cbs.gold.equals("0.000") && cbs.money.equals("0.000")){
                jama.remove(i);
                continue;
            }
            else
                i++;
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
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 102, 102));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24));
        jLabel1.setText("****Customer Balance Sheet****");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(132, 132, 132)
                .addComponent(jLabel1)
                .addContainerGap(156, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Jama");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Nave");

        jTable1.setFont(new java.awt.Font("Times New Roman", 0, 14));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Gold", "Money"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
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
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jTable2.setFont(new java.awt.Font("Times New Roman", 0, 14));
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Gold", "Money"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
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
        jTable2.setRowHeight(19);
        jTable2.setRowMargin(3);
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jButton3.setText("Exit (Ctrl+E)");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(77, 77, 77)
                        .addComponent(jLabel2)
                        .addGap(331, 331, 331)
                        .addComponent(jLabel3))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(26, 26, 26)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jMenu1.setText("Options");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Print");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText("Exit");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

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

        float total_nave_bal = 0;
        float total_jama_bal = 0;
        float total_nave_fin = 0;
        float total_jama_fin = 0;

        float profilte_bal = 0;//total_nave_bal-total_jama_bal;
        float profilte_gold = 0;//total_nave_fin-total_jama_gold;

    
 
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // Exit Menu
        dispose();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        // nave dlb click...
        if(evt.getClickCount()!=2)  return;
        int index = jTable2.getSelectedRow();
        String cust_name = nave.get(index).name;
        //JOptionPane.showMessageDialog(rootPane, "Loading : "+cust_name);
//        PersonalTrasactionalDetails ptd = new PersonalTrasactionalDetails(parent, true);
//        ptd.setDataOfCustomer(cust_name);
//        ptd.setVisible(true);
        showBS(cust_name);
    }//GEN-LAST:event_jTable2MouseClicked

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
          if(evt.getClickCount()!=2)  return;
        int index = jTable1.getSelectedRow();
        String cust_name = jama.get(index).name;
        //JOptionPane.showMessageDialog(rootPane, "Loading : "+cust_name);
        showBS(cust_name);
    }//GEN-LAST:event_jTable1MouseClicked

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // Print Cust bal sheet..
        try {
            HashMap hm = new HashMap();
            JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(AllConstants.custBalSheetFileName, hm, new DBConnection().getConnection());
            JasperPrintManager.printPages(jprint, 0, jprint.getPages().size()-1, true);
            System.out.println("Printing!");
            System.out.println("Done!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed


    private void showBS(String cust_name){
       
        final PersonalTrasactionalDetails ptd = new PersonalTrasactionalDetails(parent, true);
        if(!ptd.setDataOfCustomer(cust_name))
        {
            JOptionPane.showMessageDialog(rootPane, "No Transactions Available!");
            return;
        }
        setVisible(false);
        ptd.setVisible(true);
        
            
        new Thread(){
                 public void run(){
                     while(ptd.isMeActive()){
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(PersonalTrasactionalDetails.class.getName()).log(Level.SEVERE, null, ex);
                        }
                     }
                     setVisible(true);
                 }
             }.start();
    }
    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CustomerBalanceSheet dialog = new CustomerBalanceSheet(new javax.swing.JFrame(), true);
                dialog.loadBalanceSheetForCustomer();
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    // End of variables declaration//GEN-END:variables

}

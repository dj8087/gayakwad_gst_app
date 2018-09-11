


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * BalanceSheet.java
 *
 * Created on Sep 3, 2015, 11:28:26 AM
 */

package ajgold;
import ajgold.balanceSheetDetails.RawStock;
import ajgold.balanceSheetDetails.SaleStock;
import ajgold.balanceSheetDetails.CustomerBalanceSheet;
import ajgold.balanceSheetDetails.In_Hand_Transactions;
import gayakwad.DBConnection;
import java.awt.*;
import java.sql.ResultSet;
import com.implidea.DecimalFormat;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import raw.RawItemCost;
import ajgold.balanceSheetDetails.MyDefaultTableCellRenderer;
import java.util.Vector;
import raw.BinLaki;
import raw.RowItem;
import stock.Product;
import stock.Stock;
/**
 *
 * @author Shree
 */
public class BalanceSheet extends javax.swing.JDialog {

    /** Creates new form BalanceSheet */
    Frame parent;
    public BalanceSheet(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.parent=parent;
        initComponents();
        java.awt.Dimension dim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-getSize().height/2);
        loadBalanceSheet();
        
        jTable1.setDefaultRenderer(Object.class, new MyDefaultTableCellRenderer());
        jTable2.setDefaultRenderer(Object.class, new MyDefaultTableCellRenderer());
    }

    

    private void loadBalanceSheet(){
        loadMoneyAccount();
        loadRawFineLabor();
        loadThusiGoldFineLabor();
        loadOutSourcedRow();
        //showMoney();
         showBalanceSheet();
    }
    

    float opening_balance_money = 0, opening_balance_gold=0;
    float inhand_money = 0, inhand_gold=0;
    float out_sourced_row=0,faulty_raw=0,bin_laki_fine=0;
    float our_sourced_raw_labor =  0;
    float faulty_raw_labor =  0;
    float bin_laki_labor =  0;

    Acc nave = new Acc();
    Acc jama  = new Acc();

    // nave..
    class Acc{
        

        float thusi_total_Cost = 0;
        float thusi_total_fine=0;

        float raw_total_fine = 0;
        float raw_total_cost = 0;

        float cust_total_fin=0;
        float cust_total_money=0;


        float wrkr_total_fin=0;
        float wrkr_total_money=0;
        

    }

    //jama

    

    DecimalFormat df_gold = new DecimalFormat("########.###");
    DecimalFormat df_money = new DecimalFormat("########");
    Vector<RowItem> allRawStock = new Vector<RowItem>();
    private void loadRawFineLabor(){
        //SELECT name,wt,tounch,qnty FROM raw_stock_mst r;

        try {
            DBConnection con = new DBConnection();
            ResultSet rs = con.executeQuery("SELECT name,wt,tounch,qnty FROM raw_stock_mst r;");
            while(rs.next()){

                String name = rs.getString(1);
                float wt = rs.getFloat(2);
                float tnch = rs.getFloat(3);
                int qnty = rs.getInt(4);
                RowItem ri = new RowItem(name, wt, tnch);
                ri.qnty=qnty;
                allRawStock.add(ri);
                jama.raw_total_fine += ri.getTotalFineInGram();
                jama.raw_total_cost+= ri.getTotalLabor();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(rootPane, ex);
        }

        
    }
    private Stock stock = new Stock();
    private void loadThusiGoldFineLabor(){
        try {
            DBConnection con = new DBConnection();
            ResultSet rs = con.executeQuery("select * from stock_mst;");

            while(rs.next()){
                Product pp;
                {
                    String code = rs.getString(1);
                    String name = rs.getString(2);
                    float wt = rs.getFloat(3);
                    String details = rs.getString(4);
                    pp = new Product(code, name, wt, details);
                    pp.getFine();
                    stock.addProduct(pp);
                }
                jama.thusi_total_fine+=pp.getFine();     //(wt*this_fine/100);
                jama.thusi_total_Cost+=pp.labor;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(rootPane, ex);
        }

    }

    Vector<RowItem> allOutRaw = new Vector<RowItem>();
    Vector<RowItem> allFalutyRaw = new Vector<RowItem>();
    Vector<RowItem> allBinLakiRaw = new Vector<RowItem>();
    private void loadOutSourcedRow(){
        try {
            //SELECT wid,name,confermordermaterial.order_id,itemName,qnty,wt,tounch,(qnty*wt*tounch/100)/1000 as fine FROM confermordermaterial natural join order_to_worker_map natural join worker_mst where confermordermaterial.order_id not in (SELECT order_id FROM completedorder);
            DBConnection con = new DBConnection();
          
             ResultSet rs = con.executeQuery("SELECT itemName,wt,tounch,qnty FROM confermordermaterial where order_id not in (SELECT order_id FROM completedorder) order by order_id,itemName;");
             
        while(rs.next()){
            String name = rs.getString(1);
            float wt = rs.getFloat(2);
            float tounch = rs.getFloat(3);
            int qnty = rs.getInt(4);
            RowItem ri = new RowItem(name, wt, tounch);
            ri.qnty=qnty;
            out_sourced_row+=ri.getTotalFineInGram();
            our_sourced_raw_labor+=ri.getTotalLabor();
            allOutRaw.add(ri);

        }

         //System.out.println("out labor:"+our_sourced_raw_labor);

            //SELECT qnty,wt,tounch,((qnty*wt*tounch/100)/1000) as fine  FROM faultyraw f;
            
            rs = con.executeQuery("SELECT name,wt,tounch,qnty  FROM faultyraw order by name;;");
            while(rs.next()){
                String name = rs.getString(1);
                float wt = rs.getFloat(2);
                float tounch = rs.getFloat(3);
                int qnty = rs.getInt(4);
                 RowItem ri = new RowItem(name, wt, tounch);
                ri.qnty=qnty;
                faulty_raw+=ri.getTotalFineInGram();
                faulty_raw_labor+=ri.getTotalLabor();
                allFalutyRaw.add(ri);
            }


            //bin laki...

            //SELECT name,wt,tounch,'1' as qnty FROM raw_non_qnty r;
             rs = con.executeQuery("SELECT name,wt,tounch,'1' as qnty FROM raw_non_qnty r;");
            while(rs.next()){
                String name = rs.getString(1);
                float wt = rs.getFloat(2);
                float tounch = rs.getFloat(3);
                int qnty = rs.getInt(4);
                BinLaki ri = new BinLaki(name, wt, tounch);
                ri.qnty=qnty;
                bin_laki_fine+=ri.getTotalFineInGram();
                bin_laki_labor+=ri.getTotalLabor();
                allBinLakiRaw.add(ri);
            }
            

        } catch (Exception ex) {
            Logger.getLogger(BalanceSheet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void loadMoneyAccount(){
        try {
            //nave rup.   SELECT sum(money_ac) FROM customer_mst where money_ac<0;
            //jama rup..  SELECT sum(money_ac) FROM customer_mst where money_ac>0;
            //opening       SELECT money FROM aj_gold_acc_m_g a;
            //wrker  nave jama   SELECT sum(gold_ac)/1000 FROM worker_ac where gold_ac<0;
            //                   SELECT sum(money_ac) FROM worker_ac where money_ac>0;

           
            DBConnection con = new DBConnection();
            ResultSet rs = con.executeQuery("SELECT money,gold FROM aj_gold_acc_m_g order by dt desc;");
            if(rs.next()){
                opening_balance_money = rs.getFloat(1);
                opening_balance_gold = rs.getFloat(2);
            }
            rs = con.executeQuery("SELECT money,gold FROM aj_gold_in_hand order by dt desc;");
            if(rs.next()){
                inhand_money = rs.getFloat(1);
                inhand_gold = rs.getFloat(2);
            }
            
            rs = con.executeQuery("SELECT sum(money_ac) FROM customer_mst where money_ac<0 union SELECT sum(gold_ac) FROM customer_mst where gold_ac<0;");
            rs.next();
            nave.cust_total_money+=rs.getFloat(1);
            rs.next();
            nave.cust_total_fin+=rs.getFloat(1);

            
           
            rs = con.executeQuery("SELECT sum(money_ac) FROM customer_mst where money_ac>=0 union SELECT sum(gold_ac) FROM customer_mst where gold_ac>=0;");
            rs.next();
            jama.cust_total_money+=rs.getFloat(1);
            rs.next();
            jama.cust_total_fin+=rs.getFloat(1);



            


            rs = con.executeQuery("SELECT sum(gold_ac)/1000 FROM worker_ac where gold_ac>=0  union SELECT sum(money_ac) FROM worker_ac where money_ac>=0;");
            rs.next();
            jama.wrkr_total_fin+=rs.getFloat(1);
            rs.next();
            jama.wrkr_total_money+=rs.getFloat(1);




            rs = con.executeQuery("SELECT sum(gold_ac)/1000 FROM worker_ac where gold_ac<0  union SELECT sum(money_ac) FROM worker_ac where money_ac<0;");
            rs.next();
            nave.wrkr_total_fin+=rs.getFloat(1);
            rs.next();
            nave.wrkr_total_money+=rs.getFloat(1);


        } catch (Exception ex) {
            Logger.getLogger(BalanceSheet.class.getName()).log(Level.SEVERE, null, ex);
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
        jButton2 = new javax.swing.JButton();
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
        jLabel1.setText("****Balance Sheet****");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(165, 165, 165)
                .addComponent(jLabel1)
                .addContainerGap(229, Short.MAX_VALUE))
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

        jButton2.setText("Update Opening Balance (Ctrl +U)");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

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
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(77, 77, 77)
                        .addComponent(jLabel2)
                        .addGap(331, 331, 331)
                        .addComponent(jLabel3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE))
                .addContainerGap())
        );

        jMenu1.setText("Options");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Update");
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
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

        float total_nave_bal = 0;
        float total_jama_bal = 0;
        float total_nave_fin = 0;
        float total_jama_fin = 0;

        float profilte_bal = 0;//total_nave_bal-total_jama_bal;
        float profilte_gold = 0;//total_nave_fin-total_jama_gold;

    private void showBalanceSheet(){

//        jLabel7.setText("Gold");

        //nave
        String[][] nave_data = new String[9][];
        //data[0] = new String[]{"Opening",df.format(jama.thusi_total_fine),df.format(jama.thusi_total_Cost)};
        nave_data[0] = new String[]{"Thusi",df_gold.format(Math.abs(jama.thusi_total_fine)),df_money.format(jama.thusi_total_Cost)};
        nave_data[1] = new String[]{"Raw",df_gold.format(Math.abs(jama.raw_total_fine)),df_money.format(Math.abs(jama.raw_total_cost))};
        nave_data[2] = new String[]{"Customer",df_gold.format(Math.abs(nave.cust_total_fin)),df_money.format(Math.abs(nave.cust_total_money))};
        nave_data[3] = new String[]{"Worker",df_gold.format(Math.abs(nave.wrkr_total_fin)),df_money.format(Math.abs(nave.wrkr_total_money))};
        nave_data[4] = new String[]{"Order",""+df_gold.format(Math.abs(out_sourced_row)),df_money.format(our_sourced_raw_labor)};
        nave_data[5] = new String[]{"Faulty",df_gold.format(Math.abs(faulty_raw)),df_money.format(faulty_raw_labor)};
        nave_data[6] = new String[]{"Bin Laki",df_gold.format(Math.abs(bin_laki_fine)),df_money.format(Math.abs(bin_laki_labor))};
        nave_data[7] = new String[]{"In Hand",df_gold.format(Math.abs(inhand_gold)),df_money.format(Math.abs(inhand_money))};
        total_nave_bal = bin_laki_labor+inhand_money+Math.abs(jama.thusi_total_Cost)+Math.abs(jama.raw_total_cost)+Math.abs(nave.cust_total_money)+Math.abs(nave.wrkr_total_money)+our_sourced_raw_labor+faulty_raw_labor;
        total_nave_fin = bin_laki_fine+inhand_gold+Math.abs(jama.thusi_total_fine)+Math.abs(jama.raw_total_fine)+Math.abs(nave.cust_total_fin)+Math.abs(nave.wrkr_total_fin)+out_sourced_row+faulty_raw;
        
        nave_data[8] = new String[]{"Total",df_gold.format(total_nave_fin),df_money.format(total_nave_bal)};

        
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


        //jama
        String[][] jama_data = new String[5][];
        jama_data[0] = new String[]{"Opening Balance",""+df_gold.format(Math.abs(opening_balance_gold)),""+df_money.format(Math.abs(opening_balance_money))};
        jama_data[1] = new String[]{"Customer",""+df_gold.format(Math.abs(jama.cust_total_fin)),""+df_money.format(Math.abs(jama.cust_total_money))};
        jama_data[2] = new String[]{"Worker",""+df_gold.format(Math.abs(jama.wrkr_total_fin)),""+df_money.format(Math.abs(jama.wrkr_total_money))};
        total_jama_bal = Math.abs(opening_balance_money)+Math.abs(jama.cust_total_money)+Math.abs(jama.wrkr_total_money);
        total_jama_fin =Math.abs( opening_balance_gold)+Math.abs(jama.cust_total_fin)+Math.abs(jama.wrkr_total_fin);
        

        profilte_bal =  total_nave_bal-total_jama_bal;
        profilte_gold = total_nave_fin-total_jama_fin;


        float total_bal = profilte_bal+total_jama_bal;
        float total_fin = profilte_gold+total_jama_fin;

        

        jama_data[3] = new String[]{"Profite/Loss",""+df_gold.format(profilte_gold) ,""+df_money.format(profilte_bal)};
        jama_data[4] = new String[]{"Total",""+df_gold.format(total_fin) ,""+df_money.format(total_bal)};

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

    

    private void showMoney(){

//        jLabel7.setText("Money");
//                //jama
//        jList1.setModel(new javax.swing.AbstractListModel() {
//            String[] strings = {""+opening_balance+" Shri Shillk",""+jama.thusi_total_Cost+" Thushi",
//                                ""+jama.raw_total_cost+" Raw", ""+jama.cust_total_money+" Customer",""+jama.wrkr_total_money+" Worker"};
//            public int getSize() { return strings.length; }
//            public Object getElementAt(int i) { return strings[i]; }
//        });
//
//
//        //nave
//
//        jList2.setModel(new javax.swing.AbstractListModel() {
//            String[] strings = {""+Math.abs(nave.cust_total_money)+" Customer",""+Math.abs(nave.wrkr_total_money)+" Worker"};
//            public int getSize() { return strings.length; }
//            public Object getElementAt(int i) { return strings[i]; }
//        });
    }
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // Money Menu
        updateOpenigBal();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // Exit Menu
        dispose();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Update Opeing
        updateOpenigBal();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // jama tbl one click
        if(evt.getClickCount()==2){
            int index = jTable1.getSelectedRow();

            System.out.println("Bal Sheet:1:2:"+index);
            switch(index){

                case 1: //customer jama
                    CustomerBalanceSheet dialog = new CustomerBalanceSheet(parent, true);
                    dialog.loadBalanceSheetForCustomer();
                    dialog.setVisible(true);
                    setVisible(false);
                    break;
                case 2: //worker jama
                    CustomerBalanceSheet dialogW = new CustomerBalanceSheet(parent, true);
                    dialogW.loadBalanceSheetForWorker();
                    dialogW.setVisible(true);
                    break;


                    
            }
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        // nave dbl click
        if(evt.getClickCount()==2){
            int index = jTable2.getSelectedRow();

            System.out.println("Bal Sheet:2:2:"+index);
            switch(index){
                case 0: //thusi
                    SaleStock ss = new SaleStock(parent, true,stock);
                    ss.setVisible(true);
                    break;
                case 1: //Raw..
                    RawStock rs = new RawStock(parent, true);
                    rs.setVisible(true);
                    break;
                case 2: //customer jama
                    CustomerBalanceSheet dialog = new CustomerBalanceSheet(parent, true);
                    dialog.loadBalanceSheetForCustomer();
                    dialog.setVisible(true);
                   // setVisible(false);
                    break;
                case 3: //worker jama
                    CustomerBalanceSheet dialogW = new CustomerBalanceSheet(parent, true);
                    dialogW.loadBalanceSheetForWorker();
                    dialogW.setVisible(true);
                    break;

                case 4: //out sourced raw
                    OutSourcedRaw osr = new OutSourcedRaw(parent, true);
                    osr.loadRow();
                    osr.setVisible(true);
//                    RawStock rsout = new RawStock(parent, true, allOutRaw);
//                    rsout.hideBtns();
//                    rsout.setHeaderTxt("Order Raw Material");
//                    rsout.setVisible(true);
                    break;


                case 5: //faulty  raw

                    RawStock rsfalt = new RawStock(parent, true, allFalutyRaw);
                    rsfalt.hideBtns();
                    rsfalt.setHeaderTxt("Faulty Raw Material");
                    rsfalt.setVisible(true);
//                    osr = new OutSourcedRaw(parent, true);
//                    osr.loadRow();
//                    osr.setVisible(true);
                    break;



                case 6: //Bin laki

                    RawStock binlaki = new RawStock(parent, true, allBinLakiRaw);
                    binlaki.hideBtns();
                    binlaki.setHeaderTxt("Bin Laki Material");
                    binlaki.setVisible(true);
//                    osr = new OutSourcedRaw(parent, true);
//                    osr.loadRow();
//                    osr.setVisible(true);
                    break;


                case 7:
                    new In_Hand_Transactions(parent, true).setVisible(true);
                    break;


            }
        }
    }//GEN-LAST:event_jTable2MouseClicked

    private void updateOpenigBal(){
        try {
                float newBalacce = opening_balance_money + profilte_bal;
                float newGold = opening_balance_gold + profilte_gold;
                JOptionPane.showMessageDialog(rootPane, "Update To :" + df_gold.format(newGold) + "gm " + df_money.format(newBalacce) + "/- Rs.");
                int op = JOptionPane.showConfirmDialog(rootPane, "Do you want Update Opening Stock?");
                /*
                 * 0-Yes
                 * 1-No
                 * 2-Cacel
                 */
                if (op == 0) {
                    //yes

                        //update..
                    DBConnection con = new DBConnection();
                    ResultSet rs = con.executeQuery("select max(id) from aj_gold_acc_m_g;");
                    int nid=1;
                    if(rs.next())
                        nid=rs.getInt(1);
                    nid++;
                    con.executeUpdate("insert into aj_gold_acc_m_g values(" + newBalacce + "," + newGold + "," + nid + ",now())");
                    dispose();
                    new BalanceSheet(parent, true).setVisible(true);
                } else if (op == 1) {
                    //no
                } else {
                    //cancel
                }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(rootPane, "Error:"+ex);
        }
    }
    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                BalanceSheet dialog = new BalanceSheet(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButton2;
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

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

import gayakwad.DBConnection;
import java.sql.ResultSet;
import com.implidea.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import raw.RawItemCost;

/**
 *
 * @author Shree
 */
public class BalanceSheet_With_List extends javax.swing.JDialog {

    /** Creates new form BalanceSheet */
    public BalanceSheet_With_List(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        java.awt.Dimension dim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-getSize().height/2);
        loadBalanceSheet();
    }

    private void loadBalanceSheet(){
        loadMoneyAccount();
        loadRawFineLabor();
        loadThusiGoldFineLabor();


        //jama
        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = {""+opening_balance+" Shri Shillk",""+jama.thusi_total_Cost+" Thushi",
                                ""+jama.raw_total_cost+" Raw", ""+jama.cust_total_money+" Customer",""+jama.wrkr_total_money+" Worker"};
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });


        //nave

        jList2.setModel(new javax.swing.AbstractListModel() {
            String[] strings = {""+Math.abs(nave.cust_total_money)+" Customer",""+Math.abs(nave.wrkr_total_money)+" Worker"};
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
    }
    

    float opening_balance = 0;
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

    

    DecimalFormat df = new DecimalFormat("#####.###");

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

                jama.raw_total_fine += (((wt * (tnch/100)) * qnty)/1000);

                for(Object[] obj : RawItemCost.rawItemCost){
                    if(name.toLowerCase().contains(((String) obj[0]).toLowerCase())){
                        jama.raw_total_cost+= (((Float) obj[1])*qnty);
                    }
                }
                
            }


        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(rootPane, ex);
        }

        
    }

    private void loadThusiGoldFineLabor(){
        try {
            DBConnection con = new DBConnection();
            ResultSet rs = con.executeQuery("select * from stock_mst;");

            while(rs.next()){
                rs.getString(1);
                String name = rs.getString(2);
                float wt = rs.getFloat(3);
                String details = rs.getString(4);
                float this_fine = 0;
                float this_cost = 0;
                if(details.equals("NA")){

                    this_cost=250;
                    if(name.toLowerCase().indexOf("dou")==0 ||  name.toLowerCase().indexOf("du")==0)
                        this_cost=750;
                    
                    this_fine = 80;
                    if(name.contains("-92")){
                        this_fine=97;
                    }
                    jama.thusi_total_Cost+=this_cost;
                    jama.thusi_total_fine+=(wt*this_fine/100);
                }else{
                    /*
                        Pot 7.5::608::79.5::7.6&&
                        Checks 13::92::79.0::13.0&&
                        Checks 24::4::79.0::35.0&&
                     panadi 350::1::57.0::340.0&&
                     bol ghungaru::3::66.0::62.45&&
                     kadya::1::57.0::19.0&&
                     __12
                    */
                    String[] sp1 = details.split("__");

                    String[] sp2 = sp1[0].split("&&");
                   // System.out.println("["+sp2[0]+"]["+sp2[1]+"]");
                    String[][] data = new String[sp2.length][];
                    for(int i=0;i<data.length;i++){
                        String[] sp3 = sp2[i].split("::");
                        data[i]= new String[4];
                        data[i][0]  = sp3[0];
                        data[i][1]  = sp3[3];
                        data[i][2]  = sp3[2];
                        data[i][3]  = sp3[1];

                        jama.thusi_total_fine+= (((((Float.parseFloat(data[i][1])/1000)*Float.parseFloat(data[i][2]))/100)*Integer.parseInt(data[i][3])));

                        for(Object[] obj : RawItemCost.rawItemCost){
                            if(data[i][0].toLowerCase().contains(((String) obj[0]).toLowerCase())){
                                jama.thusi_total_Cost+= (Float) obj[1];
                            }
                        }
                    }
                }
            }


        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(rootPane, ex);
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
            ResultSet rs = con.executeQuery("SELECT money FROM aj_gold_acc_m_g a;");
            if(rs.next())
                opening_balance = rs.getFloat(1);

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
            Logger.getLogger(BalanceSheet_With_List.class.getName()).log(Level.SEVERE, null, ex);
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
        jList1 = new javax.swing.JList();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 102, 102));

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

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
                .addContainerGap(186, Short.MAX_VALUE))
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

        jList2.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList2);

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Nave");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Total:");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Total:");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Tally:");

        jButton1.setText("Money");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Gold");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Money");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 105, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 168, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGap(120, 120, 120)
                .addComponent(jLabel3)
                .addGap(100, 100, 100))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addContainerGap(555, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

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

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Gold

        jLabel7.setText("Gold");
        //jama
        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = {""+df.format(jama.thusi_total_fine)+" Thushi",
                                ""+df.format(jama.raw_total_fine)+" Raw",
                                ""+df.format(jama.cust_total_fin)+" Customer",
                                ""+df.format(jama.wrkr_total_fin)+" Worker"};
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });


        //nave

        jList2.setModel(new javax.swing.AbstractListModel() {
            String[] strings = {""+Math.abs(nave.cust_total_fin)+" Customer",
                                ""+Math.abs(nave.wrkr_total_fin)+" Worker"};
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });

        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // money

        jLabel7.setText("Money");
                //jama
        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = {""+opening_balance+" Shri Shillk",""+jama.thusi_total_Cost+" Thushi",
                                ""+jama.raw_total_cost+" Raw", ""+jama.cust_total_money+" Customer",""+jama.wrkr_total_money+" Worker"};
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });


        //nave

        jList2.setModel(new javax.swing.AbstractListModel() {
            String[] strings = {""+Math.abs(nave.cust_total_money)+" Customer",""+Math.abs(nave.wrkr_total_money)+" Worker"};
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });

    }//GEN-LAST:event_jButton1ActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                BalanceSheet_With_List dialog = new BalanceSheet_With_List(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AllOrders.java
 *
 * Created on Aug 18, 2015, 1:38:30 PM
 */

package workers;

import gayakwad.DBConnection;
import gayakwad.NewDesing;
import java.awt.Frame;
import java.sql.ResultSet;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import raw.ProductDesing;
import raw.RowItem;

/**
 *
 * @author admin
 */
public class AllOrders extends javax.swing.JDialog {

    /** Creates new form AllOrders */
    JFrame parent;
    public AllOrders(JFrame parent, boolean modal) {
        super(parent, modal);
        this.parent=parent;
        initComponents();

          java.awt.Dimension dim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            this.setLocation(dim.width/2-this.getSize().width/2, 0);


        loadAllNonCompletedOrder();
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(allOW));
        jComboBox1.setSelectedIndex(0);
        jComboBox1ItemStateChanged(null);
        jComboBox1.requestFocus();
        suggesion();
    }


WorkerObj[] wo;
ProductDesing[] desings;
RowItem[] rt;
    private void suggesion(){
        try {
                DBConnection con = new DBConnection();

                ResultSet rs;

                    rs = con.executeQuery("SELECT count(*) FROM design_mst;");
                    rs.next();
                    desings = new ProductDesing[rs.getInt(1)];

                    rs = con.executeQuery("SELECT * FROM design_mst;");
                   int i = 0;
                    while (rs.next()) {
                        int code = rs.getInt(1);
                        String name = rs.getString(2);
                        desings[i] = new ProductDesing(code, name);
                        i++;
                    }

                    jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(desings));
                    
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(rootPane, ex, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }




    
    class OrderWorder{
        public final int order_id,wid;
        public final String wname;
        public OrderDetails od;
        public FinalMaterial[] fm;

        public OrderWorder(int order_id, int wid, String wname) {
            this.order_id = order_id;
            this.wid = wid;
            this.wname = wname;
        }

        public void loadDetails(){
            od = OrderDetails.loadOrder(order_id);
        }
        public void loadFinalMaterial(){
             DBConnection con;
            try {
                con = new DBConnection();
                ResultSet rs = con.executeQuery("SELECT count(*) FROM confermordermaterial where order_id="+order_id);
                rs.next();
                int fmc = rs.getInt(1);
                if(fmc<1)   return;
                fm = new FinalMaterial[fmc];
                rs = con.executeQuery("SELECT itemname,wt,tounch,qnty FROM confermordermaterial where order_id="+order_id);
                int i=0;
                while(rs.next()){
                    fm[i] = new FinalMaterial(rs.getString(1), rs.getFloat(2), rs.getFloat(3), rs.getInt(4));
                    i++;
                }
            } catch (Exception ex) {
                Logger.getLogger(AllOrders.class.getName()).log(Level.SEVERE, null, ex);
            }
            

        }

        public void cancelOrder() throws Exception{
            try{
                new DBConnection().executeUpdate("call cancelOrder("+order_id+")");
            }catch(Exception e){
                if(e.getMessage().contains("No data - zero rows fetched, selected, or processed")){
                    System.out.println("Error OK");
                }
                else
                    e.printStackTrace();
            }
        }
        public void saveTODB() throws Exception{
            DBConnection con = new DBConnection();

            //first cancel order.
            cancelOrder();
            //then re-create
            
            //check qnty..
             for(FinalMaterial ffmm : fm){
                String qry = "SELECT qnty FROM raw_stock_mst where name='"+ffmm.raw_name+"' and wt="+ffmm.wt+" and tounch="+ffmm.tounch;
                System.out.println(qry);
                 ResultSet rs = con.executeQuery(qry);
                 if(rs.next()){
                    String qStr = rs.getString(1);
                    int qnty = Integer.parseInt(qStr);
                    System.out.println(ffmm.raw_name+"["+qnty+"]");
                    if(qnty<ffmm.qnty){
                        JOptionPane.showMessageDialog(rootPane, "Stock For "+ffmm.raw_name+" (wt:"+ffmm.wt+") Not Available","Alert",JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                 }else{
                        JOptionPane.showMessageDialog(rootPane, "Stock For "+ffmm.raw_name+" (wt:"+ffmm.wt+")  Not Available","Alert",JOptionPane.WARNING_MESSAGE);
                        return;
                 }
             }



             //.......................
              
              
                for(ProductDesing pd : od.desings){
                    String requ = "NA";
                    con.executeUpdate("insert into confermOrder values ("+order_id+",'"+pd.name+"',"+pd.qnty+",'"+requ+"')");
                }
                for(FinalMaterial ffmm : fm){
                    con.executeUpdate("insert into confermOrderMaterial values ("+order_id+",'"+ffmm.raw_name+"',"+ffmm.qnty+","+ffmm.wt+","+ffmm.tounch+")");
                    //deduct..
                    con.executeUpdate("update raw_stock_mst set qnty=qnty-"+ffmm.qnty+" where name='"+ffmm.raw_name+"' and wt="+ffmm.wt+" and tounch="+ffmm.tounch);
                }

                con.executeUpdate("insert into order_to_worker_map values('"+wid+"',"+order_id+")");


                JOptionPane.showMessageDialog(rootPane, "Order Changed!");

             //.......................


        }

        public String toString(){
            return ""+order_id;
        }
        
    }

    OrderWorder[] allOW;
    private void loadAllNonCompletedOrder(){
        try {
            DBConnection con = new DBConnection();
            ResultSet rs = con.executeQuery("SELECT distinct(c.order_id),wm.wid,wm.name FROM confermorder c join order_to_worker_map ow join worker_mst wm where wm.wid=ow.wid and c.order_id=ow.order_id and c.order_id not in (select order_id from completedorder);");
            Vector<OrderWorder> ow = new Vector<OrderWorder>();
            while(rs.next()){
                ow.add(new OrderWorder(rs.getInt(1), rs.getInt(2), rs.getString(3)));
            }

            int size = ow.size();
            allOW = new OrderWorder[size];
            for(int i=0;i<size;i++){
                allOW[i] = ow.get(i);
                allOW[i].loadDetails();
                allOW[i].loadFinalMaterial();
            }

        } catch (Exception ex) {
            Logger.getLogger(AllOrders.class.getName()).log(Level.SEVERE, null, ex);
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
        jComboBox1 = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 102, 102));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24));
        jLabel1.setText("All Orders");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(264, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(202, 202, 202))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 18));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Select Order No.");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jComboBox1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jComboBox1KeyTyped(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Worker:");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product Name", "Quantity"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton4.setText("Delete Design (D)");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Row Material:");

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Material", "Weight", "Tounch", "Quantity"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jButton5.setText("Pick Up (U)");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Cancel Material (X)");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton1.setText("Save Order (Ctrl+S)");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Exit (Ctrl+E)");

        jButton7.setText("Cancel Order (Q)");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("+");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });
        jComboBox2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jComboBox2KeyTyped(evt);
            }
        });

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Design Name:");

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Quantity");

        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField3KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 581, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel3)
                                .addGap(18, 109, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel4)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton6)))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3))
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE))
                .addContainerGap())
        );

        jMenu1.setText("Options");
        jMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu1ActionPerformed(evt);
            }
        });

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Save Order");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem6.setText("Pick Up");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem6);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText("Delete Desing");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setText("Cancel Material");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem5.setText("Cancel Order");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Exit");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

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

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // Exit menu..
        dispose();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // delete desing..
        delelteDesin();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void delelteDesin(){
        int index = jTable1.getSelectedRow();
        if(index<0){
            JOptionPane.showMessageDialog(rootPane, "Please Select Desing to delete");
            return;
        }
        OrderWorder ow = (OrderWorder) jComboBox1.getSelectedItem();
        ProductDesing[] newD = new ProductDesing[ow.od.desings.length-1];
        int newDindex = 0;
        for(int i=0;i<ow.od.desings.length;i++){
            if(i==index)    continue;
            newD[newDindex] = ow.od.desings[i];
            newDindex++;
        }
        ow.od.desings = newD;
        load(ow);
    }

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // pick up
        pickUpRaw();
    }//GEN-LAST:event_jButton5ActionPerformed

    
    private void pickUpRaw(){
        OrderWorder ow = (OrderWorder) jComboBox1.getSelectedItem();
         RawStockPickup rsp = new RawStockPickup(parent, true);
        //rsp.loadRow(""+tm.getValueAt(index, 0),Float.parseFloat(""+tm.getValueAt(index, 1)));
        rsp.loadAll();
        rsp.setVisible(true);
            if(rsp.selected_name!=null){
                if(ow.fm==null){
                    ow.fm = new FinalMaterial[1];
                   
                    ow.fm[0] = new FinalMaterial(rsp.selected_name, rsp.selected_wt, rsp.selected_tnch, rsp.selected_qnty);
 
                }else{
                    //check existing..
                    boolean found = false;
                    FinalMaterial newOne = new FinalMaterial(rsp.selected_name, rsp.selected_wt, rsp.selected_tnch, rsp.selected_qnty);
                    for(FinalMaterial ffmm : ow.fm){
                        if(ffmm.raw_name.equals(newOne.raw_name) && ffmm.wt==newOne.wt && ffmm.tounch==newOne.tounch){
                            ffmm.qnty+=newOne.qnty;
                            found=true;
                        }
                    }
                    if(!found){
                        FinalMaterial[] t_d = new FinalMaterial[ow.fm.length+1];
                        System.arraycopy(ow.fm, 0, t_d, 0, ow.fm.length);
                        t_d[t_d.length-1] = newOne;
                        ow.fm = t_d;
                    }
                }
        }
        load(ow);
    }


    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // Cancle Materail..
        cancelMaterial();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void cancelMaterial(){
        int index = jTable2.getSelectedRow();
        if(index<0){
            JOptionPane.showMessageDialog(rootPane, "Please Select Material to delete");
            return;
        }
        OrderWorder ow = (OrderWorder) jComboBox1.getSelectedItem();
        FinalMaterial[] newFM = new FinalMaterial[ow.fm.length-1];
        int newDindex = 0;
        for(int i=0;i<ow.fm.length;i++){
            if(i==index)    continue;
            newFM[newDindex] = ow.fm[i];
            newDindex++;
        }
        ow.fm=newFM;
        load(ow);
    }

    
    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
        OrderWorder od = ((OrderWorder) jComboBox1.getSelectedItem());

        load(od);
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jComboBox1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBox1KeyTyped
        // TODO add your handling code here:
         OrderWorder od = ((OrderWorder) jComboBox1.getSelectedItem());

        load(od);
    }//GEN-LAST:event_jComboBox1KeyTyped

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        cancleOrder();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // Add new Desing..
        try{
           // setVisible(false);
            new NewDesing(parent).setVisible(true);

            //reaload all desing...
            suggesion();
        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(rootPane, "Error:"+e);
        }
}//GEN-LAST:event_jButton8ActionPerformed

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        // jcombo desing item chanage
       
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jComboBox2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBox2KeyTyped
        // jcombo 1 key
        char key = evt.getKeyChar();
        if(key=='\n'){
            jTextField3.requestFocus();
        }
}//GEN-LAST:event_jComboBox2KeyTyped

    private void jTextField3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyTyped
        // qnty key
        char key = evt.getKeyChar();
        if(key=='\n'){
            addOrder();
            jComboBox1.requestFocus();
        }
}//GEN-LAST:event_jTextField3KeyTyped

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // delete desing menu
        delelteDesin();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        //
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu1ActionPerformed
        // pick up menu..
        
    }//GEN-LAST:event_jMenu1ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // cancel materail menu..
        cancelMaterial();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // cancel order menu..
        cancleOrder();
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // Save Order
        saveOrder();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Save ORder..
            saveOrder();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cancleOrder(){

        OrderWorder od = ((OrderWorder) jComboBox1.getSelectedItem());

        int op = JOptionPane.showConfirmDialog(rootPane, "Do you want Delete?");
          /*
            * 0-Yes
            * 1-No
            * 2-Cacle
            */
          if(op==0)
           {
            try {
                od.cancelOrder();
            } catch (Exception ex) {
                Logger.getLogger(AllOrders.class.getName()).log(Level.SEVERE, null, ex);
            }
           }else if(op==1){

           }else{

           }
    }

    private void saveOrder(){
        OrderWorder od = ((OrderWorder) jComboBox1.getSelectedItem());
        try {
            od.saveTODB();
        } catch (Exception ex) {
           ex.printStackTrace();
           JOptionPane.showMessageDialog(rootPane, ex);
        }
    }
    private void addOrder(){
         int index = jComboBox2.getSelectedIndex();
        String name = desings[index].name;
        int code = desings[index].code;
        String qq = jTextField3.getText();
        int pQnty = 0;
        try{
            pQnty = Integer.parseInt(qq);
        }catch(Exception e){
              JOptionPane.showMessageDialog(rootPane, "Incorrect Value For Quantity", "Error", JOptionPane.ERROR_MESSAGE);
              return;
        }
         

        ProductDesing pd =new ProductDesing(code, name);
        pd.qnty=pQnty;

        OrderWorder od = ((OrderWorder) jComboBox1.getSelectedItem());
        od.od.addNewDesing(pd);
        load(od);
    }

    private void load(OrderWorder od){
        jLabel3.setText("Worker:"+od.wname);
        String[][] data_t1 = new String[od.od.desings.length][];
        for(int i=0;i<data_t1.length;i++){
            data_t1[i] = new String[2];
            data_t1[i][0] = od.od.desings[i].name;
            data_t1[i][1] = ""+od.od.desings[i].qnty;
        }


          jTable1.setModel(new javax.swing.table.DefaultTableModel(data_t1,
            new String [] {
                "Product Name", "Quantity"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });


        String[][] data_t2 = new String[od.fm.length][];
        for(int i=0;i<data_t2.length;i++){
            data_t2[i] = new String[4];
            data_t2[i][0] = od.fm[i].raw_name;
            data_t2[i][1] = ""+od.fm[i].wt;
            data_t2[i][2] = ""+od.fm[i].tounch;
            data_t2[i][3] = ""+od.fm[i].qnty;
        }

          jTable2.setModel(new javax.swing.table.DefaultTableModel(data_t2,
            new String [] {
                "Material", "Weight", "Tounch", "Quantity"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });


    }
    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AllOrders dialog = new AllOrders(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables

}

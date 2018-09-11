/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AllInvoices.java
 *
 * Created on Sep 16, 2015, 9:24:37 AM
 */

package gayakwad.invoices;

import ajgold.AJG_Static_AC;
import gayakwad.AllConstants;
import gayakwad.DBConnection;
import java.awt.Color;
import java.awt.Frame;
import java.sql.ResultSet;
import com.implidea.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import sale.CreditAtSell;
import sale.CustomerObj;

/**
 *
 * @author admin
 */
public class AllInvoices extends javax.swing.JDialog {


    CustomerObj[] co;
    SellInvoice[] allInvoices;
    Frame parent;
    /** Creates new form AllInvoices */
    public AllInvoices(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.parent = parent;
        initComponents();
        java.awt.Dimension dim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, 0);
    }


    public boolean openInvoice(int inv_no) {
        try {
            loadAllAllCust();
            SellInvoice this_si = SellInvoice.loadInvoiceFor(inv_no);
            if(this_si==null){
                return false;
            }
            allInvoices = new SellInvoice[]{this_si};
            //add to drop dwn
            jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(allInvoices));
            jComboBox2.setSelectedIndex(0);
            jComboBox2ItemStateChanged(null);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            if(ex.getMessage().contains("empty result set")){
                JOptionPane.showMessageDialog(parent, "Inoice Number Not Found!");
                dispose();
            }
            else
                JOptionPane.showMessageDialog(parent, ex);

                return false;
        }
    }
    public void loadInvoicesForCustomer(int cid){
        try {

            co = new CustomerObj[1];
            co[0] = CustomerObj.loadCustomerObj(cid);
            allInvoices = SellInvoice.loadAllInvoicesForCust(cid);
            if(allInvoices==null || allInvoices.length<1){
                JOptionPane.showMessageDialog(parent, "No Invoice");
                dispose();
                return;
            }
               //add to drop dwn
            
            jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(co));
            jComboBox1.setSelectedIndex(0);

            jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(allInvoices));
            jComboBox2.setSelectedIndex(0);
            jComboBox2ItemStateChanged(null);

            
        } catch (Exception ex) {
            ex.printStackTrace();;
            JOptionPane.showMessageDialog(parent, ex);
        }
    }

    public void loadAllOpenInvoices(){
        try {
            //load all cust...
            loadAllAllCust();
            allInvoices = SellInvoice.loadAllOpenInvoices();

            //add to drop dwn
            jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(allInvoices));
            jComboBox2.setSelectedIndex(0);
            jComboBox2ItemStateChanged(null);
            jCheckBox1.setSelected(false);
        } catch (Exception ex) {
           ex.printStackTrace();;
           JOptionPane.showMessageDialog(rootPane, ex);
        }
    }



    public void loadAllClosedInvoices(){
        try {
            //load all cust...
            loadAllAllCust();
            allInvoices = SellInvoice.loadAllClosedInvoices();
            System.out.println("closed:"+allInvoices.length);
            //add to drop dwn
            for(SellInvoice si : allInvoices)
                System.out.println(si);
            jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(allInvoices));
            jComboBox2.setSelectedIndex(0);
            jComboBox2ItemStateChanged(null);
            jCheckBox1.setSelected(true);
        } catch (Exception ex) {
           ex.printStackTrace();;
           JOptionPane.showMessageDialog(rootPane, ex);
        }
    }



    private void loadAllAllCust() throws Exception
    {
         DBConnection con = new DBConnection();
            ResultSet rs = con.executeQuery("SELECT count(*) FROM customer_mst;");
            rs.next();
            int count = rs.getInt(1);
            co = new CustomerObj[count];
            rs = con.executeQuery("SELECT * FROM customer_mst;");
            int i=0;
            while(rs.next()){
                int cid = rs.getInt(1);
                String name = rs.getString(2);
                String address = rs.getString(3);
                String contact = rs.getString(4);
                float base_tounch = rs.getFloat(5);
                float base_labor = rs.getFloat(6);
                float gold_ac = rs.getFloat(7);
                float money_ac = rs.getFloat(8);
                co[i] = new CustomerObj(cid, name, address, contact, base_tounch, base_labor, gold_ac, money_ac);
                i++;
            }

            jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(co));
            jComboBox1.setSelectedIndex(0);
           // jComboBox1ItemStateChanged(null);
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
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 102, 102));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24));
        jLabel1.setText("****Invoices****");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(218, 218, 218)
                .addComponent(jLabel1)
                .addContainerGap(234, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(255, 102, 102));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Customer Name:");

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

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Invoice No.");

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

        jButton1.setText("Reload (Ctrl+R)");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jCheckBox1.setBackground(new java.awt.Color(255, 102, 102));
        jCheckBox1.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jCheckBox1.setForeground(new java.awt.Color(255, 255, 255));
        jCheckBox1.setText("Closed Invoices");
        jCheckBox1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jCheckBox1MouseClicked(evt);
            }
        });
        jCheckBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox1ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(89, 89, 89)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addGap(18, 18, 18)
                        .addComponent(jCheckBox1)
                        .addContainerGap(130, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel12)
                    .addComponent(jCheckBox1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Code", "Name", "Weight", "Tounch", "Labor", "Quantity"
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
        jScrollPane1.setViewportView(jTable1);

        jButton2.setText("Print (Ctrl+P)");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Exit  (Ctrl+E)");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setBackground(new java.awt.Color(0, 0, 0));
        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jLabel3.setText("Total Fine:");

        jLabel4.setBackground(new java.awt.Color(0, 0, 0));
        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jLabel4.setText("Total Labor:");

        jLabel5.setBackground(new java.awt.Color(0, 0, 0));
        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jLabel5.setText("Jama Fine:");

        jLabel6.setBackground(new java.awt.Color(0, 0, 0));
        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jLabel6.setText("Jama Labor:");

        jLabel9.setBackground(new java.awt.Color(0, 0, 0));
        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jLabel9.setText("Balance Fine:");

        jLabel10.setBackground(new java.awt.Color(0, 0, 0));
        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jLabel10.setText("Balance Labor:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 227, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel10))
                .addGap(171, 171, 171))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(jLabel6)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jButton4.setText("Payment (Ctrl+F)");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Close Invoice (Ctrl+G)");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Opening Date:");

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Closing Date:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 414, Short.MAX_VALUE)
                        .addComponent(jLabel8)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE))
                .addContainerGap())
        );

        jMenu1.setText("Options");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Reload");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Print");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setText("Payment");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem5.setText("Close Invoice");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

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
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // c name combo item
      //  System.out.println("Invoice_event:"+invoice_Event);
        if(invoice_Event>0)  {
            invoice_Event--;
            return;
        }
        CustomerObj cust_obj = (CustomerObj) jComboBox1.getSelectedItem();

        //load only invoices
        Vector<SellInvoice> thisCustSI = new Vector<SellInvoice>();
        for(SellInvoice si : allInvoices)
            if(si.cid==cust_obj.cid){
                thisCustSI.add(si);
                si.cust_obj=cust_obj;
            }
        int count = thisCustSI.size();
        if(count<1){
          jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"--"}));
          jComboBox2.setSelectedIndex(0);
          refreshTable(null);
          return;
        }
        SellInvoice[] thisCustSi = new SellInvoice[count];
        for(int i=0;i<count;i++){
            thisCustSi[i] = thisCustSI.get(i);
        }
        
        
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(thisCustSi));
        jComboBox2.setSelectedIndex(0);
        refreshTable(thisCustSi[0]);

        
}//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jComboBox1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBox1KeyTyped
        // cname combo
         if(evt.getKeyChar()!='\n')  return;

         jComboBox2.requestFocus();
         

    }//GEN-LAST:event_jComboBox1KeyTyped

    
    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        // TODO add your handling code here:
        custForInvoNo();
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jComboBox2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBox2KeyTyped
        // TODO add your handling code here:
        if(evt.getKeyChar()!='\n')  return;
        custForInvoNo();
    }//GEN-LAST:event_jComboBox2KeyTyped

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // Reload menu
        reload();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Realod btn.
        reload();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // Print btn
        print((SellInvoice) jComboBox2.getSelectedItem(),(CustomerObj) jComboBox1.getSelectedItem());
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Print btn
        print((SellInvoice) jComboBox2.getSelectedItem(),(CustomerObj) jComboBox1.getSelectedItem());
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // Exit menu
        exit();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Exit Btn
        exit();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // payment btn
        customer_payament((SellInvoice) jComboBox2.getSelectedItem());
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        //  payment menu
        customer_payament((SellInvoice) jComboBox2.getSelectedItem());
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // close invoice
        closeInvoice((SellInvoice) jComboBox2.getSelectedItem());
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // Close invoice
        closeInvoice((SellInvoice) jComboBox2.getSelectedItem());
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jCheckBox1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckBox1MouseClicked
        // TODO add your handling code here:

        //reloadCheck();
    }//GEN-LAST:event_jCheckBox1MouseClicked

    private void jCheckBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox1ItemStateChanged
        // TODO add your handling code here:
        reloadCheck();
    }//GEN-LAST:event_jCheckBox1ItemStateChanged

    private void reloadCheck(){
        if(jCheckBox1.isSelected()){
            //load closed..!
         //   jPanel1.setVisible(false);
            loadAllClosedInvoices();
           // jPanel1.setVisible(true);
        }
         else{
                //load open
            jPanel1.setVisible(false);
            loadAllOpenInvoices();
            jPanel1.setVisible(true);
         }
    }
    
    private void closeInvoice(SellInvoice si){
       if(si.is_closed){
           //open...
           si.openInvoice(jPanel1);
             si.closedDate= null;
           si.is_closed = false;
           System.out.println("----------Inoice Open----");
        }
       else{
            //Close...
           si.closeInvoice(jPanel1);
           si.closedDate=new Date();
           si.is_closed = true;
           System.out.println("----------Inoice Closed----");
        }
       jComboBox2.invalidate();
       jComboBox2.invalidate();
       jComboBox2.invalidate();
       jComboBox2.invalidate();
       //jComboBox2.repaint();
       refreshTable(si);
    }

    private void customer_payament(SellInvoice si){
         //credit...
           float gold_credit = 0;
           float money_credit = 0;
           String conversion_comment_G = "";
           String conversion_comment_M = "";

                //CreditAtSell dialog = new CreditAtSell(new javax.swing.JFrame(), true,si.cust_obj,si.getTotalFine(),si.totalLabor());
                CreditAtSell dialog = new CreditAtSell(new javax.swing.JFrame(), true,si.cust_obj,0,0);
                dialog.setVisible(true);
                if(dialog.FINISHED){
                    gold_credit =(dialog.total_fine_actual_recv);
                    money_credit = (dialog.total_cash_actual_recv);
                    System.out.println("["+(money_credit)+"]["+gold_credit+"]");
                    System.out.println("["+(dialog.total_cash_converted)+"]["+dialog.total_fine_converted+"]");
                    System.out.println("["+(dialog.total_G_TO_M)+"]["+dialog.total_M_TO_G+"]");
                    System.out.println("["+(si.cust_obj.money_ac)+"]["+si.cust_obj.gold_ac+"]");

                    /*
                     * M      G
                    [17500.0][7.0]
                    [5000.0][3.0]
                    [7500.0][2.0]
                    [-5050.0][-5.54]
                    3.0 gm Fine Convert-> +Rs.7500.0/- Jama
                    Rs.5000.0/- Convert-> 2.0 gm fine Jama
                     */

                    if(dialog.total_G_TO_M>0)
                        conversion_comment_G=""+dialog.total_fine_converted+" gm Fine Convert-> Rs."+dialog.total_G_TO_M+"/- Jama";
                    if(dialog.total_M_TO_G>0)
                        conversion_comment_M="Rs."+dialog.total_cash_converted+"/- Convert-> "+dialog.total_M_TO_G+" gm fine Jama";

                    System.out.println(conversion_comment_G);
                    System.out.println(conversion_comment_M);

                    //if(cust_obj!=null)
                     //   return;
                }else{
                    return;
                }


          





        try{
          int invoice_index = getIndexOfInvoice(si);
           allInvoices[invoice_index] = SellInvoice.loadInvoiceFor(si.invoice_no);
           allInvoices[invoice_index].cust_obj=si.cust_obj;
           refreshTable(allInvoices[invoice_index]);
            CustomerObj cust_obj = si.cust_obj;
            DBConnection con = new DBConnection();
            //con.executeUpdate("update customer_mst set gold_ac=gold_ac-"+total_fine+", money_ac=money_ac-"+total_labor+" where cid="+cid);
            con.executeUpdate("update customer_mst set gold_ac="+si.cust_obj.gold_ac+", money_ac="+si.cust_obj.money_ac+" where cid="+si.cust_obj.cid);
            //update trasacaions..
                if(gold_credit!=0){
                    String qry = "insert into aj_transations(name,tran_type,amount,dt,invoice_no,comment) values('"+cust_obj.name+"','GOLD',"+gold_credit+",now(),"+si.invoice_no+",'At Invoice Time "+conversion_comment_M+"');";
                    //System.out.println(qry);
                    con.executeUpdate(qry);
               }

                if(money_credit!=0){
                    String qry = "insert into aj_transations(name,tran_type,amount,dt,invoice_no,comment) values('"+cust_obj.name+"','MONEY',"+money_credit+",now(),"+si.invoice_no+",'At Invoice Time "+conversion_comment_G+"');";
                  //  System.out.println(qry);
                    con.executeUpdate(qry);
               }
            //update in hand..

            {
               AJG_Static_AC.updateHand(money_credit, gold_credit);
            }
        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(rootPane, e);
        }

    }
    private void exit(){
        dispose();
    }


    private int getIndexOfInvoice(SellInvoice si){
        int invoice_index = -1;
          for(int i=0;i<allInvoices.length;i++){
               if(si.equals(allInvoices[i])){
                   invoice_index=i;
                   break;
               }
           }
        return invoice_index;
    }
    public static void print(SellInvoice si,CustomerObj cust_obj){
        try {
            if(si.convertType==1)
                System.out.println("["+si.amountG+"]gm Gold To Rs.["+si.amountM+"]: Rate:"+df_money.format(si.getConversionRate()));
            else
                System.out.println("Rs.["+si.amountM+"] Money To Gold ["+si.amountM+"]gm Rate:"+df_money.format(si.getConversionRate()));
            // = (CustomerObj) jComboBox1.getSelectedItem();
            HashMap hm = new HashMap();
            hm.put("cname", si.cname);
            System.out.println("["+si.invoice_no+"]");
            hm.put("invoice_no",""+ si.invoice_no);
            hm.put("t_g", df_gold.format(si.getTotalFine()));
            hm.put("t_p", df_money.format(si.totalLabor()));
            hm.put("d_g", df_gold.format(si.old_gold*(-1)));
            hm.put("d_p", df_money.format(si.old_money*(-1)));
            hm.put("p_g", "-"+df_gold.format(si.jamaGold));
            hm.put("p_p", "-"+df_money.format(si.jamaMoney));
            hm.put("b_g", df_gold.format(cust_obj.gold_ac*(-1)));
            hm.put("b_p", df_money.format(cust_obj.money_ac*(-1)));
            if(si.amountG>0){
                if(si.convertType==1)//GTM
                {
                    hm.put("c_g", "-"+df_gold.format(si.amountG)+"x"+df_money.format(si.getConversionRate()));
                    hm.put("c_m","+"+ df_money.format(si.amountM));
                }
                else{
                    hm.put("c_g","+"+ df_gold.format(si.amountG));
                    hm.put("c_m", "-"+df_money.format(si.amountM)+"/"+df_money.format(si.getConversionRate()));

                 }
            }else{
                hm.put("c_g","0");
                hm.put("c_m", "0");

            }
            // Generate jasper print
            JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(AllConstants.jasperFileName, hm, new DBConnection().getConnection());
            //JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(jasperFileName, hm);
            // Export pdf file
            //JasperExportManager.exportReportToPdfFile(jprint, pdfFileName);
            JasperPrintManager.printPages(jprint, 0, jprint.getPages().size()-1, true);
          //  dispose();
            System.out.println("Printing!");
           //  JasperViewer.viewReport(jprint);
             System.out.println("Done!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(new Frame(), ex);
            //Logger.getLogger(AllInvoices.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    private static DecimalFormat df_gold = new DecimalFormat("########.###");
    private static DecimalFormat df_money = new DecimalFormat("########");
    
    private void refreshTable(SellInvoice si){
//        if(si==null){
//
//                    jTable1.setModel(new javax.swing.table.DefaultTableModel(new String[0][0],
//                        new String [] {
//                            "Code", "Name", "Weight", "Tounch", "Labor", "Quantity"
//                        }
//                    ) {
//                        Class[] types = new Class [] {
//                            java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
//                        };
//                        boolean[] canEdit = new boolean [] {
//                            false, false, false, false, false, false
//                        };
//
//                        public Class getColumnClass(int columnIndex) {
//                            return types [columnIndex];
//                        }
//
//                        public boolean isCellEditable(int rowIndex, int columnIndex) {
//                            return canEdit [columnIndex];
//                        }
//                    });
//            return;
//        }

        
        CustomerObj cust_obj = si.cust_obj;
        String[][] data = new String[0][0];
        if(si!=null){
            data = si.getDataForTable();
            if(si.open_date!=null)
                jLabel7.setText("Opening Date:  "+((si.open_date.getDate()<10 ? "0"+si.open_date.getDate() : ""+si.open_date.getDate())+"-"+(si.open_date.getMonth()+1)+"-"+(si.open_date.getYear()+1900)));
            else
                jLabel7.setText("Opening Date Not Available");

            if(si.closedDate!=null){
                jLabel8.setText("Closing Date:  "+((si.closedDate.getDate()<10 ? "0"+si.closedDate.getDate() : ""+si.closedDate.getDate())+"-"+(si.closedDate.getMonth()+1)+"-"+(si.closedDate.getYear()+1900)));
                jLabel8.setVisible(true);
            }
            else
                jLabel8.setVisible(false);



            jLabel3.setText("Total Fine: "+df_gold.format(si.getTotalFine()));
            jLabel4.setText("Total Labor: "+df_money.format(si.totalLabor()));

            jLabel5.setText("Jama Fine: "+df_gold.format(si.jamaGold));
            jLabel6.setText("Jama Money :"+df_money.format(si.jamaMoney));


            float gold_bal = cust_obj.gold_ac;
            float money_bal = cust_obj.money_ac;

            jLabel9.setText("Balance Fine: "+df_gold.format(Math.abs(gold_bal)));
            jLabel10.setText("Balance Money: "+df_money.format(Math.abs(money_bal)));
            if(gold_bal<0)
                jLabel9.setForeground(Color.red);
            else
               jLabel9.setForeground(Color.black);

            if(money_bal<0)
                jLabel10.setForeground(Color.red);
            else
               jLabel10.setForeground(Color.black);

            if(si.is_closed)
                jButton5.setText("Open Invoice (Ctrl+G)");
            else
                jButton5.setText("Close Invoice (Ctrl+G)");

            

       }else{
            jLabel3.setText("Total Fine: ");
            jLabel4.setText("Total Labor: ");

            jLabel5.setText("Jama Fine: ");
            jLabel6.setText("Jama Money :");


            float gold_bal = cust_obj.gold_ac;
            float money_bal = cust_obj.money_ac;
 
            jLabel9.setText("Balance Fine: "+df_gold.format(Math.abs(gold_bal)));
            jLabel10.setText("Balance Money: "+df_money.format(Math.abs(money_bal)));
            if(gold_bal<0)
                jLabel9.setForeground(Color.red);
            else
               jLabel9.setForeground(Color.black);

            if(money_bal<0)
                jLabel10.setForeground(Color.red);
            else
               jLabel10.setForeground(Color.black);
       }

        

        
        jTable1.setModel(new javax.swing.table.DefaultTableModel(data,
            new String [] {
                "Code", "Name", "Weight", "Tounch", "Labor", "Quantity"
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
    }
    

    int invoice_Event = 0;
    private void custForInvoNo(){
          

        //load only cust
        int selectIndex = 0;
        try{
            SellInvoice si = (SellInvoice) jComboBox2.getSelectedItem();
            if(si==null)    return;
            for(selectIndex=0;selectIndex<co.length;selectIndex++){
                if(co[selectIndex].cid==si.cid){
                    si.cust_obj=co[selectIndex];
                    break;
                }
            }
            refreshTable(si);
            invoice_Event = 2;
            jComboBox1.setSelectedIndex(selectIndex);
         

        }catch(Exception e){
            e.printStackTrace();
            return;
        }
    }

    private void reload()
    {
       // setVisible(false);
       // new AllInvoices(parent, true).setVisible(true);
            jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(co));
            jComboBox1.setSelectedIndex(0);
            jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(allInvoices));
            jComboBox2.setSelectedIndex(0);
    }
        /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AllInvoices dialog = new AllInvoices(new javax.swing.JFrame(), true);
                //dialog.loadInvoicesForCustomer(97);
                dialog.openInvoice(83);
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
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

}

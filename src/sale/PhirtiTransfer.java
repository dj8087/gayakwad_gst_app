/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PhirtiTransfer.java
 *
 * Created on Sep 22, 2015, 8:02:24 AM
 */

package sale;

import gayakwad.DBConnection;
import java.awt.Color;
import java.awt.Rectangle;
import java.sql.ResultSet;
import com.implidea.DecimalFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import sale.RawSell.RawSellItem;
import stock.*;

/**
 *
 * @author admin
 */

class TranferJama{
    public final String cust_name;
    public final float gold,labor;
    public final int thusi_75,thusi_92;
    private static DecimalFormat df_gold = new DecimalFormat("######.###");
    private static DecimalFormat df_money = new DecimalFormat("#########.#");
    
    public TranferJama(String cust_name, float gold, float labor, int thusi_75, int thusi_92) {
        this.cust_name = cust_name;
        this.gold = gold;
        this.labor = labor;
        this.thusi_75 = thusi_75;
        this.thusi_92 = thusi_92;
    }

    public String[] getData(){
        return new String[]{cust_name,""+thusi_75,""+thusi_92,df_gold.format(gold),df_money.format(labor)};
    }

    public String toString(){
        return cust_name+"::"+thusi_75+"::"+thusi_92+"::"+df_gold.format(gold)+"::"+df_money.format(labor);
    }

    
}
public class PhirtiTransfer extends javax.swing.JDialog {

    public static final String PHIRTI = "Phirti";
    public static final int PHIRTI_CID = 62;//62;
    public static final int COMMISION_CID = 75;//rasul;
    CustomerObj[] co;
    CustomerObj phirti_co;
    JFrame parent;

    private Vector<TranferJama> all_tranfer_jama = new Vector<TranferJama>();
    /** Creates new form PhirtiTransfer */
    public PhirtiTransfer(JFrame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        java.awt.Dimension dim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(new Rectangle(dim.width, getHeight()));
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getHeight()/2);

        initAll();
    }

    private  void initAll(){
        try {
            stock = new Stock();
            transfered = new Stock();
            stock_to_cust = new Stock();
            jComboBox1.setEnabled(true);
           // jComboBox1.setSelectedIndex(0);
            loadAllAllCust();
            loadPhirtiSellDetails();
            refreshPhirtiSellTable();
            refreshCustomerSellTable();
            jComboBox1.requestFocus();
            jComboBox1ItemStateChanged(null);
        } catch (Exception ex) {
            ex.printStackTrace();;
            JOptionPane.showMessageDialog(rootPane, ex);
        }
    }
    private Stock stock,transfered;
    private Stock stock_to_cust;
    private void loadPhirtiSellDetails() throws Exception{
        DBConnection con = new DBConnection();
        ResultSet rs = con.executeQuery("SELECT barcode,name,ssm.wt,details,tounch,labor FROM selled_stock_mst ssm join sell_not_closed_invoices si where si.cid="+PHIRTI_CID+" and code=barcode order by barcode;");
        while(rs.next()){
            Product p = new Product(rs.getString(1), rs.getString(2), rs.getFloat(3), rs.getString(4));
            p.tounch = rs.getFloat(5);
            p.labor = rs.getFloat(6);
            stock.addProduct(p);
        }
        
    }
    DecimalFormat df_gold = new DecimalFormat("########.###");
    DecimalFormat df_money = new DecimalFormat("########");

    private void refreshPhirtiSellTable(){
        int count = stock.stock.size();
        String[][] data = new String[count][];
        float totalWt = 0,totalFine=0,totalLabor=0;
        for(int i=0;i<count;i++){
            Product p = stock.stock.get(i);
            data[i] = new String[3];
            data[i][0] = p.barcode;
            data[i][1] = p.name;
            data[i][2] = df_gold.format(p.wt);
            totalWt+=p.wt;
            totalFine+=(p.wt*p.tounch/100);
            totalLabor+=p.labor;
        }

        jLabel6.setText("Total Wt: "+df_gold.format(totalWt)+"       Total Fine: "+df_gold.format(totalFine));
        jLabel7.setText("Total Labor: "+totalLabor);

        jLabel10.setText("Balance Fine: "+Math.abs(phirti_co.gold_ac));
        jLabel11.setText("Balance Labor: "+Math.abs(phirti_co.money_ac));

        if(phirti_co.gold_ac<0)
            jLabel10.setForeground(Color.red);
        else
            jLabel10.setForeground(Color.BLACK);

        if(phirti_co.money_ac<0)
            jLabel11.setForeground(Color.red);
        else
            jLabel11.setForeground(Color.BLACK);


        jTable1.setModel(new javax.swing.table.DefaultTableModel(data,
            new String [] {
                "Code", "Name", "Weight"
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
        
    }

    TranferJama currentTJ;
    private void refreshCustomerSellTable(){
        currentTJ=null;
        int count = stock_to_cust.stock.size();
        if(count==1)
            jComboBox1.setEnabled(false);
        CustomerObj current = (CustomerObj) jComboBox1.getSelectedItem();
        String[][] data = new String[count][];
        total_wt = 0;
        total_fine=0;
        total_labor=0;

        int t75=0,t92=0;
        for(int i=0;i<count;i++){
            Product p = stock_to_cust.stock.get(count-1-i);
            data[i] = new String[7];
            data[i][0] = ""+(count-i);
            data[i][1] = p.barcode;
            data[i][2] = p.name;
            if(p.barcode.equals("--")){
                data[i][3] = df_gold.format(p.wt*p.qnty_if_raw/1000);
                data[i][4] = df_gold.format(p.tounch);
                data[i][5] = df_gold.format((p.wt/1000)*p.qnty_if_raw*p.tounch/100);
                data[i][6] = df_gold.format(p.labor*p.qnty_if_raw);
                total_wt+=p.wt*p.qnty_if_raw/1000;
                total_fine+=((p.wt/1000)*p.tounch*p.qnty_if_raw/100);
                total_labor+=p.labor*p.qnty_if_raw;

            }else{

                data[i][3] = df_gold.format(p.wt);
                data[i][4] = df_gold.format(p.tounch);
                data[i][5] = df_gold.format(p.wt*p.tounch/100);
                data[i][6] = df_gold.format(p.labor);

                total_wt+=p.wt;
                total_fine+=(p.wt*p.tounch/100);
                total_labor+=p.labor;
            }
            if(p.name.contains("-92"))
                t92++;
            else
                t75++;
        }

        currentTJ = new TranferJama(current.name, total_wt, total_labor, t75, t92);
        jLabel8.setText("Total Wt: "+df_gold.format(total_wt)+"     Total Fine: "+df_gold.format(total_fine));
        jLabel9.setText("Total Labor: "+total_labor);

        jLabel14.setText("Balance Fine: "+Math.abs(current.gold_ac));
        jLabel15.setText("Balance Labor: "+Math.abs(current.money_ac));

        if(phirti_co.gold_ac<0)
            jLabel14.setForeground(Color.red);
        else
            jLabel14.setForeground(Color.BLACK);

        if(phirti_co.money_ac<0)
            jLabel15.setForeground(Color.red);
        else
            jLabel15.setForeground(Color.BLACK);


        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            data,
            new String [] {
                "No.","Code", "Name", "Weight", "Tounch", "Fine", "Labor"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class,java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false,false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });

    }
    
    private void loadAllAllCust() throws Exception
    {
         DBConnection con = new DBConnection();
            ResultSet rs = con.executeQuery("SELECT count(*) FROM customer_mst;");
            rs.next();
            int count = rs.getInt(1);
            co = new CustomerObj[count-1];  //one for phirti
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
                if(cid==PHIRTI_CID){
                    phirti_co= new CustomerObj(cid, name, address, contact, base_tounch, base_labor, gold_ac, money_ac);
                }else{
                    co[i] = new CustomerObj(cid, name, address, contact, base_tounch, base_labor, gold_ac, money_ac);
                    i++;
                }
            }

            jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(co));
            jComboBox1.setSelectedIndex(0);
            jComboBox1ItemStateChanged(null);
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
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 102, 102));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Code", "Name", "Weight"
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

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24));
        jLabel1.setText("Phirti Sell Transfer");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(404, 404, 404)
                .addComponent(jLabel1)
                .addContainerGap(496, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 24));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Invoice No:");

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

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Customer :");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Base Tounch");

        jTextField1.setText("0.0");
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField1KeyTyped(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Base Labor");

        jTextField2.setText("0.0");
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField2KeyTyped(evt);
            }
        });

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No.", "Code", "Name", "Weight", "Tounch", "Fine", "Labor"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setBackground(new java.awt.Color(0, 0, 0));
        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jLabel6.setText("Total Fine:");

        jLabel7.setBackground(new java.awt.Color(0, 0, 0));
        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jLabel7.setText("Total Labor:");

        jLabel10.setBackground(new java.awt.Color(0, 0, 0));
        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jLabel10.setText("Balance Fine:");

        jLabel11.setBackground(new java.awt.Color(0, 0, 0));
        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jLabel11.setText("Balance Labor:");

        jButton3.setText("Total Jama");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 102, Short.MAX_VALUE)
                        .addComponent(jButton3))
                    .addComponent(jLabel10))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addGap(13, 13, 13)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setBackground(new java.awt.Color(0, 0, 0));
        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jLabel8.setText("Total Fine:");

        jLabel9.setBackground(new java.awt.Color(0, 0, 0));
        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jLabel9.setText("Total Labor:");

        jLabel14.setBackground(new java.awt.Color(0, 0, 0));
        jLabel14.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jLabel14.setText("Balance Fine:");

        jLabel15.setBackground(new java.awt.Color(0, 0, 0));
        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jLabel15.setText("Balance Labor:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15))
                .addContainerGap(314, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(jLabel15)
                .addContainerGap())
        );

        jButton2.setText("Finish (Ctrl+F)");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton1.setText("Reset (Ctrl+R)");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
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
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(jLabel4))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jLabel5)
                                                .addComponent(jTextField1)
                                                .addComponent(jTextField2))
                                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE))))))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(77, 77, 77))
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 657, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(92, 92, 92)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
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
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel5)
                        .addGap(11, 11, 11)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jMenu3.setText("Options");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Finish");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Reset");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setText("Delete");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem4);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText("Exit");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem3);

        jMenuBar1.add(jMenu3);

        jMenu1.setText("Sell Type");

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem5.setText("Raw Material");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem6.setText("Bin Laki");
        jMenu1.add(jMenuItem6);

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
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // c name combo item
        CustomerObj cust_obj = (CustomerObj) jComboBox1.getSelectedItem();
        jTextField1.setText(""+cust_obj.base_tounch);
        jTextField2.setText(""+cust_obj.base_labor);

         jLabel14.setText("Balance Fine: "+Math.abs(cust_obj.gold_ac));
        jLabel15.setText("Balance Labor: "+Math.abs(cust_obj.money_ac));

        if(cust_obj.gold_ac<0)
            jLabel14.setForeground(Color.red);
        else
            jLabel14.setForeground(Color.BLACK);

        if(cust_obj.money_ac<0)
            jLabel15.setForeground(Color.red);
        else
            jLabel15.setForeground(Color.BLACK);

}//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jComboBox1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBox1KeyTyped
        // c name key
        
        CustomerObj cust_obj = (CustomerObj) jComboBox1.getSelectedItem();
        jTextField1.setText(""+cust_obj.base_tounch);
        jTextField2.setText(""+cust_obj.base_labor);

        jLabel14.setText("Balance Fine: "+Math.abs(cust_obj.gold_ac));
        jLabel15.setText("Balance Labor: "+Math.abs(cust_obj.money_ac));

        if(cust_obj.gold_ac<0)
            jLabel14.setForeground(Color.red);
        else
            jLabel14.setForeground(Color.BLACK);

        if(cust_obj.money_ac<0)
            jLabel15.setForeground(Color.red);
        else
            jLabel15.setForeground(Color.BLACK);

        if(evt.getKeyChar()=='\n')
            jTable1.requestFocus();
    }//GEN-LAST:event_jComboBox1KeyTyped

    private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped
        // tounch
        if(evt.getKeyChar()!='\n')  return;
        jTextField2.requestFocus();
    }//GEN-LAST:event_jTextField1KeyTyped

    private void jTextField2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyTyped
        // labor
        
}//GEN-LAST:event_jTextField2KeyTyped

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // T1 mouse click..
        if(evt.getClickCount()==2){
            int index = jTable1.getSelectedRow();
            Product p = stock.stock.remove(index);
            transfered.addProduct(p);
            Product p2 = p.clone();
            try{
            p2.tounch = Float.parseFloat(jTextField1.getText());
            }catch(Exception e){
                JOptionPane.showMessageDialog(rootPane, "Wrong Entry For Tounch");
                return;
            }
            try{
            p2.labor = Float.parseFloat(jTextField2.getText());
            }catch(Exception e){
                JOptionPane.showMessageDialog(rootPane, "Wrong Entry For Labor");
                return;
            }
            stock_to_cust.addProduct(p2);
            refreshPhirtiSellTable();
            refreshCustomerSellTable();
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        try {
            // Finish Menu
            save();
        } catch (Exception ex) {
            Logger.getLogger(PhirtiTransfer.class.getName()).log(Level.SEVERE, null, ex);
        }
}//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // Reset Menu
        reset();
}//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // Delete Entry..
        int rowIndex = jTable2.getSelectedRow();
        if(rowIndex<0){
            JOptionPane.showMessageDialog(parent, "Select Entry From Customer Table!");
            return;
        }
        int index = stock_to_cust.stock.size()-1-rowIndex;
        Product pp = stock_to_cust.stock.remove(index);
        for(Product p2 : transfered.stock)
            if(p2.barcode.equals(pp.barcode)){
                stock.addProduct(p2);
                transfered.stock.remove(p2);
                break;
            }
        refreshCustomerSellTable();
        refreshPhirtiSellTable();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // exit
        dispose();
//        parent.setVisible(true);
}//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            // Finish Btn
            if(stock_to_cust.stock.size()<1){
                JOptionPane.showMessageDialog(parent, "No Sell Transfer!");
                return;
            }
            save();
        } catch (Exception ex) {
            Logger.getLogger(PhirtiTransfer.class.getName()).log(Level.SEVERE, null, ex);
        }
}//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Reset Btn
        reset();
}//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Total Jama
        int size = all_tranfer_jama.size();
        if(size<1){
            JOptionPane.showMessageDialog(parent, "No Transfer!");
            return;
        }
        float totalG=0,totlaL=0;
        String[][] data = new String[size][];
        for(int i=0;i<size;i++){
            TranferJama tf = all_tranfer_jama.get(i);
            data[i] = tf.getData();
            totalG+=tf.gold;
            totlaL+=tf.labor;
        }
        PhirtiTranferTotalJama pttj = new PhirtiTranferTotalJama(parent, true);
        pttj.setData(data, df_gold.format(totalG),df_money.format(totlaL));
        pttj.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // Raw Materila
        CustomerObj cust = (CustomerObj) jComboBox1.getSelectedItem();
        PhirtiRawSell prs = new PhirtiRawSell(parent, true);
        prs.setCustomerObj(cust);
        prs.setVisible(true);
        Vector<RawSellItem> rawSell = prs.rawSell;
        
        for (RawSellItem rawSellItem : rawSell) {
            System.out.println(rawSellItem);

            {
                Product pp = new Product("--", rawSellItem.name, rawSellItem.wt, "RAW_NA");
                pp.qnty_if_raw = rawSellItem.qnty;
                pp.tounch = rawSellItem.tounch;
                pp.originalItm=rawSellItem.original;
                pp.labor = rawSellItem.labor;
                stock_to_cust.addProduct(pp);
                                        
            }
            {
//                Product pp = new Product("--", rawSellItem.original.name, rawSellItem.original.wt, "RAW_NA");
//                pp.qnty_if_raw = rawSellItem.original.qnty;
//                pp.tounch = rawSellItem.original.tounch;
//                stock.addProduct(pp);
            }

        }
        refreshCustomerSellTable();
        refreshPhirtiSellTable();
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private float last_g_bal=0,last_m_bal=0;
    float total_wt=0,total_fine=0,total_labor=0;
    private void save() throws Exception{


        if(currentTJ==null) return;

        
        DBConnection con = new DBConnection();
        CustomerObj cust_obj = (CustomerObj) jComboBox1.getSelectedItem();
        int invoice_no = 0;
        int row_id = 0;

         row_id = 0;
        //SELECT max(invoice_no) as ex FROM sell_invoice;
           ResultSet rs = con.executeQuery("select * from next_invoice_no;");
            if(rs.next()){
                invoice_no = rs.getInt(1);
            }else{
               invoice_no=0;
            }
           //invoice_no++;
           row_id++;

        float gold_credit = 0;
           float money_credit = 0;
           String conversion_comment_G = "";
           String conversion_comment_M = "";
                last_g_bal = cust_obj.gold_ac;
                last_m_bal = cust_obj.money_ac;
                CreditAtSell dialog = new CreditAtSell(parent, true,cust_obj,total_fine,total_labor);
                dialog.setVisible(true);
                if(dialog.FINISHED){
                    gold_credit =(dialog.total_fine_actual_recv);
                    money_credit = (dialog.total_cash_actual_recv);
                    System.out.println("["+(money_credit)+"]["+gold_credit+"]");
                    System.out.println("["+(dialog.total_cash_converted)+"]["+dialog.total_fine_converted+"]");
                    System.out.println("["+(dialog.total_G_TO_M)+"]["+dialog.total_M_TO_G+"]");
                    System.out.println("["+(cust_obj.money_ac)+"]["+cust_obj.gold_ac+"]");

                    /*
                     * M      G
                    [17500.0][7.0]
                    [5000.0][3.0]
                    [7500.0][2.0]
                    [-5050.0][-5.54]
                    3.0 gm Fine Convert-> +Rs.7500.0/- Jama
                    Rs.5000.0/- Convert-> 2.0 gm fine Jama
                     *
                     *
                     * [M1][G1]
                     * [M2][G2]
                     * [M3][G4]
                     * [Acc_M][Acc_G]
                     *
                     * Account payment=> M1+M2-M3
                     * Account payment=> G1+G2-G3
                     * 
                     *
                     */

                    if(dialog.total_G_TO_M>0)
                        conversion_comment_G=""+dialog.total_fine_converted+" gm Fine Convert-> Rs."+dialog.total_G_TO_M+"/- Jama";
                    if(dialog.total_M_TO_G>0)
                        conversion_comment_M="Rs."+dialog.total_cash_converted+"/- Convert-> "+dialog.total_M_TO_G+" gm fine Jama";

                    System.out.println(conversion_comment_G);
                    System.out.println(conversion_comment_M);

                    
                    float gold_acc_paid = gold_credit+dialog.total_fine_converted-dialog.total_M_TO_G;
                    float money_acc_paid = money_credit+dialog.total_cash_converted-dialog.total_G_TO_M;
                    phirti_co.gold_ac = total_fine+phirti_co.gold_ac;
                    phirti_co.money_ac=total_labor+phirti_co.money_ac;

                    //check for commision...

                    PhirtiCommisionDetails pcd = new PhirtiCommisionDetails(parent, true);
                    pcd.jLabel3.setText("Wt:"+df_gold.format(total_wt)+"");
                    pcd.jLabel4.setText("Fine:"+df_gold.format(total_fine)+"");
                    pcd.jLabel5.setText("Labor:"+df_money.format(total_labor)+"");
                    pcd.setVisible(true);
                    if(pcd.commision_fin<0){
                        JOptionPane.showMessageDialog(rootPane, "Tranfer Cancel!");
                        return;
                    }
                    phirti_co.gold_ac = phirti_co.gold_ac-pcd.commision_fin;
                    phirti_co.money_ac=phirti_co.money_ac-pcd.commision_rs;

                   //add commision..
                    if(pcd.commision_fin!=0 || pcd.commision_rs!=0){
                        con.executeUpdate("update customer_mst set gold_ac=gold_ac+"+pcd.commision_fin+", money_ac=money_ac+"+pcd.commision_rs+" where cid="+pcd.selected_cust_obj.cid);
                           if(pcd.commision_fin!=0){
                              String qry = "insert into aj_transations(name,tran_type,amount,dt,invoice_no,comment) values('"+pcd.selected_cust_obj.name+"','GOLD',"+pcd.commision_fin+",now(),"+invoice_no+",'Phirti Commision');";
                              con.executeUpdate(qry);
                            }
                        if(pcd.commision_rs!=0){
                              String qry = "insert into aj_transations(name,tran_type,amount,dt,invoice_no,comment) values('"+pcd.selected_cust_obj.name+"','MONEY',"+pcd.commision_rs+",now(),"+invoice_no+",'Phirti Commision');";
                              con.executeUpdate(qry);
                            }
                    }

                    //save to db..
                    con.executeUpdate("insert into sell_invoice_cust_old_bal values("+invoice_no+","+cust_obj.cid+","+last_g_bal+","+last_m_bal+")");
                    con.executeUpdate("insert into sell_invoice_date values("+invoice_no+",now())");
                    con.executeUpdate("update customer_mst set gold_ac="+phirti_co.gold_ac+", money_ac="+phirti_co.money_ac+" where cid="+PHIRTI_CID);
                    con.executeUpdate("update customer_mst set gold_ac="+cust_obj.gold_ac+", money_ac="+cust_obj.money_ac+" where cid="+cust_obj.cid);

                  if(total_fine!=0)
                    {
                        String qry = "insert into aj_transations(name,tran_type,amount,dt,invoice_no,comment) values('"+cust_obj.name+"','GOLD',-"+total_fine+",now(),"+invoice_no+",'Thusi Sell');";
                       // System.out.println(qry);
                        con.executeUpdate(qry);
                    }
                    if(total_labor!=0)
                    {
                        String qry = "insert into aj_transations(name,tran_type,amount,dt,invoice_no,comment) values('"+cust_obj.name+"','MONEY',-"+total_labor+",now(),"+invoice_no+",'Thusi Sell');";
                        //System.out.println(qry);
                        con.executeUpdate(qry);
                    }
                    
                     if(gold_credit!=0){
                        con.executeUpdate("insert into aj_transations(name,tran_type,amount,dt,invoice_no,comment) values('"+cust_obj.name+"','GOLD',"+gold_credit+",now(),"+invoice_no+",'At Sell Time "+conversion_comment_M+"');");
                       }

                    if(money_credit!=0){
                        con.executeUpdate("insert into aj_transations(name,tran_type,amount,dt,invoice_no,comment) values('"+cust_obj.name+"','MONEY',"+money_credit+",now(),"+invoice_no+",'At Sell Time "+conversion_comment_G+"');");
                   }
                    //create new invoice for customer....
                    String barcodein = "(";
                    String invoice_values = "";

                    for(Product p : stock_to_cust.stock)
                    {
                        if(!p.barcode.equals("--")){
                            barcodein+="'"+p.barcode+"',";
                            invoice_values+= "("
                                                    +invoice_no+","
                                                    +row_id+",'"
                                                    +p.barcode+"','"
                                                    +p.name+"',"
                                                    +p.wt+","
                                                    +p.tounch+","
                                                    +p.labor
                                                    +",1,"
                                                    +cust_obj.cid+"),";
                        }
                        else{
                            //update raw materail table..
                              String qry = "Update raw_stock_mst set qnty=qnty-"+p.qnty_if_raw+" where name='"+p.originalItm.name+"' and wt="+p.originalItm.wt+" and tounch="+p.originalItm.tounch;
                                //System.out.println(qry);
                                con.executeUpdate(qry);
                                invoice_values+= "("
                                                    +invoice_no+","
                                                    +row_id+","
                                                    + "'--',"
                                                    + "'"+p.name+"',"
                                                    +p.wt+","
                                                    +p.tounch+","
                                                    +p.labor+","
                                                    +p.qnty_if_raw+","
                                                    +cust_obj.cid+"),";
                                
                                
                        }
                        row_id++;
                    }
                    barcodein=barcodein.substring(0,barcodein.length()-1)+")";
                    invoice_values=invoice_values.substring(0,invoice_values.length()-1);

                    if(barcodein.length()>4)
                    {
                        con.executeUpdate("update selled_stock_mst set cid=" + cust_obj.cid + " where barcode in " + barcodein + " and cid=" + PHIRTI_CID);
                        con.executeUpdate("delete from sell_invoice where code in "+barcodein+" and cid="+PHIRTI_CID);
                    }
                    con.executeUpdate("insert into sell_invoice values " + invoice_values);
                    

                    Date d = new Date();
                    rs = con.executeQuery("select dt from aj_gold_in_hand order by dt desc limit 1;");
                    if(rs.next())
                        d = rs.getDate(1);
                    String qry = "update aj_gold_in_hand set gold=gold+"+gold_credit+",money=money+"+money_credit+" where dt = '"+d+"'";
                //    System.out.println(qry);
                    con.executeUpdate(qry);
                    all_tranfer_jama.add(new TranferJama(cust_obj.name, gold_credit, money_credit, currentTJ.thusi_75, currentTJ.thusi_92));
                    JOptionPane.showMessageDialog(rootPane, "Tranfer Completed!");
                    for(TranferJama tj : all_tranfer_jama)
                        System.out.println(tj);
                    initAll();
                }else{
                    System.out.println("Cancle!");
                    return;
                }
    }

    private void reset(){
        jComboBox1.setEnabled(true);
        initAll();
    }
    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PhirtiTransfer dialog = new PhirtiTransfer(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables

}

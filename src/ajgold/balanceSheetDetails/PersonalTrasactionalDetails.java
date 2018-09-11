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
import java.awt.Frame;
import java.sql.ResultSet;
import com.implidea.DecimalFormat;
import gayakwad.invoices.AllInvoices;
import java.awt.Color;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import ui.DatePickTest;
import ui.DatePickerDialog;

/**
 *
 * @author admin
 */
public class PersonalTrasactionalDetails extends javax.swing.JDialog {

    Frame parent;
    /** Creates new form PersonalTrasactionalDetails */
    public PersonalTrasactionalDetails(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.parent = parent;
        initComponents();
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

    }

     DecimalFormat df_gold = new DecimalFormat("########.###");
    DecimalFormat df_money = new DecimalFormat("########");


String[][] data = null;//new String[count][];
int[] invoice_no=null;

    private String current_cname;
    public boolean setDataOfCustomer(String cname){
        try {
            current_cname=cname;
            jLabel3.setText(cname);
            return loadTrasactions("", "");
        } catch (Exception ex) {
            Logger.getLogger(PersonalTrasactionalDetails.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private  ColabrativeTrasactions[] collabTrasacions = null;
 private boolean loadTrasactions(String frmdate,String toDate) throws Exception{

      if(frmdate.length()<1)
              frmdate="1990-01-01";

          if(toDate.length()<1)
          {
              Date dt = new Date();
              toDate=""+(dt.getYear()+1900)+"-"+(dt.getMonth()+1)+"-"+(dt.getDate());
          }


      
     /*

          SELECT gold_ac+sum(amount)*(-1) as ss FROM
          (SELECT * FROM aj_transations at where at.name='Mahakali Jwel' and dt > '2015-10-15') p
           join
           customer_mst cm
          where cm.name='Mahakali Jwel' and tran_type='GOLD';

      */

  float open_gold = getOpeningBal("GOLD", frmdate, toDate);
  float open_m = getOpeningBal("MONEY", frmdate, toDate);

     jLabel12.setText(df_gold.format(Math.abs(open_gold)));
     if(open_gold<0)
         jLabel12.setForeground(Color.red);
     else
         jLabel12.setForeground(Color.BLACK);




     jLabel13.setText(df_money.format(Math.abs(open_m)));
     if(open_m<0)
         jLabel13.setForeground(Color.red);
     else
         jLabel13.setForeground(Color.BLACK);



     float closing_g=getClosingBal("GOLD", open_gold, frmdate, toDate),closing_m=getClosingBal("MONEY", open_m, frmdate, toDate);



        jLabel14.setText(df_gold.format(Math.abs(closing_g)));
     if(closing_g<0)
         jLabel14.setForeground(Color.red);
     else
         jLabel14.setForeground(Color.BLACK);




     jLabel15.setText(df_money.format(Math.abs(closing_m)));
     if(closing_m<0)
         jLabel15.setForeground(Color.red);
     else
         jLabel15.setForeground(Color.BLACK);



        //SELECT * FROM aj_transations where dt>='2015-08-1' and dt<='2015-09-08' order by dt desc;

        //if empty...
        //SELECT * FROM aj_transations where dt>='1990-01-01' and dt<=now() order by dt desc


         
            DBConnection con = new DBConnection();
            int count=0;
            String condision = " where dt>='"+frmdate+"' and dt<='"+toDate+"' and name='"+current_cname+"' order by dt desc;";
            try{
                String qry = "SELECT count(id) FROM aj_transations "+condision;
                System.out.println(qry);
            ResultSet rs = con.executeQuery(qry);
            if(rs.next())
                count=rs.getInt(1);
            } catch (Exception ex) {
                Logger.getLogger(PersonalTrasactionalDetails.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(count==0){
                
                dispose();
                return false;
            }
            data = new String[count][];
            invoice_no=new int[count];
            int index = 0;
            String qry = "SELECT * FROM aj_transations "+condision;
            System.out.println(qry);
            ResultSet rs = con.executeQuery(qry);
            PersonalTransactions pt = new PersonalTransactions();
            while(rs.next()){
                //data[index] = new String[5];
                int tid = rs.getInt(1);//id
               String cnam =  rs.getString(2); //name
                String t_type = rs.getString(3);
                float amt = rs.getFloat(4);
                Date dt = rs.getDate(5);
               
                // String date = ""+(dt.getYear()+1900)+"-"+(dt.getMonth()+1)+"-"+dt.getDate();
               // System.out.println("["+date+"]");



                invoice_no[index] = rs.getInt(6);
                String cmmt = rs.getString(7);

                pt.addNewTransaction(tid,cnam, dt, t_type, amt, cmmt, invoice_no[index]);
            }
            System.out.println("--------Colabrating-----------");
            collabTrasacions = pt.getCollabrativeTrasactions();
            System.out.println("--------Colabration Completed-----------");
            data = new String[collabTrasacions.length][5];
            invoice_no=new int[collabTrasacions.length];
            for(int i=0;i<data.length;i++){

                ColabrativeTrasactions ppt = collabTrasacions[i];
                invoice_no[i] = ppt.invoice_no;
                data[i][2] = ppt.getDate();//""+ppt.dt.getDate()+"-"+(dt.getMonth()+1)+"-"+(dt.getYear()+1900);
                if(ppt.allTransactions[0]!=null)
                        data[i][0] = ppt.allTransactions[0].getTblData();//  df_gold.format(Math.abs(ppt.allTransactions[0].amt));
                    else    data[i][0] = "----";

                    if(ppt.allTransactions[1]!=null)
                        data[i][1] = ppt.allTransactions[1].getTblData();//df_money.format(Math.abs(ppt.allTransactions[1].amt));
                    else    data[i][1] = "----";


                    if(ppt.allTransactions[2]!=null)
                        data[i][3] = ppt.allTransactions[2].getTblData();//df_gold.format(Math.abs(ppt.allTransactions[2].amt));
                    else    data[i][3] = "----";


                    if(ppt.allTransactions[3]!=null)
                        data[i][4] = ppt.allTransactions[3].getTblData();//df_gold.format(Math.abs(ppt.allTransactions[3].amt));
                    else    data[i][4] = "----";
               
               }



        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            data,
            new String [] {
                "Gold-Jama", "Money-Jama", "Date", "Gold-Nave", "Money-Nave"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
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
    jTable1.getColumnModel().getColumn(0).setCellRenderer(rightRenderer);
    jTable1.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
    jTable1.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
    jTable1.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
    return true;
    }


 private float getOpeningBal(String t_type,String frmdate,String toDate) throws Exception{

String clm = "gold_ac";
if(t_type.equals("MONEY"))
    clm = "money_ac";
     String qry = " SELECT "+clm+"+sum(amount)*(-1) as ss FROM "+
          "(SELECT * FROM aj_transations at where at.name='"+current_cname+"' and dt > '"+frmdate+"'  ) p"+
          " join"+
          " customer_mst cm"+
          " where cm.name='"+current_cname+"' and tran_type='"+t_type+"';";
        System.out.println(qry);
        ResultSet rs_g_open  = new DBConnection().executeQuery(qry);

     if(rs_g_open.next())
         return rs_g_open.getFloat(1);

     return 0;
 }
    private float getClosingBal(String t_type,float opening,String frmdate,String toDate) throws Exception{
        /*
        SELECT sum(amount)+(-3.028) as ss FROM
          (SELECT * FROM aj_transations at where at.name='Mahakali Jwel' and dt > '2015-10-15') p
           join
           customer_mst cm
          where cm.name='Mahakali Jwel' and tran_type='GOLD';
        */

        String qry = " SELECT sum(amount)+("+opening+") as ss FROM "+
          "(SELECT * FROM aj_transations at where at.name='"+current_cname+"' and dt > '"+frmdate+"' and dt < '"+toDate+"' ) p"+
          " join"+
          " customer_mst cm"+
          " where cm.name='"+current_cname+"' and tran_type='"+t_type+"';";

        System.out.println(qry);
          ResultSet rs_g_open  = new DBConnection().executeQuery(qry);



     if(rs_g_open.next())
         return  rs_g_open.getFloat(1);

        return 0;
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
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 102, 102));

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Gold-Jama", "Money-Jama", "Date", "Gold-Nave", "Money-Nave"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setGridColor(new java.awt.Color(0, 0, 0));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Customer Transaction Sheet:");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 24));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Name:");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 24));
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Dheeraj Maralkar");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 18));
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("From");

        jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField1MouseClicked(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });

        jTextField2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField2MouseClicked(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 18));
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("To");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jLabel6.setText("Opening");

        jLabel7.setText("Gold:");

        jLabel8.setText("Money:");

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jLabel9.setText("Closing");

        jLabel10.setText("Gold:");

        jLabel11.setText("Money:");

        jLabel12.setText("jLabel12");

        jLabel13.setText("jLabel12");

        jLabel14.setText("jLabel12");

        jLabel15.setText("jLabel12");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel12))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel13)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 146, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel14))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel15)))
                .addGap(61, 61, 61))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jLabel14))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(jLabel15)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel12))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel13))))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        jTextArea1.setRows(5);
        jTextArea1.setText("Transactional Details:- ");
        jScrollPane2.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 976, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 976, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(62, 62, 62)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(48, 48, 48))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // exit
        dispose();
}//GEN-LAST:event_jMenuItem1ActionPerformed
String frmDate = "",toDate="";
    private void jTextField1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField1MouseClicked
        // from date


          DatePickerDialog dpd = new DatePickerDialog(parent, true);
                dpd.setVisible(true);

                String sd = dpd.SELECTED_DATE;
                if(sd==null || sd.length()<1){
                    //jTextField2.requestFocus();
                    Date selectedDay = new Date();
                    sd =  "";//+(selectedDay.getYear()+1900)+"-"+(selectedDay.getMonth()+1)+"-"+(selectedDay.getDate());
                }
                frmDate = sd;
                jTextField1.setText(dpd.SELECTED_DATE_display);
        try {
            loadTrasactions(frmDate, toDate);
        } catch (Exception ex) {
            Logger.getLogger(PersonalTrasactionalDetails.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jTextField1MouseClicked

    private void jTextField2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField2MouseClicked
        // To date

            DatePickerDialog dpd = new DatePickerDialog(parent, true);
                dpd.setVisible(true);

                String sd = dpd.SELECTED_DATE;
                if(sd==null || sd.length()<1){
                    //jTextField2.requestFocus();
                    //Date selectedDay = new Date();
                    sd =  "";//+(selectedDay.getYear()+1900)+"-"+(selectedDay.getMonth()+1)+"-"+(selectedDay.getDate());
                }
                toDate = sd;
                jTextField2.setText(dpd.SELECTED_DATE_display);
        try {
            loadTrasactions(frmDate, toDate);
        } catch (Exception ex) {
            Logger.getLogger(PersonalTrasactionalDetails.class.getName()).log(Level.SEVERE, null, ex);
        }

                
    }//GEN-LAST:event_jTextField2MouseClicked
    AllInvoices ai;
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        if(evt.getClickCount()==2){
            int invNo = invoice_no[jTable1.getSelectedRow()];
            System.out.println("Inv No:"+invNo);

            if(invNo==0){
                JOptionPane.showMessageDialog(rootPane, "Manual Transaction!");
                return;
            }
             ai = new AllInvoices(parent, true);
             if(!ai.openInvoice(invNo))
             {
                 JOptionPane.showMessageDialog(rootPane, "Invoice Number Not Found!");
                 return;
             }
             ai.setVisible(true);
             setVisible(false);
             new Thread(){
                 public void run(){
                     while(ai.isVisible()){
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(PersonalTrasactionalDetails.class.getName()).log(Level.SEVERE, null, ex);
                        }
                     }
                     setVisible(true);
                     ai=null;
                 }
             }.start();
           }
           else
           {
            int clm = jTable1.getSelectedColumn();
            if(clm>2)
                clm--;
            PTrasction ptt = collabTrasacions[jTable1.getSelectedRow()].allTransactions[clm];
            jTextArea1.setText("Transactional Details:-  "+ptt);
           }
    }//GEN-LAST:event_jTable1MouseClicked

    public boolean isMeActive(){
        if(isVisible()) return true;
        System.out.println ("Is me :"+(ai==null));
        return ai!=null;
    }
    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        try {
            // TODO add your handling code here:
            //frm date..
            char ch = evt.getKeyChar();
            if (ch == '\n') {
                loadTrasactions(frmDate, toDate);
            }
        } catch (Exception ex) {

            ex.printStackTrace();;
            JOptionPane.showMessageDialog(parent, ex);
        }
    }//GEN-LAST:event_jTextField1KeyPressed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PersonalTrasactionalDetails dialog = new PersonalTrasactionalDetails(new javax.swing.JFrame(), true);
                dialog.setDataOfCustomer("dhanjay ghorpade");

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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
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
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables

}

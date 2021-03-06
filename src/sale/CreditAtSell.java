/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sale;

import java.awt.Color;
import com.implidea.DecimalFormat;
import javax.swing.JOptionPane;

/**
 *
 * @author SNEHA
 */
public class CreditAtSell extends javax.swing.JDialog {

    /**
     * Creates new form CreditAtSell
     */
    CustomerObj cust_obj;
    float sellFine,sellLabor;
    DecimalFormat df_gold = new DecimalFormat("######.###");
    DecimalFormat df_money = new DecimalFormat("######.##");
    public CreditAtSell(java.awt.Frame parent, boolean modal,CustomerObj cust_obj,float sellFine,float sellLabor) {
        super(parent, modal);
        initComponents();
        java.awt.Dimension dim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, 0);
        this.cust_obj = cust_obj;
        this.sellFine=sellFine;
        this.sellLabor=sellLabor;
        jLabel4.setText("A/C:"+cust_obj.name);
        jLabel6.setText(""+df_gold.format(Math.abs(cust_obj.gold_ac)));
        jLabel8.setText(""+df_money.format(Math.abs(cust_obj.money_ac)));
        if(cust_obj.gold_ac<0)
            jLabel6.setForeground(Color.red);
        if(cust_obj.money_ac<0)
            jLabel8.setForeground(Color.red);
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
        jLabel1 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 102, 102));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 18));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Credit Gold And Money");

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Gold:");

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Weight");

        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField3KeyTyped(evt);
            }
        });

        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("gm");

        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("%");

        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField4KeyTyped(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Tounch");

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Cash:");

        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField5KeyTyped(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Rs.");

        jButton2.setText("Finish (Ctrl+F)");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 18));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Fine: 0.0 gm");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 18));
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Cash: Rs. 0.0");

        jButton3.setText("Cancel (Ctrl+E)");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 14));
        jLabel4.setText("A/C of :");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel5.setText("Gold A/C");

        jLabel6.setForeground(new java.awt.Color(51, 51, 51));
        jLabel6.setText("Credit/Debit");

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel7.setText("Cash A/C");

        jLabel8.setText("Credit/Debit");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 518, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(9, 9, 9)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel7))
                .addContainerGap())
        );

        jTextField6.setText("26000");
        jTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField6KeyTyped(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Rate:");

        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("/10gm");

        jButton1.setText("MTG");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton4.setText("GTM");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Save (Ctrl+S)");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel13)
                                            .addComponent(jLabel14))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jTextField4)
                                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jLabel10))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel15))
                                .addGap(53, 53, 53)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(8, 8, 8)
                                        .addComponent(jButton1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton4))
                                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel17))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel16)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel3)
                                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addContainerGap(27, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addComponent(jButton5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3)))
                        .addContainerGap(222, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel17))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)
                            .addComponent(jLabel13)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel14)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel15)
                                .addComponent(jLabel3))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16))))
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jMenu2.setText("Options");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Finish");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setText("Save");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Reset");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText("Exit");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

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
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyTyped
        //  wt key
        char key = evt.getKeyChar();
        if (key == '\n') {
            jTextField4.requestFocus();
        }
    }//GEN-LAST:event_jTextField3KeyTyped

    
    public float total_fine_actual_recv=0;
    public float total_fine_converted=0;
    
    public float total_cash_actual_recv=0;
    public float total_cash_converted=0;

    private float M_TO_G = 0;
    private float G_TO_M = 0;

    public float total_M_TO_G = 0;
    public float total_G_TO_M = 0;

    private void jTextField4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyTyped
        // tnch key
        char key = evt.getKeyChar();
        if (key == '\n') {
            
            if (jTextField3.getText().length() > 0) {
                if (jTextField4.getText().length() > 0) {
                    try {
                        float wt = Float.parseFloat(jTextField3.getText());
                        float tounch = Float.parseFloat(jTextField4.getText());
                        float fine = wt * (tounch / 100);
                        //if(total_fine_to_pay<=total_fine_paid)
                        //total_fine_to_pay+=fine;
                        
                        if(M_TO_G>0){
                            total_cash_converted+=M_TO_G;
                            total_M_TO_G+=fine;
                            M_TO_G=0;
                        }else
                            total_fine_actual_recv+=fine;
                        jLabel2.setText("Fine: "+df_gold.format(total_fine_actual_recv+total_M_TO_G)+" gm");
                    //    jTextField3.setText("");
                     //   jTextField4.setText("");
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(rootPane, "Wrong Entry For Weight Or Tounch", "Error", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Please Enter Tounch!");
                    return;
                }

            } else {
                jTextField5.requestFocus();
            }
        }
    }//GEN-LAST:event_jTextField4KeyTyped

    private void jTextField5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyTyped
        // rs transction
        char key = evt.getKeyChar();
        if (key == '\n') {
            
            String str = jTextField5.getText();
            if (str.length() < 1) {
            
                return;
            }
            float money = Float.parseFloat(str);
            //if(total_cash_to_pay<=total_cash_paid)
             //   total_cash_to_pay+=money;
            

            if(G_TO_M>0){
                total_fine_converted+=G_TO_M;
                total_G_TO_M+=money;
                G_TO_M=0;
            }
            else{
              total_cash_actual_recv+=money;
            }

            jLabel3.setText("Cash:Rs."+df_money.format(total_cash_actual_recv+total_G_TO_M)+"");
        }
    }//GEN-LAST:event_jTextField5KeyTyped

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Finish Btn
        save();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // Finish Menu
        save();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    public boolean FINISHED = false;
    private void save(){

     //   float newFine = (cust_obj.gold_ac-sellFine)+(((total_fine_converted+total_fine_actual_recv+total_M_TO_G)));
     //   float newCash = (cust_obj.money_ac-sellLabor)+((total_cash_converted+total_cash_actual_recv+total_G_TO_M));
        float newFine = (cust_obj.gold_ac-sellFine)+(((total_fine_actual_recv+total_fine_converted)));
        float newCash = (cust_obj.money_ac-sellLabor)+((total_cash_actual_recv+total_cash_converted));
        
        cust_obj.money_ac = newCash;//(sellLabor -(total_cash_converted));
        cust_obj.gold_ac = newFine;//(sellFine-total_fine_converted);
        total_cash_actual_recv+=total_G_TO_M;
        total_fine_actual_recv+=total_M_TO_G;
        FINISHED=true;
        dispose();
    }
    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // Reset Menu
        
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // exit
        dispose();
        
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // cancle
        dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextField6KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyTyped
        // M to G
    }//GEN-LAST:event_jTextField6KeyTyped

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // G to M


        String txt = jTextField6.getText();
        if(txt.length()<1){
            JOptionPane.showMessageDialog(rootPane, "Please Enter Gold Rate!");
            return;
        }
        
        float rate = 0;
        float this_fine=0;
            if (jTextField3.getText().length() > 0) {
                if (jTextField4.getText().length() > 0) {
                    try {
                        float wt = Float.parseFloat(jTextField3.getText());
                        float tounch = Float.parseFloat(jTextField4.getText());
                        this_fine = wt * (tounch / 100);
                        System.out.println("TF:"+this_fine);
                       // total_fine+=this_fine;
                        
                        //jLabel2.setText("Fine: "+df_gold.format(total_fine)+" gm");
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(rootPane, "Wrong Entry For Weight Or Tounch", "Error", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                }else {
                    JOptionPane.showMessageDialog(rootPane, "Please Enter Tounch!");
                    return;
                }
            }
                
        try{
            rate = Float.parseFloat(txt);
        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(rootPane, "Wrong Gold Rate Entry");
            return;
        }


        rate = rate/10; //per gm..

        G_TO_M = this_fine;
        //total_fine_to_pay+=this_fine;
        //total_fine_paid+=this_fine;
        jTextField5.setText(""+df_money.format(rate*this_fine));

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // Save..
        
        reflectOP();

    }//GEN-LAST:event_jButton5ActionPerformed


    private void reflectOP(){
        float newFine = (cust_obj.gold_ac-sellFine)+(((total_fine_actual_recv+total_fine_converted)));
        float newCash = (cust_obj.money_ac-sellLabor)+((total_cash_actual_recv+total_cash_converted));
        System.out.println("("+cust_obj.money_ac+"-"+sellLabor+")+(("+total_cash_actual_recv+"+"+total_cash_converted+"))=>["+newCash+"]");
         //jLabel4.setText("A/C:"+cust_obj.name);
        jLabel6.setText(""+df_gold.format(Math.abs(newFine)));
        jLabel8.setText(""+df_money.format(Math.abs(newCash)));
        if(newFine<0)
            jLabel6.setForeground(Color.red);
        else
            jLabel6.setForeground(Color.black);

        if(newCash<0)
            jLabel8.setForeground(Color.red);
        else
            jLabel8.setForeground(Color.black);
    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // M T G

        String txt = jTextField6.getText();
        if(txt.length()<1){
            JOptionPane.showMessageDialog(rootPane, "Please Enter Gold Rate!");
            return;
        }
        
        float rate = 0;
        try{
        rate = Float.parseFloat(txt);
        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(rootPane, "Wrong Gold Rate Entry");
            return;
        }

        float money = 0;
        try{
            money = Float.parseFloat(jTextField5.getText());
        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(rootPane, "Wrong Gold Rate Entry");
            return;
        }

        

        System.out.println("["+rate+"/10]");
        rate = rate/10; //per gm..

        System.out.println("["+rate+"/1]");
        float this_fine = money/rate;

        M_TO_G = money;
        //total_fine_to_pay+=this_fine;
        //total_cash_paid+=money;

        jTextField3.setText(""+this_fine);
        jTextField4.setText("100");
        jTextField3.requestFocus();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // Save Menu..
        reflectOP();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
         
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    // End of variables declaration//GEN-END:variables
}

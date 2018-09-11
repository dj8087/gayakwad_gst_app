/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * OuterFrm.java
 *
 * Created on Jun 10, 2018, 1:32:35 AM
 */

package org.ajgold.gst;

import com.implidea.DecimalFormat;
import java.awt.Component;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ajgold.gst.dialogs.SelectListDialog;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.ajgold.gst.dialogs.ConfirmationDialog;
import sale.CustomerObj;
import ui.DatePickerDialog;
 

/**
 *
 * @author Dheeraj
 */
public class _3NewGstInvoice extends JFrame implements KeyListener  {

    public void keyTyped(KeyEvent e) {}  public void keyPressed(KeyEvent e) {}

    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_UP:
                ((JTextField) getPrevTabField((JTextField) e.getComponent())).requestFocus();
                break;
            case KeyEvent.VK_DOWN:
                ((JTextField) getNextTabField((JTextField) e.getComponent())).requestFocus();
                break;
            case KeyEvent.VK_ENTER:
                processSubmitAction(e.getComponent());
                break;

            case KeyEvent.VK_ESCAPE:
                ConfirmationDialog cd = new ConfirmationDialog(this, true, "Do You Want To Exit..?");
                cd.setVisible(true);
                if(cd.isApproved()){
                    FormControllerStack.popMe();
                }
                break;
            default:
                calculate((JTextField) e.getComponent());
                break;
        }
    }

    private void calculate(JTextField t){
        if(t==weightTxt){
            calculateFlow1();
        }else if(t==rateTxt){
            calculateFlow1();
        }else if(t==grandTotalTxt){
            calculateFlow2();
        }
    }
    private void processSubmitAction(Component t){
        if(t==jTextField10){
            isDialogOpen = true;
            SelectListDialog scd = new SelectListDialog(this, true,new String[]{ "Maharashatra", "Other" });
            scd.setVisible(true);
            jTextField10.setText(scd.getSelectedItem());
            isDialogOpen = false;
            focuseLost(jTextField10);
            if(scd.getSelectedItem().equals("Maharashatra")){
                isLocalState = true;
            }else{
                isLocalState = false;
            }
            calculateFlow1();
        }else if(t==jTextField1){
            isDialogOpen = true;
            if(AllComns.allCustomers==null){
                try {
                    AllComns.allCustomers = CustomerObj.loadAllAllCust();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,"Error While Loading Customer Data","Error",JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            SelectListDialog scd = new SelectListDialog(this, true,AllComns.allCustomers);
            scd.setVisible(true);
            if(scd.getSelectedItem()!=null){
                jTextField1.setText(scd.getSelectedItem());
            }else{
                jTextField1.setText("No Customer Selected");
            }
            isDialogOpen = false;
            focuseLost(jTextField1);
        } else if(t==jTextField2){
            isDialogOpen = true;
            DatePickerDialog dpd = new DatePickerDialog(this, true);
            dpd.setVisible(true);
            String sd = dpd.SELECTED_DATE;
            if(sd==null || sd.length()<1){
                //jTextField2.requestFocus();
                Date selectedDay = new Date();
                sd =  ""+(selectedDay.getYear()+1900)+"-"+(selectedDay.getMonth()+1)+"-"+(selectedDay.getDate());
            }
            jTextField2.setText(sd);
            isDialogOpen = false;
            focuseLost(jTextField2);
        }  else if(t==jButton1){
            ConfirmationDialog cd = new ConfirmationDialog(this, true, "Confirm Save...?");
            cd.setVisible(true);
            if(cd.isApproved()){
                FormControllerStack.popMe();
            }
        }
        else{
            calculate((JTextField) t);
            ((JTextField) getNextTabField((JTextField) t)).requestFocus();
        }
    }
    private final ArrayList<JComponent> tabIndex = new ArrayList<JComponent>();
    /** Creates new form OuterFrm */
    public _3NewGstInvoice() {
        setName("NewGstInvoice");
        FormControllerStack.pushMe(this);
        setUndecorated(true);
        setAlwaysOnTop(true);
        setResizable(false);
        setVisible(true);
    //    setResizable(false);
     //   setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        setResizable(false);

        initComponents();
        Toolkit tk = Toolkit.getDefaultToolkit();
        setSize(new Dimension((int) tk.getScreenSize().getWidth(), (int) tk.getScreenSize().getHeight()));

        initTextFeild(jTextField1,"Txt1");
        initTextFeild(jTextField2,"Txt2");
        initTextFeild(jTextField3,"Txt3");
        initTextFeild(jTextField4,"Txt4");
        initTextFeild(weightTxt,"Txt5");
        initTextFeild(rateTxt,"Txt6");
        initTextFeild(totalTxt,"Txt7");
        initTextFeild(jTextField10,"Txt10");
        initTextFeild(totalGST,"Txt8");
        initTextFeild(grandTotalTxt,"Txt9");
        jButton1.addKeyListener(this);
        tabIndex.add(jButton1);

       // jTextField1.setFocusable(true);
        jTextField1.requestFocus();
 
    }

    private void initTextFeild(JTextField tf,String name){
        tabIndex.add(tf);
        tf.setName(name);
        tf.addKeyListener(this);
        focuseLost(tf);
    }


    private int getTabIndex(JTextField tf){
        for (int i = 0; i < tabIndex.size(); i++) {
            if(tf==(tabIndex.get(i)))  return i;
        }
        return -1;
    }

    private JComponent getTabField(int index){
        return tabIndex.get(index);
    }

    private JComponent getNextTabField(JTextField tf){
        int tI = (getTabIndex(tf)+1);
        if(tI>tabIndex.size()-1)    return null;
        return getTabField(tI);
    }

    private JComponent getPrevTabField(JTextField tf){
        int tI = (getTabIndex(tf));
        if(tI<1)    return null;
        return getTabField(tI-1);
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
        jLabel1 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        weightTxt = new javax.swing.JTextField();
        rateTxt = new javax.swing.JTextField();
        totalTxt = new javax.swing.JTextField();
        totalGST = new javax.swing.JTextField();
        grandTotalTxt = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jTextField10 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                formKeyTyped(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(153, 102, 255));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("New Invoice");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(742, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1)
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 24));
        jLabel6.setText("$Account Name");

        jLabel7.setFont(new java.awt.Font("Times New Roman", 0, 18));
        jLabel7.setText("Customer Name:");

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jLabel8.setText("$today Date");

        jTextField1.setBackground(new java.awt.Color(240, 240, 240));
        jTextField1.setEditable(false);
        jTextField1.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jTextField1.setText("Select Customer...");
        jTextField1.setCaretColor(new java.awt.Color(240, 240, 240));
        jTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField1FocusLost(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Times New Roman", 0, 18));
        jLabel9.setText("Invoice Date:");

        jTextField2.setBackground(new java.awt.Color(240, 240, 240));
        jTextField2.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jTextField2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField2FocusLost(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Calibri", 1, 24));
        jLabel5.setText("Item Description:");

        jLabel10.setFont(new java.awt.Font("Times New Roman", 0, 18));
        jLabel10.setText("Goods Details");

        jLabel11.setFont(new java.awt.Font("Times New Roman", 0, 18));
        jLabel11.setText("Code");

        jLabel12.setFont(new java.awt.Font("Times New Roman", 0, 18));
        jLabel12.setText("Weight (g)");

        jLabel13.setFont(new java.awt.Font("Times New Roman", 0, 18));
        jLabel13.setText("Rate (per 10g)");

        jLabel14.setFont(new java.awt.Font("Times New Roman", 0, 18));
        jLabel14.setText("Total");

        jLabel16.setFont(new java.awt.Font("Times New Roman", 0, 18));
        jLabel16.setText("Total GST");

        jLabel15.setFont(new java.awt.Font("Times New Roman", 0, 18));
        jLabel15.setText("Grand Total");

        jTextField3.setBackground(new java.awt.Color(240, 240, 240));
        jTextField3.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jTextField3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextField3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField3FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField3FocusLost(evt);
            }
        });

        jTextField4.setBackground(new java.awt.Color(240, 240, 240));
        jTextField4.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jTextField4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextField4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField4FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField4FocusLost(evt);
            }
        });

        weightTxt.setBackground(new java.awt.Color(240, 240, 240));
        weightTxt.setFont(new java.awt.Font("Times New Roman", 1, 18));
        weightTxt.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        weightTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                weightTxtFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                weightTxtFocusLost(evt);
            }
        });

        rateTxt.setBackground(new java.awt.Color(240, 240, 240));
        rateTxt.setFont(new java.awt.Font("Times New Roman", 1, 18));
        rateTxt.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        rateTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                rateTxtFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                rateTxtFocusLost(evt);
            }
        });

        totalTxt.setBackground(new java.awt.Color(240, 240, 240));
        totalTxt.setEditable(false);
        totalTxt.setFont(new java.awt.Font("Times New Roman", 1, 18));
        totalTxt.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        totalTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                totalTxtFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                totalTxtFocusLost(evt);
            }
        });

        totalGST.setBackground(new java.awt.Color(240, 240, 240));
        totalGST.setEditable(false);
        totalGST.setFont(new java.awt.Font("Times New Roman", 1, 18));
        totalGST.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        totalGST.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                totalGSTFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                totalGSTFocusLost(evt);
            }
        });

        grandTotalTxt.setBackground(new java.awt.Color(240, 240, 240));
        grandTotalTxt.setFont(new java.awt.Font("Times New Roman", 1, 18));
        grandTotalTxt.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        grandTotalTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                grandTotalTxtFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                grandTotalTxtFocusLost(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(51, 51, 255));
        jButton1.setText("Save & Print");
        jButton1.setToolTipText("");
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jTextField10.setBackground(new java.awt.Color(240, 240, 240));
        jTextField10.setEditable(false);
        jTextField10.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jTextField10.setText("Select State..");
        jTextField10.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextField10.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField10FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField10FocusLost(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Times New Roman", 0, 18));
        jLabel17.setText("GST State");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 569, Short.MAX_VALUE)
                        .addComponent(jLabel8))
                    .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE)
                    .addComponent(jLabel5)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11)
                            .addComponent(jLabel14)
                            .addComponent(jLabel13)
                            .addComponent(jLabel12)
                            .addComponent(jLabel17)
                            .addComponent(jLabel16)
                            .addComponent(jLabel15))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(totalGST, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(grandTotalTxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(weightTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rateTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(totalTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel8))
                .addGap(15, 15, 15)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(jLabel12))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(weightTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(rateTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(totalTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalGST, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(grandTotalTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(83, 83, 83))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    long lastUpdate = System.currentTimeMillis();
    private void formKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyTyped
         
    }//GEN-LAST:event_formKeyTyped

    boolean isDialogOpen = false;
    private void jTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusLost
        // Customer Name Fouce lost
        if(!isDialogOpen)   focuseLost(jTextField1);
    }//GEN-LAST:event_jTextField1FocusLost

    private void jTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusGained
         // Customer Name Fouce Gained
        focused(jTextField1);
    }//GEN-LAST:event_jTextField1FocusGained

    private void jTextField2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField2FocusGained
        // Invoice Date Fouce
        focused(jTextField2);
    }//GEN-LAST:event_jTextField2FocusGained

    private void jTextField2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField2FocusLost
        // TODO add your handling code here:
         focuseLost(jTextField2);
    }//GEN-LAST:event_jTextField2FocusLost

    private void jTextField3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField3FocusGained
        focused(jTextField3);
    }//GEN-LAST:event_jTextField3FocusGained

    private void jTextField3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField3FocusLost
        focuseLost(jTextField3);
    }//GEN-LAST:event_jTextField3FocusLost

    private void jTextField4FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField4FocusGained
        focused(jTextField4);
    }//GEN-LAST:event_jTextField4FocusGained

    private void jTextField4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField4FocusLost
       focuseLost(jTextField4);
    }//GEN-LAST:event_jTextField4FocusLost

    private void weightTxtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_weightTxtFocusGained
       focused(weightTxt);
    }//GEN-LAST:event_weightTxtFocusGained

    private void weightTxtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_weightTxtFocusLost
      focuseLost(weightTxt);
    }//GEN-LAST:event_weightTxtFocusLost

    private void rateTxtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rateTxtFocusGained
        focused(rateTxt);
    }//GEN-LAST:event_rateTxtFocusGained

    private void rateTxtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rateTxtFocusLost
       focuseLost(rateTxt);
    }//GEN-LAST:event_rateTxtFocusLost

    private void totalTxtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_totalTxtFocusGained
        focused(totalTxt);
    }//GEN-LAST:event_totalTxtFocusGained

    private void totalTxtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_totalTxtFocusLost
        focuseLost(totalTxt);
    }//GEN-LAST:event_totalTxtFocusLost

    private void totalGSTFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_totalGSTFocusGained
        focused(totalGST);
    }//GEN-LAST:event_totalGSTFocusGained

    private void totalGSTFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_totalGSTFocusLost
       focuseLost(totalGST);
    }//GEN-LAST:event_totalGSTFocusLost

    private void grandTotalTxtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_grandTotalTxtFocusGained
        focused(grandTotalTxt);
    }//GEN-LAST:event_grandTotalTxtFocusGained

    private void grandTotalTxtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_grandTotalTxtFocusLost
            focuseLost(grandTotalTxt);
    }//GEN-LAST:event_grandTotalTxtFocusLost

    private void jTextField10FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField10FocusGained
        // GST State

        focused(jTextField10);
       
    }//GEN-LAST:event_jTextField10FocusGained

    private void jTextField10FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField10FocusLost
        // GST State

        if(!isDialogOpen)   focuseLost(jTextField10);
    }//GEN-LAST:event_jTextField10FocusLost

    private  void focused(JTextField jta){
        jta.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jta.setBackground(new java.awt.Color(204, 204, 204));
    }

    private  void focuseLost(JTextField jta){
        jta.setBorder(null);
        jta.setBackground(new java.awt.Color(240, 240, 240));
    }

    
    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new _3NewGstInvoice().setVisible(true);
            }
        });
    }

    boolean isLocalState = true;
 private float getTotalGST(){
//        if(jRadioButton1.isSelected())
//        {
//            float sGst = Float.parseFloat(sGSTtxt.getText());
//            float cGst = Float.parseFloat(cGstTxt.getText());
//            return sGst+cGst;
//        }else{
//            return Float.parseFloat(iGstTxt.getText());
//        }
     if(isLocalState)
        return AllComns.iGST+AllComns.sGST;
     else
        return AllComns.iGST;
    }


    private static DecimalFormat df = new DecimalFormat("#####.##");

    private void calculateFlow1(){

        float wt=0,rate=0,total=0,ttlGst=0,grandTotal=0;

        if(weightTxt.getText().length()<1)  return;
        if(rateTxt.getText().length()<1)  return;

        try{
                ttlGst = getTotalGST();
            }catch(Exception e){
                JOptionPane.showMessageDialog(this, "You Have Entered Incorrect GST Percentage!", "Wrong Value", JOptionPane.ERROR_MESSAGE);
                return;
            }

        try{
                wt = Float.parseFloat(weightTxt.getText());
            }catch(Exception e){
                JOptionPane.showMessageDialog(this, "You Have Entered Incorrect Weight!", "Wrong Value", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try{
                rate = Float.parseFloat(rateTxt.getText());
            }catch(Exception e){
                JOptionPane.showMessageDialog(this, "You Have Entered Incorrect Rate!", "Wrong Value", JOptionPane.ERROR_MESSAGE);
                return;
            }



            total = (wt)*rate/10;

            grandTotal = total+(total*ttlGst/100);

            grandTotalTxt.setText(df.format(grandTotal));
            totalTxt.setText(df.format(total));

            totalGST.setText(df.format(grandTotal-total));
    }


    private void calculateFlow2(){

        float wt=0,rate=0,total=0,ttlGst=0,grandTotal=0;

        if(grandTotalTxt.getText().length()<1)  return;

        try{
                ttlGst = getTotalGST();
            }catch(Exception e){
                JOptionPane.showMessageDialog(this, "You Have Entered Incorrect GST Percentage!", "Wrong Value", JOptionPane.ERROR_MESSAGE);
                return;
            }



        try{
                grandTotal = Float.parseFloat(grandTotalTxt.getText());
            }catch(Exception e){
                JOptionPane.showMessageDialog(this, "You Have Entered Incorrect Grand Total!", "Wrong Value", JOptionPane.ERROR_MESSAGE);
                return;
            }




        try{
                wt = Float.parseFloat(weightTxt.getText());
            }catch(Exception e){
                JOptionPane.showMessageDialog(this, "You Have Entered Incorrect Weight!", "Wrong Value", JOptionPane.ERROR_MESSAGE);
                return;
            }



            total = (100*grandTotal)/(100+ttlGst);

            rate = total*10/wt;


            totalTxt.setText(df.format(total));
            rateTxt.setText(df.format(rate));

            totalGST.setText(df.format(grandTotal-total));



    }



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField grandTotalTxt;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField rateTxt;
    private javax.swing.JTextField totalGST;
    private javax.swing.JTextField totalTxt;
    private javax.swing.JTextField weightTxt;
    // End of variables declaration//GEN-END:variables

}

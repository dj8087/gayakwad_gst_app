/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gayakwad.invoices;

import gayakwad.DBConnection;
import java.sql.ResultSet;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import sale.CustomerObj;
import stock.Product;


/**
 *
 * @author admin
 */
public class SellInvoice {

    public final int invoice_no,cid;
    public final String cname;
    public boolean is_closed;
    public final Product[] invoice_details;
    public  Date closedDate;
    public final float jamaGold,jamaMoney;
    public CustomerObj cust_obj;
    public final float old_gold,old_money;
    public final float convertType,amountM,amountG;

    public final Date open_date;
    //convert type 1:GTM 2:MTG


    public SellInvoice(int invoice_no, int cid, String cname, boolean is_closed, Product[] invoice_details, Date closedDate, float jamaGold, float jamaMoney,float old_gold, float old_money, float convertType, float amountM, float amountG,Date open_date) {
        this.invoice_no = invoice_no;
        this.cid = cid;
        this.cname = cname;
        this.is_closed = is_closed;
        this.invoice_details = invoice_details;
        this.closedDate = closedDate;
        this.jamaGold = jamaGold;
        this.jamaMoney = jamaMoney;
        this.old_gold = old_gold;
        this.old_money = old_money;
        this.convertType = convertType;
        this.amountM = amountM;
        this.amountG = amountG;
        this.open_date=open_date;
        System.out.println("Invoice:["+this.invoice_no+"]["+this.jamaGold+"]["+this.jamaMoney+"]");
    }

    

    




    @Override
    public String toString(){
        if(is_closed)
            return ""+invoice_no+" (Closed)";
        return ""+invoice_no+"";
    }

    

    public String[][] getDataForTable(){
        String[][] ret = new String[invoice_details.length][];
        for(int i=0;i<ret.length;i++){
            ret[i] = new String[6];
            Product p = invoice_details[i];
            ret[i][0] = p.barcode;
            ret[i][1] = p.name;
            ret[i][2] = ""+p.wt;
            ret[i][3] = ""+p.tounch;
            ret[i][4] = ""+p.labor;
            ret[i][5] = ""+p.qnty_if_raw;
        }
        return ret;

    }

    public float totalLabor(){
         float ret = 0;
        for(Product p : invoice_details){
            if(p.barcode.equals("--")){
                //raw materail..
                if(p.no_qnty_raw){

                    //non laki raw...
                    ret+=(p.labor);
                }else{
                    ret+=(((p.labor)*p.qnty_if_raw));
                }
            }
            else{
                // thusi..
                ret+=(p.labor);
            }
        }
        return ret;
    }
    public float totalWt(){
        float ret = 0;
        for(Product p : invoice_details){
            if(p.barcode.equals("--")){
                //raw materail..
                if(p.no_qnty_raw){

                    //non laki raw...
                    ret+=(p.wt);
                }else{
                    ret+=(((p.wt)*p.qnty_if_raw)/1000);
                }
            }
            else{
                // thusi..
                ret+=(p.wt);
            }
        }
        return ret;
    }
    public float getTotalFine(){
        float ret = 0;
        for(Product p : invoice_details){
            if(p.barcode.equals("--")){
                //raw materail..
                if(p.no_qnty_raw){
                    //non laki raw...
                    ret+=(p.wt*p.tounch/100);
                }else{
                    System.out.print("((("+p.wt+"*"+p.tounch+"/100)*"+p.qnty_if_raw+")/1000)=");
                    ret+=((((p.wt/1000)*p.qnty_if_raw*p.tounch/100)));
                    System.out.println(""+ret);
                }
            }
            else{
                // thusi..
                ret+=(p.wt*p.tounch/100);
            }
        }
        return ret;
    }



    public static SellInvoice[] loadAllInvoicesForCust(int cid) throws Exception{

        DBConnection con = new DBConnection();
        ResultSet rs = con.executeQuery("SELECT count(distinct(invoice_no)) FROM sell_invoice_not_update where cid="+cid);
        if(rs.next()){
            int count = rs.getInt(1);
            if(count<1) return null;
            SellInvoice[] ret = new SellInvoice[count];
            rs = con.executeQuery("SELECT distinct(invoice_no) FROM sell_invoice_not_update  where cid="+cid);
            int i=0;
            while(rs.next()){
                ret[i] = loadInvoiceFor(rs.getInt(1));
                i++;
            }

            return ret;
        }

        return null;
    }

    private static SellInvoice[] ret = null;
    static int tid = 0;

    public static SellInvoice[] loadAllInvoices1() throws Exception{
        tid=0;
        DBConnection con = new DBConnection();
        ResultSet rs = con.executeQuery("SELECT count(distinct(invoice_no)) FROM sell_invoice_not_update");
        if(rs.next()){
            int count = rs.getInt(1);
            ret  = new SellInvoice[count];
            final ResultSet rs1 = con.executeQuery("SELECT distinct(invoice_no) FROM sell_invoice_not_update;");
            int i=0;
            
            while(rs1.next()){
                int inv_no = rs1.getInt(1);
                new InvoiceLoader(i, inv_no).start();
                i++;
               // Thread.sleep(100);
            }
            System.out.println("Total Tid:"+tid);
            while(tid>0)
                Thread.sleep(100);

            System.out.println("Total Tid:"+tid);
            return ret;
        }

        return null;
    }



    public static SellInvoice[] loadAllOpenInvoices() throws Exception{
        tid=0;
        DBConnection con = new DBConnection();
        //SELECT * FROM sell_invoice_not_update where invoice_no in (SELECT distinct(invoice_no) FROM sell_not_closed_invoices);
        ResultSet rs = con.executeQuery("SELECT count(distinct(invoice_no)) FROM sell_invoice_not_update_not_close");
        if(rs.next()){
            int count = rs.getInt(1);
            ret  = new SellInvoice[count];
            final ResultSet rs1 = con.executeQuery("SELECT distinct(invoice_no) FROM sell_invoice_not_update_not_close;");
            int i=0;

            while(rs1.next()){
                int inv_no = rs1.getInt(1);
                new InvoiceLoader(i, inv_no).start();
                i++;
               // Thread.sleep(100);
            }
            System.out.println("Total Tid:"+tid);
            while(tid>0)
                Thread.sleep(100);

            System.out.println("Total Tid:"+tid);
            return ret;
        }

        return null;
    }


    public static SellInvoice[] loadAllClosedInvoices() throws Exception{
        tid=0;
        DBConnection con = new DBConnection();
        //SELECT * FROM sell_invoice_not_update where invoice_no in (SELECT distinct(invoice_no) FROM sell_not_closed_invoices);
        ResultSet rs = con.executeQuery("SELECT count(distinct(invoice_no)) FROM sell_invoice_closed");
        if(rs.next()){
            int count = rs.getInt(1);
            ret  = new SellInvoice[count];
            final ResultSet rs1 = con.executeQuery("SELECT distinct(invoice_no) FROM sell_invoice_closed;");
            int i=0;

            while(rs1.next()){
                int inv_no = rs1.getInt(1);
                new InvoiceLoader(i, inv_no).start();
                i++;
               // Thread.sleep(100);
            }
            System.out.println("Total Tid:"+tid);
            while(tid>0)
                Thread.sleep(100);

            System.out.println("Total Tid:"+tid);
            return ret;
        }

        return null;
    }




   static class InvoiceLoader extends Thread  {
        final int si;
        final int si_no;

        public InvoiceLoader(int si, int si_no) {
            this.si = si;
            this.si_no = si_no;
        }
 
        public void run(){
            try{
                tid++;
                    ret[si] = loadInvoiceFor(si_no);
                tid--;
                }catch(Exception e){
                    e.printStackTrace();
                }
        }
    }
    
    public static SellInvoice loadInvoiceFor(int invoice_no) throws Exception{
        DBConnection con = new DBConnection();
        ResultSet rs = con.createStatement().executeQuery("SELECT count(*) FROM sell_invoice_not_update where invoice_no="+invoice_no);
        if(!rs.next()) return null;
        int rCount = rs.getInt(1);
        Product[] invoice_details = new Product[rCount];
        int index=-1;
        //SELECT code,product_name,wt,tounch,labor,qnty FROM sell_invoice_not_update where invoice_no=1;
        rs = con.createStatement().executeQuery("SELECT code,product_name,wt,tounch,labor,qnty FROM sell_invoice_not_update where invoice_no="+invoice_no);
        while(rs.next()){
            index++;
            invoice_details[index] = new Product(rs.getString(1), rs.getString(2), rs.getFloat(3), "NA");
            invoice_details[index].tounch = rs.getFloat(4);
            invoice_details[index].labor = rs.getFloat(5);
            invoice_details[index].qnty_if_raw = rs.getInt(6);

            //check if product_name from non_qnty..
            //SELECT id FROM raw_non_qnty where name='Bal Angati';
            invoice_details[index].no_qnty_raw = (con.getConnection().createStatement().executeQuery("SELECT id FROM raw_non_qnty where name='"+invoice_details[index].name+"';")).next();
           // System.out.println("NoQntyRaw:"+invoice_details[index].no_qnty_raw);
            
        }

        //SELECT distinct(cid),name FROM sell_invoice_not_update natural join customer_mst where invoice_no=
        String qry = "SELECT distinct(cid),name FROM sell_invoice_not_update natural join customer_mst where invoice_no="+invoice_no;
        System.out.println(qry);
        rs = con.getConnection().createStatement().executeQuery(qry);
        int cid = -1;
        if(rs.next())
            cid = rs.getInt(1);
        if(cid<0)   return null;

        
        String cname = rs.getString(2);

        //jama money
       float jamaMoney,jamaGold;
       float[] tt = getJamaGM(invoice_no);
       jamaGold=tt[0];
       jamaMoney=tt[1];

       float convertType=0;
       float convertMny=0;
       float convertGold=0;
       if(tt.length>2)
       {
           System.out.println("["+invoice_no+"]:["+tt[2]+"]::["+tt[3]+"]::["+tt[4]+"]");
           convertType=tt[2];
           convertMny=tt[3];
           convertGold=tt[4];
       }
        System.out.println("ForInve["+invoice_no+"]:["+jamaGold+"]:["+jamaMoney+"]");
        System.out.println("["+invoice_no+"]:["+jamaGold+"]:["+jamaMoney+"]");
        //old balance..
        float old_gold=0,old_money=0;
        int start_invoice_of_this = invoice_no;
        rs = con.getConnection().createStatement().executeQuery("call chain_Start("+invoice_no+",1);");
        if(rs.next())
            start_invoice_of_this=rs.getInt(1);
        rs = con.getConnection().createStatement().executeQuery("SELECT gold_ac,money_ac FROM sell_invoice_cust_old_bal where invoice_no="+start_invoice_of_this+" and cid="+cid);
        if(rs.next()){
            old_gold = rs.getFloat(1);
            old_money = rs.getFloat(2);
        }
        

        //SELECT dt FROM sell_invoice_not_update natural join sell_invoices_closed where invoice_no=
        rs = con.getConnection().createStatement().executeQuery("SELECT create_date FROM sell_invoice_date where invoice_no="+invoice_no);
        Date iDate = null;
        if(rs.next())
            iDate = rs.getDate(1);
        
        rs = con.getConnection().createStatement().executeQuery("SELECT dt FROM sell_invoice_not_update natural join sell_invoices_closed where invoice_no="+invoice_no);
        if(rs.next()){
            boolean isclosed = true;
            Date closedDate = rs.getDate(1);
            return new SellInvoice(invoice_no, cid, cname, isclosed, invoice_details, closedDate,jamaGold,jamaMoney,old_gold,old_money,convertType,convertMny,convertGold,iDate);
        }else{
            boolean isclosed = false;
            Date closedDate = null;
            return new SellInvoice(invoice_no, cid, cname, isclosed, invoice_details, closedDate,jamaGold,jamaMoney,old_gold,old_money,convertType,convertMny,convertGold,iDate);
        }

        
    }

    private synchronized static float[] getJamaGM(int invoice_no) throws Exception{
         float jamaMoney=0,jamaGold=0;;
        //SELECT tran_type,sum(amount) FROM aj_transations where invoice_no=1 and amount>0 and comment != 'Thusi Return' group by tran_type
        float[] convert=null;
          ResultSet rs = new DBConnection().createStatement().executeQuery("call chainReaction("+invoice_no+",1);");
            while(rs.next()){
                if(rs.getString(1).equals("GOLD"))
                    jamaGold=rs.getFloat(2);
                else
                    jamaMoney=rs.getFloat(2);

                String comment = rs.getString(3);
                if(comment.contains(" Convert-> "))
                    convert = processComment(comment);
            }
          if(convert!=null)
              return new float[]{jamaGold,jamaMoney,convert[0],convert[1],convert[2]};

          return new float[]{jamaGold,jamaMoney};
    }

    private static float[] processComment(String c){
        //At Sell Time 4.205 gm Fine Convert-> Rs.11059.0/- Jama
        //At Return Time 52.91 gm Fine Convert-> Rs.140000.0/- Jama
        //At Invoice Time 0.053 gm Fine Convert-> Rs.130.0/- Jama
        //3.578 gm Fine Convert-> Rs.9450.0/- Jama
        //At Sell Time Rs.970.0/- Convert-> 0.37307692 gm fine Jama


        int timeIndex = c.indexOf("Time");
        if(timeIndex>0){
            c = c.substring(timeIndex+1); //Time 4.205 gm Fine Convert-> Rs.11059.0/- Jama
            c = c.substring(c.indexOf(" ")+1); //4.205 gm Fine Convert-> Rs.11059.0/- Jama
                                                //Rs.970.0/- Convert-> 0.37307692 gm fine Jama
        }
        float mny=0,gold=0;
        String[] gm = c.split(" Convert-> ");
        //[0]4.205 gm Fine
        //[1]Rs.11059.0/- Jama
        boolean isG_To_M;
        if(gm[0].contains("gm")){
            isG_To_M = true;
            gold=getFineFrmComment(gm[0]);
            mny=getMonyeFrmComment(gm[1]);
        }else{
            isG_To_M=false;
            mny=getMonyeFrmComment(gm[0]);
            gold=getFineFrmComment(gm[1]);
        }
        float type = 1;//GTM
        if(!isG_To_M)
            type=2;//MTG

        return new float[]{type,mny,gold};
        
    }

    private static float getFineFrmComment(String c){
        //[0]4.205 gm Fine
        //[1]0.37307692 gm fine Jama
        c = c.replace(" ", "");
        c = c.replace("fine", "");
        c = c.replace("Fine", "");
        c = c.replace("Jama", "");
        c = c.replace("gm", "");
        return Float.parseFloat(c);
    }

    private static float getMonyeFrmComment(String c){
        //[1]Rs.11059.0/- Jama
        //[0]Rs.970.0/-
        c = c.replace("Rs.", "");
        c = c.replace("/-", "");
        c = c.replace("Jama", "");

        return Float.parseFloat(c);

    }

    public float getConversionRate(){
        if(amountG==0)  return 0;
        return (amountM/amountG)*10;
    }



    public void openInvoice(JPanel rootPane){
         openInvoiceNo(invoice_no, cid, rootPane);
         is_closed=false;
    }


    public void closeInvoice(JPanel rootPane){
         closeInvoiceNo(invoice_no, cid, rootPane);
         is_closed=true;
    }


    private static void openInvoiceNo(int invoice_no,int cid,JPanel rootPane){
        try{
            DBConnection con = new DBConnection();
            int op = JOptionPane.showConfirmDialog(rootPane, "Do you want Open Invoice?");
          /*
            * 0-Yes
            * 1-No
            * 2-Cacle
            */
          if(op==0)
           {    //yes
              //con.executeUpdate("insert into sell_invoices_closed values("+si.invoice_no+",now());");
              con.executeUpdate("call openInvoice("+invoice_no+","+cid+");");

              JOptionPane.showMessageDialog(rootPane, "Invoice No. "+invoice_no+" Open");

           }
        }catch(Exception e){
            //e.printStackTrace();
            System.out.println(e.getMessage());
            //No data - zero rows fetched, selected, or processed
            if(!e.getMessage().equals("No data - zero rows fetched, selected, or processed"))
                JOptionPane.showMessageDialog(rootPane, e);
        }
    }



    private static void closeInvoiceNo(int invoice_no,int cid,JPanel rootPane){
        try{
            DBConnection con = new DBConnection();
            int op = JOptionPane.showConfirmDialog(rootPane, "Do you want Close Invoice?");
          /*
            * 0-Yes
            * 1-No
            * 2-Cacle
            */
          if(op==0)
           {    //yes
              //con.executeUpdate("insert into sell_invoices_closed values("+si.invoice_no+",now());");
              con.executeUpdate("call closeInvoice("+invoice_no+","+cid+");");

              JOptionPane.showMessageDialog(rootPane, "Invoice No. "+invoice_no+" closed");
              
           }
        }catch(Exception e){
            e.printStackTrace();
            if(!e.getMessage().equals("No data - zero rows fetched, selected, or processed"))
                JOptionPane.showMessageDialog(rootPane, e);
        }
    }


}

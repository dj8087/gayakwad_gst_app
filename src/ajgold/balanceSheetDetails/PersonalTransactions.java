/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ajgold.balanceSheetDetails;

import com.implidea.DecimalFormat;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author admin
 */
public class PersonalTransactions {

    //private int differntDates1 = 0;
    Vector<PTrasction> trasactions = new Vector<PTrasction>();
    public PersonalTransactions(){}

    public void addNewTransaction(double tid,Date dd, String type, float amt,String comment,int invoice_no){
        PTrasction newpt = new PTrasction(tid, dd, type, amt, comment,invoice_no);
        boolean found = false;
        for(PTrasction pt : trasactions)
            if(found==pt.getDate().equals(newpt.getDate()))
                break;

//       if(!found)
//           differntDates1++;

        trasactions.add(newpt);
    }
      public void addNewTransaction(double tid,String cname,Date dd, String type, float amt,String comment,int invoice_no){
        PTrasction newpt = new PTrasction(tid,cname, dd, type, amt, comment,invoice_no);
        
        trasactions.add(newpt);
    }



    public PTrasction[] getAllTransactions(){
         int size = trasactions.size();
        PTrasction[] ret = new PTrasction[size];
        int index = 0;
        for(index=0;index<size;index++)
            ret[index] = trasactions.get(index);

        return ret;
    }

    public ColabrativeTrasactions[] getCollabrativeTrasactions(){
        Vector<ColabrativeTrasactions> collab = new Vector<ColabrativeTrasactions>();
        for(PTrasction pt : trasactions){
             boolean found = false;
             for(ColabrativeTrasactions cbl : collab){
                 //if(found=(cbl.getDate().equals(pt.getDate()))){

              //   System.out.println(cbl.cname);
              //   System.out.println(pt.cname);

                 if(found=(cbl.invoice_no==pt.invoice_no && cbl.cname.equals(pt.cname))){        //
                     PTrasction pt2 = pt.cloneMe();//new PTrasction(pt.tid, pt.dt, pt.type, pt.amt, "", pt.invoice_no);
                     if(pt2.type.equals("GOLD")){
                         if(pt2.amt>0){
                             if(cbl.allTransactions[0]==null)
                                 cbl.allTransactions[0] = pt2;
                             else
                                 cbl.allTransactions[0].addColabTran(pt2);//cbl.allTransactions[0].amt+=pt2.amt;
                         }
                        else {

                             if(cbl.allTransactions[2]==null)
                                 cbl.allTransactions[2] = pt2;
                             else
                                 cbl.allTransactions[2].addColabTran(pt2);//cbl.allTransactions[2].amt+=pt2.amt;
                         }

                     }else{
                         if(pt2.amt>0){

                             if(cbl.allTransactions[1]==null)
                                 cbl.allTransactions[1] = pt2;
                             else
                                 cbl.allTransactions[1].addColabTran(pt2);//cbl.allTransactions[1].amt+=pt2.amt;
                         }
                        else {

                             if(cbl.allTransactions[3]==null)
                                 cbl.allTransactions[3] = pt2;
                             else
                                 cbl.allTransactions[3].addColabTran(pt2);//cbl.allTransactions[3].amt+=pt2.amt;
                         }
                     }
                     break;
                 }
             }
             if(!found){
                 ColabrativeTrasactions cbl = new ColabrativeTrasactions(pt.cname,pt.dt,pt.invoice_no);
                 collab.add(cbl);
                 PTrasction pt2 = pt.cloneMe();//new PTrasction(pt.tid, pt.dt, pt.type, pt.amt, "", pt.invoice_no);
                     if(pt2.type.equals("GOLD")){
                         if(pt2.amt>0){
                             if(cbl.allTransactions[0]==null)
                                 cbl.allTransactions[0] = pt2;
                             else
                                 cbl.allTransactions[0].addColabTran(pt2);//cbl.allTransactions[0].amt+=pt2.amt;
                         }
                        else {

                             if(cbl.allTransactions[2]==null)
                                 cbl.allTransactions[2] = pt2;
                             else
                                cbl.allTransactions[2].addColabTran(pt2);// cbl.allTransactions[2].amt+=pt2.amt;
                         }

                     }else{
                         if(pt2.amt>0){

                             if(cbl.allTransactions[1]==null)
                                 cbl.allTransactions[1] = pt2;
                             else
                                 cbl.allTransactions[1].addColabTran(pt2);//cbl.allTransactions[1].amt+=pt2.amt;
                         }
                        else {

                             if(cbl.allTransactions[3]==null)
                                 cbl.allTransactions[3] = pt2;
                             else
                                 cbl.allTransactions[3].addColabTran(pt2);//cbl.allTransactions[3].amt+=pt2.amt;
                         }
                     }
             }
        }

        int size = collab.size();
        ColabrativeTrasactions [] ret = new ColabrativeTrasactions[size];
        int index = 0;
        for(index=0;index<size;index++)
            ret[index] = collab.get(index);

        return ret;
    }
    
}

class PTrasction{
    final Date dt;
    final String type;
    final float amt;
    String comment;
    final double tid;
    final int invoice_no;
    final String cname;
    final String note;
    Vector<PTrasction> colab_pt = null;
    public PTrasction(double tid,Date dd, String type, float amt,String comment,int invoice_no ) {
        this.tid=tid;
        this.dt = dd;
        this.type = type;
        this.amt = amt;
        this.comment=comment;
        this.invoice_no=invoice_no;
        cname=null;
        if(comment.equals("Thusi Sell"))
            note = "TS";
        else if(comment.equals("Thusi Return"))
            note = "TR";
        else if(comment.contains("Man"))
            note = "MT";

        else if(comment.contains("Fine Convert"))
            note = "GTM";


        else if(comment.contains("Advance Jama"))
            note = "AJ";

        else if(comment.contains("Advance Nave"))
            note = "AN";

        else if(comment.contains("Convert"))
            note = "MTG";

        else if(comment.contains("At Sell Time") || comment.contains("At Return Time") || comment.contains("At Invoice Time"))
            note = "PAY";

        else note = null;

        System.out.println("-------New Pt["+tid+"]:["+amt+"]:["+comment+"]:["+note+"]:["+invoice_no+"]:");

    }

    public PTrasction(double tid,String cname,Date dd, String type, float amt,String comment,int invoice_no ) {
        this.tid=tid;
        this.dt = dd;
        this.type = type;
        this.amt = amt;
        this.comment=comment;
        this.invoice_no=invoice_no;
        this.cname=cname;

        if(comment.equals("Thusi Sell"))
            note = "TS";
        else if(comment.equals("Thusi Return"))
            note = "TR";
        else if(comment.contains("Man"))
            note = "MT";

        else if(comment.contains("Fine Convert"))
            note = "GTM";

        else if(comment.contains("Convert"))
            note = "MTG";

        else if(comment.contains("Advance Jama"))
            note = "AJ";

        else if(comment.contains("Advance Nave"))
            note = "AN";


        else if(comment.contains("At Sell Time") || comment.contains("At Return Time") || comment.contains("At Invoice Time"))
            note = "PAY";

        
        else note = null;

        System.out.println("New Pt-----1["+tid+"]:["+amt+"]:["+comment+"]:["+note+"]:["+invoice_no+"]:");
    }


    public PTrasction cloneMe(){
        System.out.println("***Cloning****");
        return new PTrasction(tid, cname, dt, type, amt, comment, invoice_no);
    }
    public String getDate(){
        String date = ""+(dt.getYear()+1900)+"-"+(dt.getMonth()+1)+"-"+dt.getDate();
        return date;
    }
    public String displayDate(){
         return ""+(dt.getDate()<10 ? "0"+dt.getDate() : ""+dt.getDate() )+"-"+(dt.getMonth()+1)+"-"+(dt.getYear()+1900);
    }
    private static DecimalFormat df_gold = new DecimalFormat("########.###");
    private static DecimalFormat df_money = new DecimalFormat("########");
    public String getTblData(){
        float total = amt;
        if(colab_pt!=null){
            for(PTrasction pts : colab_pt)
                total += pts.amt;
            if(type.equals("GOLD"))
                return df_gold.format(Math.abs(total))+"(*)";
            else
                return df_money.format(Math.abs(total)) + "(*)";
        }
        
        if(note!=null){
            if(type.equals("GOLD"))
                return df_gold.format(Math.abs(amt))+"("+note+")";
            else
                return df_money.format(Math.abs(amt)) + "(" + note + ")";


        }
            if(type.equals("GOLD"))
                return df_gold.format(Math.abs(amt));
            else
                return df_money.format(Math.abs(amt));

    }

    public void addColabTran(PTrasction pt){
        if(colab_pt==null)
            colab_pt = new Vector<PTrasction>();
        colab_pt.add(pt);
    }

    public String toString(){
        if(colab_pt==null){
                return "\n"+getInfo();
        }else{
            String ret = "\n"+getInfo();
            for(PTrasction pts : colab_pt){
                ret+="\n"+pts.getInfo();
            }
            return ret;
        }
    }
    private String getInfo(){
        if(type.equals("GOLD"))
                return "("+displayDate()+") Gold "+df_gold.format(amt)+" gm  "+comment+" Inv:"+invoice_no+" ("+(int) tid+")";
            else
                return "("+displayDate()+") Rs."+df_money.format(amt)+"/- "+comment+" Inv:"+invoice_no+" ("+(int) tid+")  ";
    }
    
}


class ColabrativeTrasactions{
    final Date dt;
    final int invoice_no;
    final String cname;
    public PTrasction[] allTransactions = new PTrasction[4];   //jama 2 money 2

    public ColabrativeTrasactions(String cname,Date dt,int inv) {
        this.dt = dt;
        invoice_no=inv;
        this.cname=cname;
    }

     public String getDate(){
        return ""+(dt.getDate()<10 ? "0"+dt.getDate() : ""+dt.getDate() )+"-"+(dt.getMonth()+1)+"-"+(dt.getYear()+1900);
    }
    
}

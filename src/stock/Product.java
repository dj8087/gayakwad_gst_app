/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package stock;

import raw.RawItemCost;
import raw.RowItem;

/**
 *
 * @author admin
 */
public class Product implements Comparable<Product> {

    public String barcode, name;

    //public float cost;
    public float wt;
    public float tounch;
    public String details; //Mani Plane 2456::92.6::17::2456.0
    public int cid;
    public String cname;
    public float labor=0;
    public int invoice_no_if_selled=0;
    public int qnty_if_raw=0;
    public boolean no_qnty_raw;
    private float fine=-1;

    // for tranfer..
    public RowItem originalItm = null;
    public Product(String barcode, String name, float wt, String details) {
        this.barcode = barcode;
        this.name = name;
      //  this.cost = cost;
        this.wt = wt;
       // this.wstg=ws;
        this.details = details;
    }

    public Product() {
    }

    public RowItem[] getRawMaterial(){
        //Mani Plane 2456::17::92.60::2456.0&&...
        String[] sp1 = details.split("&&");
        RowItem[] rt = new RowItem[sp1.length];
        int j=0;
        for(String sspp : sp1){
            String[] sp2 = sspp.split("::");
            rt[j] = new RowItem(sp2[0], Float.parseFloat(sp2[2]), Float.parseFloat(sp2[3]));
            rt[j].qnty = Integer.parseInt(sp2[1]);
            j++;
        }
        return rt;
    }


    /*
                Pot 7.5::608::79.5::7.6&&
                Checks 13::92::79.0::13.0&&
                Checks 24::4::79.0::35.0&&
             panadi 350::1::57.0::340.0&&
             bol ghungaru::3::66.0::62.45&&
             kadya::1::57.0::19.0&&
             __12
            */


    
    public float getFine(){
        if(fine>0){
            return fine;
        }
        if(details.equals("NA")){

            labor=250;
            if(name.toLowerCase().indexOf("dou")==0 ||  name.toLowerCase().indexOf("du")==0)
                labor=750;


            fine=(wt*80/100);
            if(name.contains("-92")){
                fine=(wt*97/100);;
            }
        }else{
            String[] sp1 = details.split("__");
            fine = 0;
            labor=0;
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

                fine+= ((
                            ((
                            (Float.parseFloat(data[i][1])/1000)
                                *Float.parseFloat(data[i][2]))/100)
                            *Integer.parseInt(data[i][3]))
                        );
                labor+= (RawItemCost.getCostOF(data[i][0])*Integer.parseInt(data[i][3]));
                
            }
        }
        //System.out.println("Product["+barcode+"]=>["+name+"]=>["+wt+"]=>["+fine+"]=>["+labor+"]");
        return fine;
    }

    @Override
    public Product clone(){
        Product pp = new Product(barcode, name, wt, details);
        pp.labor=labor;
        pp.tounch=tounch;
        return pp;
    }

    public int compareTo(Product o) {
        return o.barcode.compareTo(barcode);
    }
    
}

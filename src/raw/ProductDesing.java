/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package raw;

import gayakwad.DBConnection;
import java.sql.ResultSet;

/**
 *
 * @author admin
 */
public class ProductDesing {

    public final int code;
    public final String name;
    public RowItem[] raw;
    public int qnty=0;
    public ProductDesing(int code, String name, RowItem[] raw) {
        this.code = code;
        this.name = name;
        this.raw = raw;
    }
    public ProductDesing(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public float getFinalWeight(){
        float ret=0;
        for(RowItem ri : raw){
            ret+= (ri.wt*ri.qnty);
        }
        return ret;
    }
    public String toString(){
        return name;
    }

    public void loadMaterilaFromDB() throws Exception{


         ResultSet p_rs = new DBConnection().createStatement().executeQuery("SELECT requirments FROM design_mst where name='"+name+"';");
            p_rs.next();
            String req = p_rs.getString(1);
            //Mani 2::14::92.60::1589.0&&
            //Mani Plane 2456::17::2456.0&&Mani Checks 2478::2::2478.0&&
            String[] sp1 = req.split("&&");
            RowItem[] rt = new RowItem[sp1.length];
            int j=0;
            for(String sspp : sp1){
                System.out.println("["+sspp+"]");
                String[] sp2 = sspp.split("::");
                rt[j] = new RowItem(sp2[0], Float.parseFloat(sp2[3]), Float.parseFloat(sp2[2]));
                rt[j].qnty = Integer.parseInt(sp2[1]);
                j++;
            }
            raw=rt;

    }


    public void loadMaterilaFromDB(String oid) throws Exception{


            ResultSet p_rs_count = new DBConnection().createStatement().executeQuery("SELECT count(itemName) FROM confermordermaterial where order_id="+oid);
            p_rs_count.next();
            int count = p_rs_count.getInt(1);
            raw = new RowItem[count];
            ResultSet p_rs = new DBConnection().createStatement().executeQuery("SELECT itemName,wt,tounch FROM confermordermaterial where order_id="+oid);
            int i=0;
            while(p_rs.next()){
                 String nm = p_rs.getString(1);
                 if(!nm.contains(" "))
                     nm=nm+" ";
                 raw[i] = new RowItem(nm, p_rs.getFloat(2), p_rs.getFloat(3));
                 i++;
            }
    }

    public void addRawMaterial(String dbString){

        if(dbString==null || dbString.length()<5)  {
            raw=null;
            return;
        }
         //Mani 2::14::92.60::1589.0&&

                        /*
                         * Raw_name::Qnty for desing::tounch::wt
                         */


            //Mani Plane 2456::17::2456.0&&Mani Checks 2478::2::2478.0&&
            String[] sp1 = dbString.split("&&");
            raw = new RowItem[sp1.length];
            int j=0;
            for(String sspp : sp1){ 
                String[] sp2 = sspp.split("::");
                raw[j] = new RowItem(sp2[0], Float.parseFloat(sp2[3]),Float.parseFloat(sp2[2]));
                raw[j].qnty = Integer.parseInt(sp2[1]);
                j++;
            }
    }
}

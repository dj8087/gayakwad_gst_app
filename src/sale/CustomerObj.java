/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sale;

import gayakwad.DBConnection;
import java.sql.ResultSet;

/**
 *
 * @author admin
 */
public class CustomerObj {

    public final int cid;
    public final String name,address,contact;
    public  float base_tounch,base_labor,gold_ac,money_ac,advance;

    public CustomerObj(int cid, String name, String address, String contact, float base_tounch, float base_labor, float gold_ac, float money_ac) {
        this.cid = cid;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.base_tounch = base_tounch;
        this.base_labor = base_labor;
        this.gold_ac = gold_ac;
        this.money_ac = money_ac;
    }

    public static CustomerObj loadCustomerObj(int cid) throws Exception{
         DBConnection con = new DBConnection();
            ResultSet rs = con.executeQuery("SELECT * FROM customer_mst where cid="+cid);
            if(rs.next()){
                rs.getInt(1);
                String name = rs.getString(2);
                String address = rs.getString(3);
                String contact = rs.getString(4);
                float base_tounch = rs.getFloat(5);
                float base_labor = rs.getFloat(6);
                float gold_ac = rs.getFloat(7);
                float money_ac = rs.getFloat(8);
                return new CustomerObj(cid, name, address, contact, base_tounch, base_labor, gold_ac, money_ac);
            }
            else return null;


    }

     public static CustomerObj[] loadAllAllCust() throws Exception
    {
         DBConnection con = new DBConnection();
            ResultSet rs = con.executeQuery("SELECT count(*) FROM customer_mst;");
            rs.next();
            int count = rs.getInt(1);
            CustomerObj[] co = new CustomerObj[count-1];  //one for phirti
            rs = con.executeQuery("SELECT * FROM customer_mst order by name;");
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
                if(cid==PhirtiTransfer.PHIRTI_CID){

                }else{
                    co[i] = new CustomerObj(cid, name, address, contact, base_tounch, base_labor, gold_ac, money_ac);
                    i++;
                }
            }
            return co;

    }

    public String toString(){
        return name;
    }
}

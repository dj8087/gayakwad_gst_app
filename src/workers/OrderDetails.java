/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package workers;

import gayakwad.DBConnection;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import raw.ProductDesing;

/**
 *
 * @author admin
 */
public class OrderDetails {

    final String workerName;
    final int wId;
    ProductDesing[] desings;
    Date orderDate,dueDate;
    int orderId;
    public OrderDetails(String wn,int id) {
        workerName = wn;
        wId = id;
    }
    public void addNewDesing(ProductDesing newpd){
        //check all names already exists..
        for(ProductDesing oldpds : desings){
            if(oldpds.name.equals(newpd.name)){
                oldpds.qnty+= newpd.qnty;
                return;
            }
        }
        
        ProductDesing[] newDesings = new ProductDesing[desings.length+1];
        System.arraycopy(desings, 0, newDesings,0,desings.length);
        newDesings[newDesings.length-1] = newpd;
        desings=newDesings;
    }

    public static OrderDetails loadOrder(int order_id){
        try {
            
            DBConnection con = new DBConnection();
            //ResultSet rs = con.executeQuery("SELECT * FROM confermorder where order_id not in (select order_id from completedorder);");
            ResultSet rs = con.executeQuery("SELECT productName,qnty,'r' FROM confermorder where order_id="+order_id);
            Vector<ProductDesing> pd = new Vector<ProductDesing>();
            while(rs.next()){
                String name = rs.getString(1);
                int qnty = rs.getInt(2);
                String req = rs.getString(3);
                ProductDesing tp = new ProductDesing(0, name);
                tp.qnty=qnty;
                pd.add(tp);
            }
            OrderDetails od = new OrderDetails("", order_id);
            od.desings = new ProductDesing[pd.size()];
            for(int i=0;i<pd.size();i++)
                od.desings[i] = pd.get(i);

            return od;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    

    
}

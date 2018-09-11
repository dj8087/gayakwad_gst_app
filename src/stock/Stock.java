/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package stock;

import gayakwad.DBConnection;
import java.util.Vector;

/**
 *
 * @author admin
 */
public class Stock {

    public Vector<Product> stock = new Vector<Product>();
    public void addProduct(Product p){
        stock.add(p);
    }

    public void saveStock() throws Exception{
        DBConnection con = new DBConnection();
        String qry = "insert into stock_mst values ";
        for(Product p : stock)
        {
            qry+= "(" + p.barcode + ",'" + p.name + "'," + p.wt + ",'" + p.details + "'), ";
            
        }
        qry = qry.substring(0,qry.lastIndexOf(","));
        con.executeUpdate(qry);
    }

    public void clearStock(){
        while(stock.size()>0)
            stock.remove(0);
    }
}

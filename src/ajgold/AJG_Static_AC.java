/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ajgold;

import gayakwad.DBConnection;
import java.sql.ResultSet;
import java.util.Date;

/**
 *
 * @author admin
 */
public class AJG_Static_AC {
    public static void updateHand(float money,float gold) throws Exception{
        DBConnection con = new DBConnection();
        //get recent.
        Date d = new Date();
         ResultSet rs = con.executeQuery("select dt from aj_gold_in_hand order by dt desc limit 1;");
        if(rs.next())
            d = rs.getDate(1);

//         String gSing = "+";
//         String mSing = "+";
//
//         if(money<0)    mSing="-";
//         if(gold<0) gSing="-";

        String qry = "update aj_gold_in_hand set gold=gold+("+gold+"),money=money+("+money+") where dt = '"+d+"'";
        //    System.out.println(qry);
        con.executeUpdate(qry);

    }
}

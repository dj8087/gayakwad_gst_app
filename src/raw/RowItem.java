/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package raw;

/**
 *
 * @author admin
 */
public class RowItem{
        public final String name;
        public  float wt,wastage;
        public int qnty,qnty_diff;
        public float tounch;
        public float labor;
        public String wid;
        public int order_id_if_out = 0;
        public RowItem(String n,float w,float tounch){
            name = n;
            wt = w;
            this.tounch=tounch;
        }

        public RowItem(String n,int qnty){
            name = n;
            wt = 0;
            tounch=0;
            this.qnty=qnty;
        }

        public String toString(){
            return name +"("+wt+")";
        }

        public String toDBString(){
            return name+"::"+qnty+"::"+tounch+"::"+wt;
        }
        public float getTotalWtInGram(){
            return (((wt) * qnty)/1000);
        }
        public float getTotalFineInGram(){
            return (((wt * (tounch/100)) * qnty)/1000);
        }
        public float getTotalLabor(){
            return (RawItemCost.getCostOF(name)*qnty);
        }

    }
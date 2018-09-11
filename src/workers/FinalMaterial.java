/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package workers;

/**
 *
 * @author admin
 */
public class FinalMaterial {

    public final String raw_name;
    public int qnty;
    public float wt=0,tounch=0;
    public FinalMaterial(String raw_name, int qnty) {
        this.raw_name = raw_name;
        this.qnty = qnty;
    }

    public FinalMaterial(String raw_name, float wt,float tounch,int qnty) {
        this.raw_name = raw_name;
        this.qnty = qnty;
        this.tounch = tounch;
        this.wt = wt;
    }


    public FinalMaterial(String raw_name, String wt,String tounch,String qnty) {
        this.raw_name = raw_name;
        this.qnty = Integer.parseInt(qnty);
        this.tounch = Float.parseFloat(tounch);
        this.wt = Float.parseFloat(wt);
    }



}

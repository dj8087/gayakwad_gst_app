/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package raw;

/**
 *
 * @author admin
 */
public class BinLaki extends RowItem {

    public BinLaki(String n,float w,float tounch){
        super(n, w, tounch);
        }

    @Override
    public float getTotalWtInGram(){
        return (((wt) * qnty));
    }
    @Override
    public float getTotalFineInGram(){
        return (((wt * (tounch/100)) * qnty));
    }
    @Override
    public float getTotalLabor(){
        return (RawItemCost.getCostOF(name)*qnty);
    }
}

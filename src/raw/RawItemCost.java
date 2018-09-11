/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package raw;

/**
 *
 * @author admin
 */
public class RawItemCost {

    public static final Object[][] rawItemCost = new Object[][]{
        {"Pot", 0.60f},
        {"Checks", 1.5f},
        {"Panadi", 20.0f},
        {"bol ghungaru", 0f},
        {"chakri", 10.0f},
        {"Checks", 1.5f},
        {"Dhana", 1.5f},
        {"ghat", 10.0f},
        {"Jav Mani", 5.0f},
        {"kadya", 0.0f},
        {"panadi", 20.0f},
        {"Plane", 0.80f},
        {"plen", 0.80f},
        {"pot", 0.60f},
    };

    public static float getCostOF(String name){
        for(Object[] obj : RawItemCost.rawItemCost){
            if(name.toLowerCase().contains(((String) obj[0]).toLowerCase())){
                return (Float) obj[1];
            }
        }
        System.out.println("Cost Not Found:"+name);
        return 0;
    }
}

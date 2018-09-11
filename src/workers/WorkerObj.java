/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package workers;

/**
 *
 * @author admin
 */
public class WorkerObj {

    public String name,address,contact,id;
    public float gold_acc,money_acc,advance;
    public WorkerObj(String name, String address, String contact, String id) {
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.id = id;
    }

    public WorkerObj(){
    }

    public String toString(){
        //System.out.println(name+"("+id+")");
        return name +" ";   //("+id+")
    }
}

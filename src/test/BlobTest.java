/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
 
import gayakwad.DBConnection;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.sql.*;

/**
 *
 * @author admin
 */
public class BlobTest implements Serializable{

    final String name;
    final int id;
    final float mark;

    public BlobTest(String name, int id, float mark) {
        this.name = name;
        this.id = id;
        this.mark = mark;
    }


    public String toString(){
        return "["+name+"]:["+id+"]["+mark+"]";
    }

    
    public static void main(String[] a) throws Exception
    {
       // write();
        read();
        update();
        read();
    }

    private static void write() throws Exception{

        DBConnection con = new DBConnection();
        PreparedStatement pre;
        BlobTest bt = new BlobTest("Draj", 1, 72.15f);
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("co"));
        oos.writeObject(bt);
        oos.flush();
        oos.close();
//        con.executeUpdate("insert into test (amcd) values ");

        pre = con.getConnection().prepareStatement("insert into test (amcd) values(?);");
        pre.setBinaryStream(1, new FileInputStream("co"),new File("co").length());
        int count = pre.executeUpdate();
        con.close();
        System.out.println("isUpdated? " + count);
    }
    private static void read() throws Exception{
        DBConnection con = new DBConnection();
        ResultSet rs = con.executeQuery("Select * from test");
        while(rs.next()){
            int id = rs.getInt(1);
            Blob bb = rs.getBlob(2);
            ObjectInputStream ois = new ObjectInputStream(bb.getBinaryStream());
            BlobTest bt = (BlobTest) ois.readObject();
            System.out.println(bt);
        }
    }

    private static void update() throws Exception{
         DBConnection con = new DBConnection();
        PreparedStatement pre;
        BlobTest bt = new BlobTest("Dheeraj", 1, 70.00f);
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("co"));
        oos.writeObject(bt);
        oos.flush();
        oos.close();
//        con.executeUpdate("insert into test (amcd) values ");

        pre = con.getConnection().prepareStatement("update test set amcd=? where idtest=4");
        pre.setBinaryStream(1, new FileInputStream("co"),new File("co").length());
        int count = pre.executeUpdate();
        con.close();
        System.out.println("isUpdated? " + count);
    }
}

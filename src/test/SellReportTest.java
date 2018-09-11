/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

// import com.mycompany.helper.* ;
// import com.mycompany.dbi.*;

public class SellReportTest {
 public static void main(String[] args) {
  HashMap hm = null;
  //HashMap<String,String> hm = null.
  // System.out.println("Usage: ReportGenerator ....");

  try {
   System.out.println("Start ....");
   // Get jasper report
   //String jrxmlFileName = "reports\\SellReport.jrxml";
   String jasperFileName = "reports\\SellReport.jasper";
    //JasperCompileManager.compileReportToFile(jrxmlFileName, jasperFileName);
System.out.println("Start ....");
   // String dbUrl = props.getProperty("jdbc.url");
   String dbUrl = "jdbc:mysql://localhost/gayakwad";
   // String dbDriver = props.getProperty("jdbc.driver");
   String dbDriver = "org.gjt.mm.mysql.Driver";
   // String dbUname = props.getProperty("db.username");
   String dbUname = "root";
   // String dbPwd = props.getProperty("db.password");
   String dbPwd = "8087";

   // Load the JDBC driver
   Class.forName(dbDriver);
   // Get the connection
   Connection conn = DriverManager
     .getConnection(dbUrl, dbUname, dbPwd);
System.out.println("Start ....");



   // Create arguments
   // Map params = new HashMap();
   hm = new HashMap();
   hm.put("cname", "Dheeraj Maralkar");
   hm.put("invoice_no", "26");
   hm.put("t_g", "11");
   hm.put("t_p", "12");
   hm.put("d_g", "13");
   hm.put("d_p", "14");
   hm.put("p_g", "15");
   hm.put("p_p", "16");
   hm.put("b_g", "17");
   hm.put("b_p", "18");
   

   
   // Generate jasper print
   JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(jasperFileName, hm, conn);
   //JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(jasperFileName, hm);

   // Export pdf file
   //JasperExportManager.exportReportToPdfFile(jprint, pdfFileName);
   JasperPrintManager.printPage(jprint, 0, true);
  // JasperViewer.viewReport(jprint);

   System.out.println("Done exporting reports to pdf");

  } catch (Exception e) {
   System.out.print("Exceptiion" + e);
   e.printStackTrace();
  }
 }
}
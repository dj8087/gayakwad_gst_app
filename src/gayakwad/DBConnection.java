package gayakwad;

import java.sql.*;
public class DBConnection
{
	private String dbName = "test";
	Connection con = null;
	Statement st = null;
	
	public DBConnection()
	throws Exception
	{
		this.dbName = "gayakwad";
		con = getConnection();
		st  = getStatement();
	}

	private static void loadDriver()
	throws Exception
	{
		Class.forName("org.gjt.mm.mysql.Driver");
	}
        
	public final Connection getConnection()
	{
		try
		{
			if(con==null)
                        {
                            loadDriver();
                            con = DriverManager.getConnection("jdbc:mysql://localhost/" + dbName, "root", "8087");
                        }
			return con;	
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
		return null;
	}
	public final Statement createStatement()
	{
		try
		{
			Statement st1=getConnection().createStatement();
			return st1;
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
		return null;
	}
	public final Statement getStatement()
	{
		try
		{
			if(st==null)
				st=getConnection().createStatement();	
			return st;
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
		return null;
	}
	
	public ResultSet executeQuery(String qry)
	throws Exception
	{
                System.out.println(qry);
		ResultSet rs = st.executeQuery(qry);
		return rs;
	}
	
	public void executeUpdate(String qry)
	throws Exception
	{
               System.out.println("["+qry+"]");
               st.executeUpdate(qry);
	}
	
	public void close()
	throws Exception
	{
		con.close();
                con=null;
                st=null;
	}
}
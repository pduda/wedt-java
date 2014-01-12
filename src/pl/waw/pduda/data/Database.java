package pl.waw.pduda.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//public
class Database
{
	//public static DatabaseTest databaseTest;
	Connection connection = null;
	String Pass;
	String User;
	String Url;
	String DBName;
	public Database(String strUrl,String dbName, String user,String pass) throws DatabaseException{
		this.Url=strUrl;
		this.Pass=pass;
		this.User=user;
		this.DBName=dbName;
		try{
			//Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Class.forName("com.mysql.jdbc.Driver");//.newInstance();
			
			String address="jdbc:mysql://"+this.Url+this.DBName;
			
			connection = DriverManager.getConnection(address,user,pass);
		}
		catch(Exception e)
		{
			throw new DatabaseException(DatabaseException.DATABASE_FAILURE,"B��d po��czenia z baz� danych",true);
		}

	}
	public String select(String query ){
		String retur="";
		try{
			Statement statement = connection.createStatement();
			
			ResultSet rs = statement.executeQuery(query);
			
			while(rs.next()){
				
				retur+=rs.toString();
			}
			
			rs.close();	
			return retur;
		}catch(SQLException e){
			System.out.println("problem z czytaniem z bazy " + e);
			System.exit(-1);
			return retur;
		}
	}		
	public void disconnect() throws DatabaseException{
			
			try{
				connection.close();
			}
			catch(SQLException e){
				throw new DatabaseException(DatabaseException.DATABASE_FAILURE,"Po��czenie z baz� danych zosta�o stracone",true);
			}
			
	}
}
package pl.waw.pduda.database;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database 
{
	static final String USER = "wedt";
	static final String PASS = "wedt123";
	static final String BASE = "jdbc:mysql://serwer1363634.home.pl/13777141_wedt?characterEncoding=UTF-8";
	
	private java.sql.Connection conn = null;
	private java.sql.Statement stmt = null;
	private ResultSet rs =null;

	public Database() throws Exception
	{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		
		this.conn = DriverManager.getConnection(BASE,USER,PASS);
    	
    	this.stmt = conn.createStatement();
	}
	
	public ResultSet select(String query) throws Exception
	{

		this.rs = this.stmt.executeQuery(query);
        
        return rs;
	}
	public void insert(String query) throws Exception
	{
		System.out.println(query);
		//this.stmt.executeUpdate(query);

	}
	public void close() throws Exception
	{
		if(this.rs!=null)
			this.rs.close();
		this.stmt.close();
		this.conn.close();
	}
	public void insertBlockText(String content,String c,int url_id) throws Exception
	{
		String q = "insert into www_blocks_parser (`content`,`class`,`url_id`) Values(?,?,?);";
		
		PreparedStatement ps=this.conn.prepareStatement(q);
		ps.setString(1,content);
		ps.setString(2,c);
		ps.setInt(3,url_id);
		ps.executeUpdate();
		
		//System.out.println(ps);
	}
	public void insertClassifierResult(String page,int mode) throws Exception
	{
		String q = "insert into results_classifier (`page`,`mode`) Values(?,?);";
		
		PreparedStatement ps=this.conn.prepareStatement(q);
		ps.setString(1,page);
		ps.setInt(2,mode);
		ps.executeUpdate();
	}

}

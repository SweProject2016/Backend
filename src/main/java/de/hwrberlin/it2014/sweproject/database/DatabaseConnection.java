package de.hwrberlin.it2014.sweproject.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
	private Connection con;
	
	/**
	 * Connects to MySQL-DB using JDBC Driver
	 * 
	 * @author Dominik Habel
	 *
	 * @param host Host IP or Domain
	 * @param database DB name
	 * @param user user name
	 * @param pwd password as plain text
	 * @return true if successfully connected, false if not
	 */
	public boolean connectToMysql(String host, String database, String user, String pwd){
		assert con == null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String connectionCommand = "jdbc:mysql://" + host + "/" + database + "?user=" + user + ((pwd != null) ? "&password=" + pwd : "");
			con = DriverManager.getConnection(connectionCommand);
			return true;

		} catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Tests if connection still connected
	 * 
	 * @author Dominik Habel
	 *
	 * @return true if connected, false if not
	 */
	public boolean isConnected(){
		try {
			return con != null && !con.isClosed();
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Execute query on database (SQL-Select)
	 * 
	 * @author Dominik Habel
	 *
	 * @param query SQL code
	 * @return ResultSet of query result
	 * @throws SQLException
	 */
	public ResultSet executeQuery(String query) throws SQLException{
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(query);
		return rs;
	}
	
	/**
	 * Execute Update on database (SQL-Update, -Delete, -Insert)
	 * 
	 * @author Dominik Habel
	 *
	 * @param query SQL code
	 * @return either (1) the row count for SQL Data Manipulation Language (DML) statements or (2) 0 for SQL statements that return nothing
	 * @throws SQLException
	 */
	public synchronized int executeUpdate(String query) throws SQLException{
		Statement st = con.createStatement();
		return st.executeUpdate(query);
	}
	
	/**
	 * Closes database connection
	 * @author Dominik Habel
	 *
	 */
	public void close(){
		try {
			con.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}

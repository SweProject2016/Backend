package de.hwrberlin.it2014.sweproject.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

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
	 * Converts a ResultSet into an ArrayList of Judgements
	 * 
	 * @author Dominik Habel
	 *
	 * @param rs result of a query (query must be SELECT * [...])
	 * @return ArrayList of Judgement
	 * @throws SQLException 
	 */
	public ArrayList<Judgement> convertResultSetToJudgementList(ResultSet rs) throws SQLException{
		ArrayList<Judgement> list = new ArrayList<Judgement>();
		HashMap<Integer, LawSector> ls = new HashMap<Integer, LawSector>();
		HashMap<Integer, Committee> c = new HashMap<Integer, Committee>();
		Judgement judge = new Judgement(null, null);
		while(rs.next()){
			judge.setFileReference(rs.getString("file_reference"));
			judge.setKeywords(rs.getString("keywords"));
			judge.setOffence(rs.getString("offence"));
			judge.setSentence(rs.getString("sentence"));
			judge.setPdfFileName(rs.getString("pdf_filename"));
			judge.setPdfLink(rs.getString("pdf_link"));
			judge.setDate(rs.getDate("date"));
			judge.setPageRank(rs.getFloat("page_rank"));
			
			int id = rs.getInt("law_sector");
			LawSector sector = ls.get(id);
			if(sector==null){
				ResultSet newLS = executeQuery("SELECT name FROM tbl_law_sector WHERE ID="+id+";");
				LawSector dummy = new LawSector(newLS.getString("name"));
				ls.put(id, dummy);
				judge.setSector(dummy);
			}else{
				judge.setSector(sector);
			}
			
			int id2=rs.getInt("committee");
			Committee committee = c.get(id2);
			if(committee==null){
				ResultSet newC = executeQuery("SELECT name FROM tbl_committee WHERE ID="+id2+";");
				Committee dummy = new Committee(newC.getString("name"));
				c.put(id2, dummy);
				judge.setComittee(dummy);
			}else{
				judge.setComittee(committee);
			}
			
			list.add(judge);
		}
		return list;
	}
	
	/**
	 * Converts a ResultSet into an ArrayList of LawSector
	 * 
	 * @author Dominik Habel
	 *
	 * @param rs result of a query (query must be SELECT * [...])
	 * @return ArrayList of LawSector
	 * @throws SQLException 
	 */
	public ArrayList<LawSector> convertResultToLawSectorList(ResultSet rs) throws SQLException{
		ArrayList<LawSector> list = new ArrayList<LawSector>();
		while(rs.next()){
			LawSector dummy = new LawSector(rs.getString("name"));
			list.add(dummy);
		}
		return list;
	}
	
	/**
	 * Converts a ResultSet into an ArrayList of Committee
	 * 
	 * @author Dominik Habel
	 *
	 * @param rs result of a query (query must be SELECT * [...])
	 * @return ArrayList of Committee
	 * @throws SQLException 
	 */
	public ArrayList<Committee> convertResultToCommitteeList(ResultSet rs) throws SQLException{
		ArrayList<Committee> list = new ArrayList<Committee>();
		while(rs.next()){
			Committee dummy = new Committee(rs.getString("name"));
			list.add(dummy);
		}
		return list;
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

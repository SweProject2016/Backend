package de.hwrberlin.it2014.sweproject.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import de.hwrberlin.it2014.sweproject.model.Result;

public class TableResultsSQL {
	
	 private final static String INSERT_QUERY_STRING = "INSERT INTO tbl_results "
	            + "(user_input, picked_file, similarity, date, user_rating)"
	            + "VALUES (?,?,?,?,?)";
	 private final static String SAVE_USER_RATING_STRING = "UPDATE tbl_results SET user_rating = ? WHERE id = ?"; 
	 
	 /**
	  * Generiert ein PreparedStatement f�r ein Insert in tbl_results
	  * @author Dominik Habel
	  *
	  * @param res das Result, das eingef�gt werden soll
	  * @param con Datenbank-Verbindung
	  * @return das generierte PreparedStatement
	  * @throws SQLException
	  */
	 public static PreparedStatement prepareInsert(final Result res, final DatabaseConnection con, int autoGeneratedKeys) throws SQLException{
        PreparedStatement stmt = null;
        try {
            Connection c = con.getConnection();
            stmt = c.prepareStatement(TableResultsSQL.INSERT_QUERY_STRING,autoGeneratedKeys);
            stmt.setString(1,res.getUserInput());
            stmt.setInt(2, TableJudgementSQL.getJudgementIdByFileReference(res.getJudgement().getFileReference(), con));
            stmt.setFloat(3, res.getSimilarity());
            stmt.setDate(4, new Date(res.getDate().getTime()));
            if(res.getUserRating()==0.0f){
            	stmt.setNull(5, java.sql.Types.FLOAT);
            }else{
            	stmt.setFloat(5, res.getUserRating());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stmt;
     }
	 
	 public static PreparedStatement prepareInsert(final Result res, final DatabaseConnection con) throws SQLException{
		 return prepareInsert(res,con,Statement.NO_GENERATED_KEYS);
	 }
	
	 /**
	  * Generiert SQL-Insert-Code zu einem Result
	  * @author Dominik Habel
	  *
	  * @param result das einzuf�gende Result
	  * @return der SQL-Code
	  */
	public static String getInsertSQLCode(Result result)
	{
		String sql = "INSERT INTO tbl_results (user_input, picked_file, similarity, date, user_rating) SELECT ";
		sql+="'"+result.getUserInput()+"',";
		sql+="j.id,";
		sql+=result.getSimilarity()+",'"+result.getDate()+"',";
		sql+=(result.getUserRating()==0.0f)?"null":result.getUserRating();
		sql+=" FROM tbl_judgement j WHERE j.file_reference = '"+result.getJudgement().getFileReference()+"';";
		return sql;
	}
	
	/**
	 * @author Max Bock & Nico Pfeiffer
	 * @param id des Results
	 * @return Sql-Select-Query
	 */
	public static String getSelectSQLCode(int id)
	{
		String sql="SELECT * FROM tbl_results WHERE id = " + id + " LIMIT 1";
		return sql;
	}
	
	/**
	 * @author Max Bock & Nico Pfeiffer
	 * @param bewertetes Result-Objekt
	 * @return Sql-Update-Query
	 */
	public static String getUpdateSQLCodeForUserRating(Result result)
	{
		String sql="UPDATE tbl_results SET user_rating = '";
		sql+=result.getUserRating();
		sql+="' WHERE id = "+ result.getID();
		return sql;
	}
	/* for the future: 
	 * after rating you receive the user_input, user_rating and caseID from the frontend 
	 * it would be better to save the result only after the user rate it or otherwise directly delete it
	 */
	
	public static int saveUserRating(int id, float rating, DatabaseConnection con){
		Connection c = con.getConnection();
		PreparedStatement stmt = null;
		int rows = -1;
		try{
			stmt = c.prepareStatement(SAVE_USER_RATING_STRING);
			stmt.setFloat(1, rating);
			stmt.setInt(2, id);
			rows = stmt.executeUpdate();
		} catch(SQLException e){
			e.printStackTrace();
			return rows;
		}
		return rows;
	}
	
	
}

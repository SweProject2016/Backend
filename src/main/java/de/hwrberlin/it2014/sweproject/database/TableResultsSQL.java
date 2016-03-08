package de.hwrberlin.it2014.sweproject.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import de.hwrberlin.it2014.sweproject.model.Result;

public class TableResultsSQL {
	
	 private final static String INSERT_QUERY_STRING = "INSERT INTO tbl_results "
	            + "(user_input, picked_file, similarity, date, user_rating)"
	            + "VALUES (?,?,?,?,?)";
	 
	 /**
	  * Builds a PreparedStatement for an Insert into tbl_results
	  * @author Dominik Habel
	  *
	  * @param res the result that will be inserted
	  * @param con database connection
	  * @return generated PreparedStatement
	  * @throws SQLException
	  */
	 public static PreparedStatement prepareInsert(final Result res, final DatabaseConnection con) throws SQLException{
	        PreparedStatement stmt = null;
	        try {
	            Connection c = con.getConnection();
	            stmt = c.prepareStatement(TableResultsSQL.INSERT_QUERY_STRING);
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
	
	public static String getInsertSQLCode(Result result){
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
	 * @param id of a result which is not rated
	 * @return Sql-Select-Query
	 */
	public static String getSelectSQLCode(int id){
		//TODO
		//get Result From DB by ID
		String sql="SELECT * FROM tbl_result WHERE id = " + id + " LIMIT 1";
		return sql;
	}
	
	/**
	 * @author Max Bock & Nico Pfeiffer
	 * @param rated result
	 * @return Sql-Update-Query
	 */
	public static String getUpdateSQLCodeForUserRating(Result result){
		//TODO
		//update the userRating
		String sql="UPDATE tbl_results SET user_rating = '";
		sql+=result.getUserRating();
		sql+="' WHERE id = "+ result.getID();
		return sql;
	}
	/* for the future: 
	 * after rating you receive the user_input, user_rating and caseID from the frontend 
	 * it would be better to save the result only after the user rate it or otherwise directly delete it
	 */
}

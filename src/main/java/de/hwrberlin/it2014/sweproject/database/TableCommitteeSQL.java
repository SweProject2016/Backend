package de.hwrberlin.it2014.sweproject.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.hwrberlin.it2014.sweproject.model.Committee;

public class TableCommitteeSQL {
	
	public static final String SELECT_BY_NAME_QUERY_STRING = "SELECT * FROM tbl_committee WHERE name = ?";
	
	public static String getInsertSQLCode(Committee com){
		return "INSERT INTO tbl_committee (name) VALUES ('"+com.getName()+"');";
	}
	
	/**
	 * Findet die ID eines Committees anhand des Namens heraus
	 * @author Dominik Habel
	 *
	 * @param com das zu untersuchende Commitee
	 * @param con die Datenbank-Verbindung
	 * @return die Datenbank ID zum übergebenen Committee
	 */
	public static int getCommitteeIdByName(final Committee com, final DatabaseConnection con){
	        Connection c =  con.getConnection();
	        Integer id = null;
	        try {
	            PreparedStatement stmt = c.prepareStatement(TableCommitteeSQL.SELECT_BY_NAME_QUERY_STRING);
	            stmt.setString(1,com.getName());
	            ResultSet rs = stmt.executeQuery();
	            while(rs.next()){
	                id = rs.getInt("id");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return id;
	    }
}

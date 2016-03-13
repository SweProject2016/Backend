package de.hwrberlin.it2014.sweproject.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

import de.hwrberlin.it2014.sweproject.model.enums.LawSector;

public class TableLawSectorSQL {
	
	private final static String SELECT_BY_NAME_QUERY = "SELECT * FROM tbl_law_sector WHERE name = ?";
	private final static String SELECT_BY_ID_QUERY = "SELECT * FROM tbl_law_sector WHERE id = ?";

	
	public static PreparedStatement prepareSelectByName(LawSector ls, DatabaseConnection con){
		Connection c = con.getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = c.prepareStatement(SELECT_BY_NAME_QUERY);
			stmt.setString(1,ls.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stmt;
	}
	
	public static int getLawSectorIdByName(LawSector ls, DatabaseConnection con){
		int id = -1;
		PreparedStatement stmt = prepareSelectByName(ls, con);
		ResultSet rs;
		try {
			rs = stmt.executeQuery();
			while(rs.next()){
				id = rs.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public static LawSector getLawSectorById(int id, DatabaseConnection con){
		Connection c = con.getConnection();
		PreparedStatement stmt = null;
		LawSector ls = null;
		try{
			stmt = c.prepareStatement(SELECT_BY_ID_QUERY);
			stmt.setInt(1,id);
			ResultSet rs = stmt.executeQuery();
			String val = null;
			while(rs.next()){
				val = rs.getString("name");
			}
			ls = LawSector.get(val);
		} catch(Exception e){
			e.getMessage();
		}
		return ls;
	}

}

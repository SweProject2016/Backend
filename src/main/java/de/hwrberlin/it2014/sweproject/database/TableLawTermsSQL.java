package de.hwrberlin.it2014.sweproject.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TableLawTermsSQL {
	
	private final static String SELECT_BY_TERM_QUERY = "SELECT * FROM tbl_law_terms WHERE term = ?";
	private final static String SELECT_ALL_TERMS_QUERY= "SELECT * FROM tbl_law_terms";
	
	/**
	 * Abfrage-Methode an die Datenbank
	 * 
	 * @author Christian Schlesing
	 * @param term Das gesuchte Wort
	 * @param con Die Datenbankverbindung
	 * @return ein PreparedStatement mit vorbereitetem Suchterm
	 */
	public static PreparedStatement prepareSelect(String term, DatabaseConnection con){
		Connection c = con.getConnection();
		PreparedStatement stmt = null;
		try{
			stmt = c.prepareStatement(SELECT_BY_TERM_QUERY);
			stmt.setString(1, term);
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return stmt;
	}
	
	/**
	 * Abfrage, die ungefiltert alle Datensaetze der Tabelle ausgibt
	 * 
	 * @author Christian Schlesing
	 * @param con Die Datebankverbindung
	 * @return eine Liste aller DS der Tabelle law_terms
	 */
	public static ArrayList<String> getAllLawTerms(DatabaseConnection con){
		ArrayList<String> terms = new ArrayList<>();
		Connection c = con.getConnection();
		try{
			PreparedStatement stmt = c.prepareStatement(SELECT_ALL_TERMS_QUERY);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				terms.add(rs.getString("term"));
			}
		} catch(SQLException e){
			e.printStackTrace();
		}
		return terms;
	}

}

package de.hwrberlin.it2014.sweproject.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;


public class ThesaurusDatabase {
	
	private final static String FIND_SYNONYMS_FOR_TERM_PS = "SELECT * FROM openthesaurus_term, openthesaurus_synset, openthesaurus_term term2 "
			+ "WHERE openthesaurus_synset.is_visible = 1 AND "
			+ "openthesaurus_synset.id = openthesaurus_term.synset_id AND "
			+ "openthesaurus_term.synset_id AND "
			+ "term2.synset_id = openthesaurus_synset.id AND "
			+ "term2.word = ?";
	
	public static Set<String> getSynonymsForTerm(String term, DatabaseConnection con){
		Connection c = con.getConnection();
		Set<String> synonyms = new HashSet<>();
		PreparedStatement stmt = null;
		try{
			stmt = c.prepareStatement(FIND_SYNONYMS_FOR_TERM_PS);
			stmt.setString(1,term);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				synonyms.add(rs.getString("word"));
			}
		} catch(SQLException e){
			e.printStackTrace();
		} finally{
			try {
				c.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return synonyms;
		
	}

}

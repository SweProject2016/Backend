package de.hwrberlin.it2014.sweproject.database;

import java.util.ArrayList;

import de.hwrberlin.it2014.sweproject.model.Judgement;

public class TableJudgementSQL {

	public static String getInsertSQLCode(Judgement judgement){
		String sql = "INSERT INTO tbl_judgement (file_reference, committee, law_sector," + "date, sentence, offence, page_rank, pdf_filename," + "pdf_link, keywords) SELECT ";
		sql += "'" + judgement.getFileReference() + "',";
		// TODO: committee+law_sector-parse
		sql += "c.id,ls.id,";
		sql += "'" + judgement.getDate() + "',";
		sql += "'" + judgement.getSentence() + "',";
		sql += "'" + judgement.getOffence() + "',";
		sql += judgement.getPageRank() + ",";
		sql += "'" + judgement.getPdfFileName() + "',";
		sql += "'" + judgement.getPdfLink() + "',";
		sql += "'" + judgement.getKeywords();
		sql += "' FROM tbl_committee c, tbl_law_sector ls WHERE c.name='" + judgement.getComittee().getName();
		sql += "' AND ls.name='" + judgement.getSector().getName() + "';";
		System.out.println(sql);
		return sql;
	}

	/**
	 * die Funktion baut eine SQL-Anfrage mit der die vom Nutzer eingegebenen Keywords mit der Datenbank verglichen werden k�nnen
	 * 
	 * @author Danilo G�nzel
	 * @param keywords Sind die von der Nutzereingabe erhaltenen Schl�sselw�rter(ohne synonyme)
	 * @param lawsector Der Rechtsbereich aus welchem die gefundenen F�lle stammen sollen
	 * @return Als R�ckgabe erh�lt man die Datenbankanfrage, die die Zeilen nach den Schl�sselworttreffern ausw�hlt
	 */
	public static String getSelectSQLCode(ArrayList<String> keywords, String lawsector){
		String query = "SELECT * FROM tbl_judgement WHERE CONTAINS((sentence, offence, keywords),";
		String queryKeywords = new String();

		for(String key : keywords) {
			queryKeywords += "'" + key.toLowerCase() + "' or ";
		}

		int len = 0;
		len = queryKeywords.length();
		queryKeywords = queryKeywords.substring(0, len - 3);
		if(lawsector != null && !lawsector.isEmpty()) {
			queryKeywords += ") AND LOWER(law_sector) = LOWER('" + lawsector + "');";
		} else {
			queryKeywords += ");";
		}

		query += queryKeywords;

		return query;
	}
}

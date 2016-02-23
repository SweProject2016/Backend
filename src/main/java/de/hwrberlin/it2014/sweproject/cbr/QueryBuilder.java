package de.hwrberlin.it2014.sweproject.cbr;

import java.util.ArrayList;

import de.hwrberlin.it2014.sweproject.synonymysationing.ThesaurusLoader;

/**
 * die Klasse baut eine SQL-Anfrage mit der die vom Nutzer eingegebenen Keywords mit der Datenbank verglichen werden können
 * 
 * @author Danilo Günzel
 *
 */
public class QueryBuilder {

	/**
	 * 
	 * @param keywords Sind die von der Nutzereingabe erhaltenen Schlüsselwörter(ohne synonyme)
	 * @return Als Rückgabe erhält man die Datenbankanfrage, die die Zeilen nach den Schlüsselworttreffern auswählt
	 */
	public static String buildQuery(ArrayList<String> keywords){
		String query = "SELECT * FROM tbl_judgement WHERE CONTAINS((sentence, offence, keywords),";
		String queryKeywords = " '";

		for(String key : keywords) {
			queryKeywords += key.toLowerCase() + " or ";
			for(String syn : ThesaurusLoader.getSynonyms(key)) {
				queryKeywords += syn.toLowerCase() + " or ";
			}
		}

		if(queryKeywords.endsWith(" or ")) {
			int len = 0;
			len = queryKeywords.length();
			queryKeywords = queryKeywords.substring(0, len - 3);
			queryKeywords += "');";
		}

		return query;
	}
}

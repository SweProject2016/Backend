package de.hwrberlin.it2014.sweproject.cbr;

import java.util.ArrayList;

import de.hwrberlin.it2014.sweproject.synonym.ThesaurusLoader;

/**
 * die Klasse baut eine SQL-Anfrage mit der die vom Nutzer eingegebenen Keywords mit der Datenbank verglichen werden k�nnen
 * 
 * @author Danilo G�nzel
 *
 */
public class QueryBuilder {

	/**
	 * 
	 * @param keywords Sind die von der Nutzereingabe erhaltenen Schl�sselw�rter(ohne synonyme)
	 * @param lawsector Der Rechtsbereich aus welchem die gefundenen F�lle stammen sollen
	 * @return Als R�ckgabe erh�lt man die Datenbankanfrage, die die Zeilen nach den Schl�sselworttreffern ausw�hlt
	 */
	public static String buildQuery(ArrayList<String> keywords, String lawsector){
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
			if(!lawsector.isEmpty()){
				queryKeywords += "') AND law_sector LIKE '" + lawsector + "';";
			}else{
				queryKeywords += "');";	
			}
		}
		
		query += queryKeywords;

		return query;
	}
}

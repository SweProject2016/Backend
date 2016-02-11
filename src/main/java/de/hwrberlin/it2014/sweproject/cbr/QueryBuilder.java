package de.hwrberlin.it2014.sweproject.cbr;

import de.hwrberlin.it2014.sweproject.thesaurus.ThesaurusLoader;
import java.util.ArrayList;

/**
 * die Klasse baut eine SQL-Anfrage mit der die vom Nutzer eingegebenen Keywords mit der Datenbank verglichen werden k�nnen
 * @author Danilo G�nzel
 *
 */
public class QueryBuilder {
	
	ArrayList<String> keysWithSyn = null;
	
	/**
	 * Konstruktor erstellt eine leere ArrayList, in der nach Methodenaufruf von BuildQuery alle Schl�sselw�rter und deren Synonyme stehen
	 */
	public QueryBuilder(){
		this.keysWithSyn = new ArrayList<String>();
	}
	/**
	 * 
	 * @param keywords Sind die von der Nutzereingabe erhaltenen Schl�sselw�rter(ohne synonyme)
	 * @return Als R�ckgabe erh�lt man die Datenbankanfrage, die die Zeilen nach den Schl�sselworttreffern ausw�hlt
	 */
	private String buildQuery(String[] keywords){
		String query = "SELECT * FROM tbl_judgement WHERE CONTAINS((sentence, offence, keywords),";
		String queryKeywords = " '";
		
		for(String key : keywords){
//			this.keysWithSyn.add(key);
			queryKeywords += key.toLowerCase() + "','";
			for(String syn : ThesaurusLoader.getSynonyms(key)){
//				keysWithSyn.add(syn);
				queryKeywords += syn.toLowerCase() + "','";
			}
		}
		
		if(queryKeywords.endsWith("','")){
			int len = 0;
			len = queryKeywords.length();
			queryKeywords = queryKeywords.substring(0, len-1);
		}
		
		return query;
	}
}

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
	 * @param keywords Sind die von der Nutzereingabe erhaltenen Schl�sselw�rter
	 * @return Als R�ckgabe erh�lt man die Datenbankanfrage, die die Spalten nach den Schl�sselworttreffern ausw�hlt
	 */
	private String buildQuery(String[] keywords){
		String query = "SELECT * FROM TABLE_NAME WHERE ";
		
		for(String key : keywords){
			this.keysWithSyn.add(key);
			for(String syn : ThesaurusLoader.getSynonyms(key)){
				keysWithSyn.add(syn);
			}
		}
		
		
		
		return query;
	}
}

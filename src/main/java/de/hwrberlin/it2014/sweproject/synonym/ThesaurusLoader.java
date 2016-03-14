package de.hwrberlin.it2014.sweproject.synonym;

import java.util.ArrayList;

import de.hwrberlin.it2014.sweproject.database.DatabaseConnection;
import de.hwrberlin.it2014.sweproject.database.ThesaurusDatabase;

/**
 * Diese Klasse stellt die Verbindung zu der (lokalen)Thesaurus-Datenbank dar, um eingegebene Schl�sselw�rter auch durch Synonyme finden zu k�nnen.
 * @author Danilo
 *
 */
public class ThesaurusLoader {
	
	/**
	 * 
	 * @param keyword ist ein Schl�sselwort, f�r das die Synonyme heraus gefunden werden sollen
	 * @return Der R�ckgabewert ist ein ArrayList von Strings, die die gefundenen Synonyme enth�lt
	 */
	public static ArrayList<String> getSynonyms(final String keyword){
        DatabaseConnection con = new DatabaseConnection();
        ArrayList<String> synonyms = new ArrayList<String>();
        synonyms.addAll(ThesaurusDatabase.getSynonymsForTerm(keyword, con));
        return synonyms;
}
	
}

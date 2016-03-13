package de.hwrberlin.it2014.sweproject.synonym;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
        StringBuffer response = new StringBuffer();
        Set<String> syns = new HashSet<>();
        ArrayList<String> synonyms = new ArrayList<String>();
        try {
            String url = "https://www.openthesaurus.de/synonyme/search?q="+ keyword +"&format=application/json";
 
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
            con.setRequestMethod("GET");
 
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
 
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            
            
        // parsing response
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(response.toString());
        JSONArray synsets = (JSONArray) json.get("synsets");
        for(int i=0;i<synsets.size();i++){
        	JSONObject aSynset = (JSONObject) synsets.get(i);
        	JSONArray terms = (JSONArray) aSynset.get("terms");
        	for(int j=0;j<terms.size();j++){
        		JSONObject aTerm = (JSONObject) terms.get(j);
        		String term = (String) aTerm.get("term");
        		syns.add(term);
        	}
        }
        
        } catch (ParseException | IOException e) {
            e.printStackTrace();
		}
        //using Set for unique entries
        synonyms.addAll(syns);

        return synonyms;
}
	
}

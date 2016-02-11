package de.hwrberlin.it2014.sweproject.thesaurus;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Diese Klasse stellt die Verbindung zu der (lokalen)Thesaurus-Datenbank dar, um eingegebene Schlüsselwörter auch durch Synonyme finden zu können.
 * @author Danilo
 *
 */
public class ThesaurusLoader {
	
	/**
	 * 
	 * @param keyword ist ein Schlüsselwort, für das die Synonyme heraus gefunden werden sollen
	 * @return Der Rückgabewert ist ein ArrayList von Strings, die die gefundenen Synonyme enthält
	 */
	public static ArrayList<String> getSynonyms(String keyword){
		ArrayList<String> syns = new ArrayList<String>();
		ArrayList<String> lines = new ArrayList<String>();
		String line = null;
		FileReader fileReader;
		try {
			fileReader = new FileReader("openthesaurus.txt");
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			while ((line = bufferedReader.readLine()) != null) {
				lines.add(line);
			}
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	     
		for(String entry : lines){
			if(entry.contains(keyword)){
				for(String singleWord : entry.split(";")){
					singleWord.replaceAll(";", "");
					singleWord.trim();
					syns.add(singleWord.toLowerCase());
				}
			}
		}
		
		return syns;
	}
	
}

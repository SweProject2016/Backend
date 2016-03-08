package de.hwrberlin.it2014.sweproject.synonym;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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
	public static ArrayList<String> getSynonyms(String keyword){
		ArrayList<String> syns = new ArrayList<String>();
		ArrayList<String> lines = new ArrayList<String>();
		File thesaurusFile = new File("src/main/java/de/hwrberlin/it2014/sweproject/synonym/openthesaurus.txt");
		String thesaurusFilePath = thesaurusFile.getAbsolutePath();
		String line = null;
		FileReader fileReader;
		try {
			//fileReader = new FileReader("openthesaurus.txt");
			fileReader = new FileReader(thesaurusFilePath);
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
			if(entry.split(";").equals(keyword)){
			//if(entry.contains(keyword)){
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

package de.hwrberlin.it2014.sweproject.synonym;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

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
		try {
			
            ClassLoader classLoader = LawTester.class.getClassLoader();
        	File file = new File(classLoader.getResource("files/openthesaurus.txt").getFile());
            
        	Scanner scan = new Scanner(file);
        	
        	while(scan.hasNextLine()){
        		String aLine = scan.nextLine();
        		lines.add(aLine);
        	}
			
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

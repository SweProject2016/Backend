package de.hwrberlin.it2014.sweproject.synonymysationing;

import java.util.ArrayList;

public class LawTester {
	
	/**
	 * Die Methode bekommt ein Schluesselwort und ueberprueft ob dieses ein juristischer Ausdruck ist 
	 * @param keyword ein Schluesselwort zur Ueberpruefung
	 * @return wenn der Schluessel ein juristisches Wort ist, wird true zurueckgegeben, sonst false
	 */
	public static boolean testIfWordIsLawTerm(String keyword){
		boolean result = false;
		
		//TODO test in multiple lexi wether keyword is an existing lawterm
		for(String entry : getLexiEntries()){
			if(keyword == entry){
				return result = true;
			}
		}
		return result;
	}
	
	/**
	 * Die interne Methode nimmt eines der juristischen Lexika und fügt dessen Inhalt in eine ArrayList ein 
	 * @return ArrayList<String> aList mit Inhalt eines Lexikons
	 */
	private static ArrayList<String> getLexiEntries(){
		return null;
	}

}

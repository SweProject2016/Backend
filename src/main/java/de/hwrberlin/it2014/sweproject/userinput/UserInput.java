package de.hwrberlin.it2014.sweproject.userinput;

import de.hwrberlin.it2014.sweproject.synonymysationing.LawTester;
import java.util.ArrayList;

/**
 * Klasse für Nutzereingabe
 *
 * @author Maximilian Kramp, Paul Piske
 * @since 14.02.2016 12:00:00
 */
public class UserInput {

	/**
	 * Default Constructor
	 *
	 * @author Maximilian Kramp, Paul Piske
	 * @since 14.02.2016 12:00:00
	 */
	public UserInput(){

	}

	/**
	 * User input from URL to String Array
	 *
	 * @author Maximilian Kramp, Paul Piske
	 * @since 14.02.2016 12:00:00
	 * @param input URL String
	 * @return User input as array of Strings
	 */
	public String[] getUserInput(final String input){
		String regex = "%20+";
		return input.split(regex);
	}

	/**
	 * Beschreibung
	 *
	 * @author Maximilian Kramp, Paul Piske
	 * @since 01.03.2016 18:00:00
	 * @param input URL String
	 * @return 
	 */
	public ArrayList<String> getLawTermsFromInput(String[] input){
		ArrayList<String> lawTerms = new ArrayList<String>();
		LawTester lt = new LawTester();
		
		for(String term : input){
			if(lt.testIfWordIsLawTerm(term)){
				lawTerms.add(term);
			}
		}
		return lawTerms;
	}

}

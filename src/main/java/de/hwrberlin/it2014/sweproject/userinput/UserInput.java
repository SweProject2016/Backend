package de.hwrberlin.it2014.sweproject.userinput;

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

}

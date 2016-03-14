package de.hwrberlin.it2014.sweproject.userinput;

import de.hwrberlin.it2014.sweproject.synonym.LawTester;

/**
 * Klasse für Nutzereingabe
 *
 * @author Maximilian Kramp, Paul Piske
 * @since 14.02.2016 12:00:00
 */
public class UserInput {

	/**
	 * Wandelt die Begriffe die der Nutzer eingegeben hat in ein String Array um.
	 *
	 * @author Maximilian Kramp, Paul Piske
	 * @since 14.02.2016 12:00:00
	 * @param input
	 *            URL String
	 * @return Die eingegebenen Begriffe als String Array.
	 */
	private static String[] getUserInput(final String input) {
		String regex = "\\s+";
		return input.split(regex);
	}

	/**
	 * Sucht Rechtsbegriffe aus dem übergebenen String und gibt diese als Leerzeichen-getrennten String zurück.
	 *
	 * @author Maximilian Kramp, Paul Piske
	 * @since 01.03.2016 18:00:00
	 * @param input
	 * @return Leerzeichen-getrennten String von Rechtsbegriffen.
	 */
	public static String getLawTermsFromInput(final String input) {
		String lawTerms = "";

		String[] splittedInput = getUserInput(input);

		for (String term : splittedInput) {
			if (LawTester.testIfWordIsLawTerm(term)) {
				lawTerms += term + " ";
			}
		}
		return lawTerms;
	}

}

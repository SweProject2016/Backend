package de.hwrberlin.it2014.sweproject.userinput;

import de.hwrberlin.it2014.sweproject.synonym.LawTester;

/**
 * Klasse f�r Nutzereingabe
 *
 * @author Maximilian Kramp, Paul Piske
 * @since 14.02.2016 12:00:00
 */
public class UserInput {

    /**
     * User input from URL to String Array
     *
     * @author Maximilian Kramp, Paul Piske
     * @since 14.02.2016 12:00:00
     * @param input
     *            URL String
     * @return User input as array of Strings
     */
    private static String[] getUserInput(final String input) {
        String regex = "\\s+";
        return input.split(regex);
    }

    /**
     * Sucht Rechtsbegriffe aus dem �bergebenen String und gibt diese als Leerzeichen-getrennten String zur�ck.
     *
     * @author Maximilian Kramp, Paul Piske
     * @since 01.03.2016 18:00:00
     * @param input
     * @return
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

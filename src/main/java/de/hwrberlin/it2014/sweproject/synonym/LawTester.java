package de.hwrberlin.it2014.sweproject.synonym;

import java.util.ArrayList;

import de.hwrberlin.it2014.sweproject.database.DatabaseConnection;
import de.hwrberlin.it2014.sweproject.database.TableLawTermsSQL;

public class LawTester {

    private static ArrayList<String> lexiEntries = getLexiEntries();

    /**
     * Die Methode bekommt ein Schluesselwort und ueberprueft ob dieses ein juristischer Ausdruck ist
     *
     * @param keyword
     *            ein Schluesselwort zur Ueberpruefung
     * @return wenn der Schluessel ein juristisches Wort ist, wird true zurueckgegeben, sonst false
     */
    public static boolean testIfWordIsLawTerm(final String keyword) {
        boolean result = false;

        //TODO test in multiple lexi wether keyword is an existing lawterm
        for (String entry : LawTester.lexiEntries) {
            if (keyword.equals(entry)) {
                return result = true;
            }
        }
        return result;
    }

    /**
     * Die interne Methode nimmt eines der juristischen Lexika und fügt dessen Inhalt in eine ArrayList ein
     *
     * @return ArrayList<String> aList mit Inhalt eines Lexikons
     */
    private static ArrayList<String> getLexiEntries() {
    	DatabaseConnection con = new DatabaseConnection();
    	ArrayList<String> lexiEntries = null;
    	try{
    		lexiEntries = TableLawTermsSQL.getAllLawTerms(con);
    	} catch(Exception e){
    		e.printStackTrace();
    	} finally {
			con.close();
		}
    	return lexiEntries;
    }

    /**
     * @param Der
     *            Ausrdruck der geprueft werden soll, ob dieser ein juristicher Begriff ist.
     * @return gibt true zurück wenn der Begriff aus der juristischen Sprache stammt
     */
    private static boolean testIfLawTermOnline(final String term) {
        boolean result = false;

        //TODO http connection if possibles and get the result from the result, if necessary filter the content from the result

        return result;
    }

}

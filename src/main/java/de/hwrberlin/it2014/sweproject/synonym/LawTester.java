package de.hwrberlin.it2014.sweproject.synonym;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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
     * Die interne Methode nimmt eines der juristischen Lexika und f�gt dessen Inhalt in eine ArrayList ein
     *
     * @return ArrayList<String> aList mit Inhalt eines Lexikons
     */
    private static ArrayList<String> getLexiEntries() {
        ArrayList<String> entries = new ArrayList<String>();
        String line = null;
        FileReader fileReader;
        try {
            fileReader = new FileReader("lawTermsExtended.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
                entries.add(line);
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return entries;
    }

    /**
     * @param Der
     *            Ausrdruck der geprueft werden soll, ob dieser ein juristicher Begriff ist.
     * @return gibt true zur�ck wenn der Begriff aus der juristischen Sprache stammt
     */
    private static boolean testIfLawTermOnline(final String term) {
        boolean result = false;

        //TODO http connection if possibles and get the result from the result, if necessary filter the content from the result

        return result;
    }

}

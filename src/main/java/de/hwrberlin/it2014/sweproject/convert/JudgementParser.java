package de.hwrberlin.it2014.sweproject.convert;

import java.io.IOException;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import de.hwrberlin.it2014.sweproject.model.Judgement;
import de.hwrberlin.it2014.sweproject.model.LawSector;
import de.hwrberlin.it2014.sweproject.userinput.UserInput;

public class JudgementParser {
    private static final String BGH_FILE_URL = "http://juris.bundesgerichtshof.de/cgi-bin/rechtsprechung/document.py?Gericht=bgh&nr=%d&Frame=0";

    /**
     * Liefert ein Judgement-Objekt, wenn eine Urteils-PDF übergeben wurde. ACHTUNG: Der Dateiname muss folgendes Format haben: {@code <id>_<filename>.pdf} (wird so vom ursprünglichen Import
     * geliefert.)
     *
     * @param pdfPath
     * @return Judgement-Objekt oder null bei Fehler
     * @throws IOException
     */
    public static Judgement getFromPdf(final Path pdfPath) throws IOException {
        String s = PDFManager.convertPDFToText(pdfPath);
        String pdfName = pdfPath.getFileName().toString();

        int id;
        try {
            id = Integer.parseInt(pdfName.split(Pattern.quote("_"))[0]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Dateiname in falschem Format.");
            return null;
        }
        Judgement j = new Judgement(String.format(JudgementParser.BGH_FILE_URL, id), pdfName);
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        String dateStr;
        if (s.contains("IM NAMEN DES VOLKES")) {
            j.setFileReference(stringBetween(s, "URTEIL", "Veründet").trim());
            dateStr = stringBetween(s, "Verkündet am:\n", "\n").trim();
        } else if (s.contains("BESCHLUSS")) {
            j.setFileReference(stringBetween(s, "BESCHLUSS", "vom").trim());
            dateStr = stringBetween(s, "vom", "in de").trim();
        } else {
            System.err.println("Fall-Typ nicht erkannt.");
            return null;
        }

        try {
            j.setDate(format.parse(dateStr));
        } catch (ParseException e) {
            System.err.println("Datum nicht erkannt.");
            return null;
        }

        String subheading;
        if (s.matches("(?s).*in dem .{0,30}streit.*")) {
            subheading = "in dem " + stringBetween(s, "in dem ", "streit") + "streit";
        } else if (s.matches("(?s).*in der .{0,30}sache.*")) {
            subheading = "in der " + stringBetween(s, "in der ", "sache") + "sache";
        } else if (s.contains("in dem Verfahren")) {
            subheading = "in dem Verfahren";
        } else {
            System.err.println("Fall-Art nicht erkannt.");
            return null;
        }
        subheading = subheading.toLowerCase();
        if (subheading.matches(".*(rechtsstreit|landwirtschaft|familien).*")) {
            j.setSector(new LawSector("Zivilrecht"));
            j.setSentence(parseSentence(s));
        } else if (subheading.matches("straf(anzeige)?sache")) {
            j.setSector(new LawSector("Strafrecht"));
            j.setSentence(parseSentence(s));
            j.setOffence(parseOffence(s));
        } else if (subheading.contains("verfahren")) {
            j.setSector(new LawSector("Zivilrecht")); // TODO: Stimmt das oder muss hier nochmal geprüft werden?
            j.setSentence(parseSentence(s));
            j.setOffence(parseOffence(s));
        } else {
            System.err.println("Rechtsbereich nicht erkannt.");
            return null;
        }

        j.setKeywords(UserInput.getLawTermsFromInput(s)); // Keywords aus dem gesamten Text generieren (enthält dann auch ggf. Gründe und Co)

        return j;
    }

    private static String parseSentence(final String s) { // sucht das Strafmaß bzw. den Beschluss und gibt ihn zurück
        //strafmaß --> beschlossen:
        int index1 = s.indexOf("beschlossen:");
        int index2 = s.indexOf("Gründe");
        if (index2 == -1) {
            index2 = s.indexOf("G r ü n d e "); // vieleicht falch geschrieben
            if (index2 == -1) {
                index2 = s.indexOf("Entscheidungsgründe"); // vieleicht falch geschrieben
            }
            if (index2 == -1) { // nicht vorhanden --> bis ende des Dokuments augeben
                index2 = s.length();
            }
        }

        String z = s.substring(index1 + 12, index2);
        return z.trim();
    }

    private static String parseOffence(final String source) {
        return stringBetween(source, "wegen", "\n").trim();
    }

    private static String stringBetween(String source, final String before, final String after) {
        int begin = source.indexOf(before) + before.length();
        source = source.substring(begin);
        int end = source.indexOf(after);
        return source.substring(0, end);
    }
}

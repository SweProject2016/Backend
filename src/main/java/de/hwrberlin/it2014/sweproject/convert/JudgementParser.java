package de.hwrberlin.it2014.sweproject.convert;

import java.io.IOException;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hwrberlin.it2014.sweproject.model.Committee;
import de.hwrberlin.it2014.sweproject.model.Judgement;
import de.hwrberlin.it2014.sweproject.model.enums.LawSector;
import de.hwrberlin.it2014.sweproject.userinput.UserInput;

public class JudgementParser {
    private static final String BGH_FILE_URL = "http://juris.bundesgerichtshof.de/cgi-bin/rechtsprechung/document.py?Gericht=bgh&nr=%d&Frame=0";
    private static final String NEW_PAGE_MARKER = "NEW_PAGE_MARKER";
    private static final String EOF_MARKER = "EOF_MARKER";
    private static final DateFormat dateFormat = new SimpleDateFormat("dd. MMMM yyyy", Locale.GERMAN);

    /**
     * Liefert ein Judgement-Objekt, wenn eine Urteils-PDF übergeben wurde. ACHTUNG: Der Dateiname muss folgendes Format haben: {@code <id>_<filename>.pdf} (wird so vom ursprünglichen Import
     * geliefert.)
     *
     * @param pdfPath
     * @return Judgement-Objekt oder null bei Fehler
     * @throws IOException
     */
    public static Judgement getFromPdf(final Path pdfPath) throws IOException, NullPointerException {
        String pdfName = pdfPath.getFileName().toString();

        int id;
        try {
            id = Integer.parseInt(pdfName.split(Pattern.quote("_"))[0]);
        } catch (NumberFormatException e) {
            System.err.println("Dateiname in falschem Format.");
            return null;
        }

        // Zeilenumbrüche zu \n umwandeln,
        // Seitenzahlen entfernen und Seitenwechsel merken,
        // getrennte Wörter wieder zusammensetzen
        String s = PDFManager.convertPDFToText(pdfPath).replace("\r", "").replaceAll("\n-\\s+\\d+\\s+-\n", JudgementParser.NEW_PAGE_MARKER + "\n").replace("-\n", "") + JudgementParser.EOF_MARKER;

        Judgement j = new Judgement(String.format(JudgementParser.BGH_FILE_URL, id), pdfName);

        j.setFileReference(search(s, "(?:URTEIL|Urteil|BESCHLUSS|Beschluss)\n(.*?)(Verkündet|vom|\n)").trim());

        try {
            j.setDate(JudgementParser.dateFormat.parse(search(s.toLowerCase(), "(?:verkündet am\\:\n|vom\n?)(.*?)(?:\n)").trim()));
        } catch (ParseException e) {
            System.err.println("Datum nicht erkannt.");
            return null;
        }

        j.setLawSector(parseLawSector(s));

        String sentence = parseSentence(s);
        if (sentence.isEmpty()) {
            System.err.println("Beschluss nicht erkannt.");
            return null;
        }
        j.setSentence(sentence);
        j.setOffence(parseOffence(s));

        // Keywords aus dem gesamten Text generieren (enthält dann auch ggf. Gründe und Co)
        j.setKeywords(UserInput.getLawTermsFromInput(removeDuplicateWords(s)));

        // Default-Werte
        j.setPageRank(0);
        j.setComittee(new Committee("bgh"));

        return j;
    }

    /**
     * Liefert den Rechtsbereich anhand des Untertitels.
     *
     * @param source
     * @return
     */
    private static LawSector parseLawSector(final String source) {
        String subheading = search(source.toLowerCase(), "(in de(m|r) .*(streit|sache|verfahren)|in sachen)");
        if (subheading == null) {
            System.out.println("Rechtsbereich nicht erkannt.");
            return LawSector.UNDEFINED;
        } else {
            if (subheading.contains("straf")) {
                return LawSector.STRAFRECHT;
            } else {
                return LawSector.ZIVILRECHT;
            }
        }
    }

    /**
     * Liefert den Beschluss bzw. das Strafmaß.
     *
     * @param source
     * @return
     */
    private static String parseSentence(final String source) {
        return search(source, "(?:beschlossen|für Recht erkannt)\\s*\\:(.*?)(?:(Entscheidungs)?(G|g)ründe|G r ü n d e|G R Ü N D E|Tatbestand|" + JudgementParser.EOF_MARKER + ")").replace(JudgementParser.NEW_PAGE_MARKER, "").replace(JudgementParser.EOF_MARKER, "").trim();
    }

    /**
     * Liefert die Anklage ("wegen ..."), wenn vohanden.
     *
     * @param source
     * @return
     */
    private static String parseOffence(final String source) {
        String firstPage = source.split(JudgementParser.NEW_PAGE_MARKER)[0] + JudgementParser.NEW_PAGE_MARKER;
        if (!firstPage.contains("\nwegen")) {
            return "";
        }
        return stringBetween(firstPage, "\nwegen", JudgementParser.NEW_PAGE_MARKER).replace(JudgementParser.NEW_PAGE_MARKER, "").replace(JudgementParser.EOF_MARKER, "").replace("\n", " ").trim();
    }

    /**
     * Entfernt doppelte Wörter aus dem übergebenen Text.
     *
     * @param source
     * @return
     */
    private static String removeDuplicateWords(final String source) {
        // Teilt den String an den Leerzeichen, erstellt daraus ein LinkedHashSet (dadurch keine doppelten Wörter mehr).
        // Dieses wird durch .toString() als [EintragA, EintragB, EintragC] angezeigt, deshalb werden "[", "]" und "," entfernt.
        return new LinkedHashSet<String>(Arrays.asList(source.split("\\s+"))).toString().replaceAll("(^\\[|\\]$)", "").replace(", ", " ");
        // ggf. sollten hier noch Sonderzeichen entfernt werden
    }

    /**
     * Gibt den Text aus {@code source} zwischen {@code before} und {@code after} zurück. {@code null}, wenn nichts gefunden wird.
     *
     * @param source
     * @param before
     * @param after
     * @return
     */
    private static String stringBetween(String source, final String before, final String after) {
        int begin = source.indexOf(before);
        source = source.substring(begin + before.length());
        int end = source.indexOf(after);

        if (begin == -1 || end == -1) {
            return null;
        }

        return source.substring(0, end);
    }

    /**
     * Vereinfacht die Nutzung von RegEx. Die erste gefundene Gruppe wird zurückgegeben. Leerstring, wenn nichts gefunden wurde. {@code Pattern.DOTALL} wird gesetzt, Multiline wird also unterstützt.
     *
     * @param source
     * @param regex
     * @return
     */
    private static String search(final String source, final String regex) {
        Pattern p = Pattern.compile(regex, Pattern.DOTALL);
        Matcher m = p.matcher(source);

        return m.find() ? m.group(1) : "";
    }
}

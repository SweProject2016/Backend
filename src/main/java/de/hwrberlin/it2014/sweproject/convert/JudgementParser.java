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
import de.hwrberlin.it2014.sweproject.model.LawSector;
import de.hwrberlin.it2014.sweproject.userinput.UserInput;

public class JudgementParser {
    private static final String BGH_FILE_URL = "http://juris.bundesgerichtshof.de/cgi-bin/rechtsprechung/document.py?Gericht=bgh&nr=%d&Frame=0";
    private static final String NEW_PAGE_MARKER = "NEW_PAGE_MARKER";
    private static final String EOF_MARKER = "EOF_MARKER";

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
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Dateiname in falschem Format.");
            return null;
        }

        // Zeilenumbrüche zu \n umwandeln,
        // Seitenzahlen entfernen und Seitenwechsel merken,
        // getrennte Wörter wieder zusammensetzen
        String s = PDFManager.convertPDFToText(pdfPath).replace("\r", "").replaceAll("\n-\\s+\\d+\\s+-\n", JudgementParser.NEW_PAGE_MARKER + "\n").replace("-\n", "") + JudgementParser.EOF_MARKER;

        Judgement j = new Judgement(String.format(JudgementParser.BGH_FILE_URL, id), pdfName);
        DateFormat format = new SimpleDateFormat("dd. MMMM yyyy", Locale.GERMAN);
        String dateStr = search(s.toLowerCase(), "(?:verkündet am\\:\n|vom\n?)(.*?)(?:\n)").trim();

        String ref = search(s, "(?:URTEIL|Urteil|BESCHLUSS|Beschluss)\n(.*?)(Verkündet|vom|\n)").trim();
        // TODO: AKZ steht selten nicht an dieser Stelle. Evtl. AKZ-RegEx bauen?
        j.setFileReference(ref);

        try {
            j.setDate(format.parse(dateStr));
        } catch (ParseException e) {
            System.err.println("Datum nicht erkannt.");
            return null;
        }

        String subheading = search(s.toLowerCase(), "(in de(m|r) .*(streit|sache|verfahren)|in sachen)");
        if (subheading == null) {
            System.out.println("Rechtsbereich nicht erkannt.");
            j.setSector(new LawSector("undefined"));
        } else {
            if (subheading.contains("straf")) {
                j.setSector(new LawSector("Strafrecht"));
            } else {
                j.setSector(new LawSector("Zivilrecht"));
            }
        }
        j.setSentence(parseSentence(s));
        j.setOffence(parseOffence(s));

        j.setKeywords(UserInput.getLawTermsFromInput(removeDuplicateWords(s))); // Keywords aus dem gesamten Text generieren (enthält dann auch ggf. Gründe und Co)

        j.setPageRank(0);
        j.setComittee(new Committee("bgh"));

        return j;
    }

    private static String parseSentence(final String source) { // sucht das Strafmaß bzw. den Beschluss und gibt ihn zurück
        return search(source, "(?:beschlossen|für Recht erkannt)\\s*\\:(.*?)(?:(Entscheidungs)?(G|g)ründe|G r ü n d e|G R Ü N D E|Tatbestand|" + JudgementParser.EOF_MARKER + ")").replace(JudgementParser.NEW_PAGE_MARKER, "").replace(JudgementParser.EOF_MARKER, "").trim();
    }

    private static String parseOffence(final String source) {
        String firstPage = source.split(JudgementParser.NEW_PAGE_MARKER)[0] + JudgementParser.NEW_PAGE_MARKER;
        if (!firstPage.contains("\nwegen")) {
            return "";
        }
        return stringBetween(firstPage, "\nwegen", JudgementParser.NEW_PAGE_MARKER).replace(JudgementParser.NEW_PAGE_MARKER, "").replace(JudgementParser.EOF_MARKER, "").replace("\n", " ").trim();
    }

    private static String removeDuplicateWords(final String source) {
        return new LinkedHashSet<String>(Arrays.asList(source.split("\\s+"))).toString().replaceAll("(^\\[|\\]$)", "").replace(", ", " ");
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

    private static String search(final String source, final String regex) {
        Pattern p = Pattern.compile(regex, Pattern.DOTALL);
        Matcher m = p.matcher(source);

        return m.find() ? m.group(1) : null;
    }
}

package de.hwrberlin.it2014.sweproject.convert;

import java.io.IOException;
import java.nio.file.Path;

public class FallParser {

    public static Fall getFromPdf(final Path pdfPath) throws IOException {
        String s = PDFManager.convertPDFToText(pdfPath);

        Fall f = new Fall();
        if (s.contains("IM NAMEN DES VOLKES")) {
            f.setAktenzeichen(stringBetween(s, "URTEIL", "Verkündet").trim());
            f.setDatum(stringBetween(s, "Verkündet am:\n", "\n").trim());
            f.setUeberschrift("BUNDESGERICHTSHOF IM NAMEN DES VOLKES URTEIL");
        } else if (s.contains("BESCHLUSS")) {
            f.setAktenzeichen(stringBetween(s, "BESCHLUSS", "vom").trim());
            f.setDatum(stringBetween(s, "vom", "in de").trim());
            f.setUeberschrift("BUNDESGERICHTSHOF BESCHLUSS");
        } else {
            System.err.println("Fall-Typ nicht erkannt.");
            return null;
        }

        if (s.matches("(?s).*in dem .{0,30}streit.*")) {
            f.setUnterueberschrift("in dem " + stringBetween(s, "in dem ", "streit") + "streit");
        } else if (s.matches("(?s).*in der .{0,30}sache.*")) {
            f.setUnterueberschrift("in der " + stringBetween(s, "in der ", "sache") + "sache");
        } else if (s.contains("in dem Verfahren")) {
            f.setUnterueberschrift("in dem Verfahren");
        } else {
            System.err.println("Fall-Art nicht erkannt.");
            return null;
        }

        if (f.getUnterueberschrift().matches(".*((R|r)echtsstreit|Landwirtschaft|Familien).*")) {
            f.setRechtsbereich("Zivilrecht");
            f.setStrafmass(getStrafmass(s));
        } else if (f.getUnterueberschrift().contains("Strafsache")) { //TODO Strafanzeigesache in PDF 17790_2_ars_111-00
            f.setRechtsbereich("Strafrecht");
            f.setStrafmass(getStrafmass(s));
            f.setVergehen(getVergehen(s));
        } else if (f.getUnterueberschrift().contains("Verfahren")) {
            f.setRechtsbereich("Zivilrecht"); // TODO: Stimmt das oder muss hier nochmal geprüft werden?
            f.setStrafmass(getStrafmass(s));
            f.setVergehen(getVergehen(s));
        } else {
            System.err.println("Rechtsbereich nicht erkannt.");
            return null;
        }

        
        //Gründe auslessen (bei allen PDF gleich, die Gründe haben)
        int index1 = s.indexOf( "Gründe" );
        if (index1==-1) {
     	   index1 = s.indexOf("G r ü n d e "); // vieleicht falch geschrieben
     	   if(index1!=-1); index1+=7;
     	   if(index1==-1){
     		  index1 = s.indexOf("Entscheidungsgründe"); // vieleicht falch geschrieben
     		 if(index1!=-1); index1+=13;
     	   }
        }
     	if (index1!=-1) { // Gründe vorhanden
     		int index2=s.length();
     		 f.setGruende(s.substring( index1+8, index2 ));
    	}
        
        

        return f;
    }

    private static String getStrafmass(final String s) { // sucht das Strafmaß bzw. den Beschluss und gibt ihn zurück
        //strafmaß --> beschlossen:
        int index1 = s.indexOf("beschlossen:");
        int index2 = s.indexOf("Gründe");
        if (index2 == -1) {
            index2 = s.indexOf("G r ü n d e "); // vieleicht falch geschrieben
            if(index2==-1){
       		  index2 = s.indexOf("Entscheidungsgründe"); // vieleicht falch geschrieben
       	   }
            if (index2 == -1) { // nicht vorhanden --> bis ende des Dokuments augeben
                index2 = s.length();
            }
        }

        String z = s.substring(index1 + 12, index2);
        return z.trim();
    }

    private static String getVergehen(final String source) {
        return stringBetween(source, "wegen", "\n").trim();
    }

    private static String stringBetween(String source, final String before, final String after) {
        int begin = source.indexOf(before) + before.length();
        source = source.substring(begin);
        int end = source.indexOf(after);
        return source.substring(0, end);
    }
}

// Fehler treten bei fogenden PDFs auf:
//17791_1_str_293-01
//17790_2_ars_111-00
//17791_1_str_293-01

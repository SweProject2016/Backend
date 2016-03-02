package bghimport.convert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFManager {
    /**
     * Liefert den Text-Inhalt einer PDF.
     *
     * @param filePath
     *            Pfad zur PDF.
     * @return Text in der PDF.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static String convertPDFToText(final Path filePath) throws FileNotFoundException, IOException {
        try (RandomAccessFile file = new RandomAccessFile(new File(filePath.toString()), "r")) {
            PDFParser parser = new PDFParser(file);
            parser.parse();
            COSDocument cosDoc = parser.getDocument();
            PDFTextStripper pdfStripper = new PDFTextStripper();
            PDDocument pdDoc = new PDDocument(cosDoc);
            pdfStripper.setStartPage(1);
            pdfStripper.setEndPage(pdDoc.getNumberOfPages());

            return pdfStripper.getText(pdDoc);
        }
    }
}
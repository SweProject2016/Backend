package de.hwrberlin.it2014.sweproject.convert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import de.hwrberlin.it2014.sweproject.database.DatabaseConnection;
import de.hwrberlin.it2014.sweproject.database.TableJudgementSQL;
import de.hwrberlin.it2014.sweproject.model.Judgement;

public class Import {
    /**
     * Importiert alle PDFs aus dem übergebenen Ordner.
     *
     * @param directory
     * @throws IOException
     */
    public static void importCases(final Path directory) {
        File f = new File(directory.toString());
        DatabaseConnection con = new DatabaseConnection();
        for (File file : f.listFiles()) {
            if (!file.isFile() || !file.getName().toLowerCase().endsWith(".pdf")) {
                continue;
            }

            try {
                Judgement j = JudgementParser.getFromPdf(Paths.get(file.getAbsolutePath()));
                if (j != null) {
                    con.executeUpdate(TableJudgementSQL.getInsertSQLCode(j));
                    file.delete();
                }
            } catch (IOException | SQLException e) {
                e.printStackTrace();
                continue;
            }
        }
        con.close();
    }
}

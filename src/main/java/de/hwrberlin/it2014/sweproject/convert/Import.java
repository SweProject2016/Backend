package de.hwrberlin.it2014.sweproject.convert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Date;

import de.hwrberlin.it2014.sweproject.database.DatabaseConnection;
import de.hwrberlin.it2014.sweproject.database.TableJudgementSQL;
import de.hwrberlin.it2014.sweproject.model.Committee;
import de.hwrberlin.it2014.sweproject.model.Judgement;
import de.hwrberlin.it2014.sweproject.model.LawSector;
import de.hwrberlin.it2014.sweproject.userinput.UserInput;

public class Import {

    public Import(){

    }

    public void importCases(final String filePath) throws IOException{
        File[] files = new File(filePath).listFiles();
        DatabaseConnection con = new DatabaseConnection();
        boolean connected = con.connectToMysql();
        for(File file : files) {
            if(file.isFile()) {
                Path path = Paths.get(file.getAbsolutePath());
                Fall fall = FallParser.getFromPdf(path);
                String text = "";
                text += fall.getGruende() + " ";
                text += fall.getVergehen() + " ";
                text += fall.getRechtsBereich();
                String keywords = UserInput.getLawTermsFromInput(text);

                System.out.println(connected);
                Date da = new Date();
                java.sql.Date d = new java.sql.Date(da.getTime());

                Judgement j = new Judgement(file.getAbsolutePath(), file.getName());
                j.setDate(d);
                j.setKeywords(keywords);
                j.setOffence(fall.getVergehen());
                j.setPageRank(1.231f);
                j.setSector(new LawSector(fall.getRechtsBereich()));
                j.setSentence(fall.getStrafmass());
                j.setComittee(new Committee("com2"));
                j.setFileReference(file.getName());

                try {
                    con.executeUpdate(TableJudgementSQL.getInsertSQLCode(j));
                } catch(SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
        con.close();
    }
}

package de.hwrberlin.it2014.sweproject.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import de.hwrberlin.it2014.sweproject.model.Judgement;

public class TableJudgementSQL {

    private final static String QUERY_STRING = "INSERT INTO tbl_judgement "
            + "(file_reference, committee, law_sector, tbl_judgement.date, sentence, offence, page_rank, pdf_filename,pdf_link, keywords)"
            + "VALUES (?,?,?,?,?,?,?,?,?,?)";



    public static String getInsertSQLCode(final Judgement judgement){
        String sql = "INSERT INTO tbl_judgement (file_reference, committee, law_sector," + "date, sentence, offence, page_rank, pdf_filename," + "pdf_link, keywords) SELECT ";
        sql += "'" + judgement.getFileReference() + "',";
        // TODO: committee+law_sector-parse
        sql += "c.id,ls.id,";
        sql += "'" + judgement.getDate() + "',";
        sql += "'" + judgement.getSentence() + "',";
        sql += "'" + judgement.getOffence() + "',";
        sql += judgement.getPageRank() + ",";
        sql += "'" + judgement.getPdfFileName() + "',";
        sql += "'" + judgement.getPdfLink() + "',";
        sql += "'" + judgement.getKeywords();
        sql += "' FROM tbl_committee c, tbl_law_sector ls WHERE c.name='" + judgement.getComittee().getName();
        sql += "' AND ls.name='" + judgement.getSector().getName() + "';";
        System.out.println(sql);
        return sql;
    }


    /**
     * Insert Methode mit PreparedStatement
     *
     * @param j Judgement Objekt
     * @param con DatabaseConnection
     * @return PreparedStatement
     * @throws SQLException
     */
    public static PreparedStatement prepareInsert(final Judgement j, final DatabaseConnection con) throws SQLException{
        PreparedStatement stmt = null;
        try {
            Connection c = con.getConnection();
            stmt = c.prepareStatement(TableJudgementSQL.QUERY_STRING);
            stmt.setString(1,j.getFileReference());
            stmt.setInt(2, 1);
            stmt.setInt(3, TableLawSectorSQL.getLawSectorIdByName(j.getSector(), con));
            stmt.setDate(4, new Date(j.getDate().getTime()));
            stmt.setString(5, j.getSentence());
            stmt.setString(6, j.getOffence());
            stmt.setFloat(7, j.getPageRank());
            stmt.setString(8, j.getPdfFileName());
            stmt.setString(9, j.getPdfLink());
            stmt.setString(10, j.getKeywords());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stmt;
    }


    /**
     * die Funktion baut eine SQL-Anfrage mit der die vom Nutzer eingegebenen Keywords mit der Datenbank verglichen werden k�nnen
     *
     * @author Danilo G�nzel
     * @param keywords Sind die von der Nutzereingabe erhaltenen Schl�sselw�rter(ohne synonyme)
     * @param lawsector Der Rechtsbereich aus welchem die gefundenen F�lle stammen sollen
     * @return Als R�ckgabe erh�lt man die Datenbankanfrage, die die Zeilen nach den Schl�sselworttreffern ausw�hlt
     */
    public static String getSelectSQLCode(final ArrayList<String> keywords, final String lawsector){
        String query = "SELECT * FROM tbl_judgement WHERE CONTAINS((sentence, offence, keywords),";
        String queryKeywords = new String();

        for(String key : keywords) {
            queryKeywords += "'" + key.toLowerCase() + "' or ";
        }

        int len = 0;
        len = queryKeywords.length();
        queryKeywords = queryKeywords.substring(0, len - 3);
        if(lawsector != null && !lawsector.isEmpty()) {
            queryKeywords += ") AND LOWER(law_sector) = LOWER('" + lawsector + "');";
        } else {
            queryKeywords += ");";
        }

        query += queryKeywords;

        return query;
    }
}

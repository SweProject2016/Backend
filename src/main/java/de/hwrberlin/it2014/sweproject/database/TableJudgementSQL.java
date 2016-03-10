package de.hwrberlin.it2014.sweproject.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import de.hwrberlin.it2014.sweproject.model.Judgement;
import de.hwrberlin.it2014.sweproject.model.enums.LawSector;

public class TableJudgementSQL {

    private final static String INSERT_QUERY_STRING = "INSERT INTO tbl_judgement "
            + "(file_reference, committee, law_sector, tbl_judgement.date, sentence, offence, page_rank, pdf_filename,pdf_link, keywords)"
            + "VALUES (?,?,?,?,?,?,?,?,?,?)";

    public static final String SELECT_BY_FILE_REFERENCE_QUERY_STRING = "SELECT * FROM tbl_judgement WHERE file_reference = ?";
    
    /**
     * Findet die ID eines Judgements anhand des Aktenzeichens heraus
     * @author Dominik Habel
     *
     * @param fileRef das Aktenzeichen
     * @param con die Datenbank-Verbindung
     * @return die Datenbank ID zum übergebenen Aktenzeichen
     */
    public static int getJudgementIdByFileReference(final String fileRef, final DatabaseConnection con){
        Connection c =  con.getConnection();
        Integer id = null;
        try {
            PreparedStatement stmt = c.prepareStatement(TableJudgementSQL.SELECT_BY_FILE_REFERENCE_QUERY_STRING);
            stmt.setString(1,fileRef);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
    
    /**
     * Generiert SQL-Insert-Code zu einem Judgement
     * @author Dominik Habel
     *
     * @param judgement das einzufügende Judgement
     * @return der SQL-Code
     */
    public static String getInsertSQLCode(final Judgement judgement){
        String sql = "INSERT INTO tbl_judgement (file_reference, committee, law_sector," + "date, sentence, offence, page_rank, pdf_filename," + "pdf_link, keywords) SELECT ";
        sql += "'" + judgement.getFileReference() + "',";
        sql += "c.id,";
        sql += judgement.getLawSector().ordinal()+",";
        sql += "'" + judgement.getDate() + "',";
        sql += "'" + judgement.getSentence() + "',";
        sql += "'" + judgement.getOffence() + "',";
        sql += judgement.getPageRank() + ",";
        sql += "'" + judgement.getPdfFileName() + "',";
        sql += "'" + judgement.getPdfLink() + "',";
        sql += "'" + judgement.getKeywords();
        sql += "' FROM tbl_committee c WHERE c.name='" + judgement.getComittee().getName()+ "';";
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
            stmt = c.prepareStatement(TableJudgementSQL.INSERT_QUERY_STRING);
            stmt.setString(1,j.getFileReference());
            stmt.setInt(2, TableCommitteeSQL.getCommitteeIdByName(j.getComittee(), con));
            stmt.setInt(3, j.getLawSector().ordinal());
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
     * die Funktion baut eine SQL-Anfrage mit der die vom Nutzer eingegebenen Keywords mit der Datenbank verglichen werden kï¿½nnen
     *
     * @author Danilo Günzel
     * @param keywords Sind die von der Nutzereingabe erhaltenen Schlï¿½sselwï¿½rter(ohne synonyme)
     * @param lawsector Der Rechtsbereich aus welchem die gefundenen Fï¿½lle stammen sollen
     * @return Als Rï¿½ckgabe erhï¿½lt man die Datenbankanfrage, die die Zeilen nach den Schlï¿½sselworttreffern auswï¿½hlt
     */
    public static String getSelectSQLCode(final ArrayList<String> keywords, final LawSector lawsector){
        //String query = "SELECT * FROM tbl_judgement WHERE CONTAINS((sentence, offence, keywords),";
        String query = "SELECT * FROM tbl_judgement WHERE ";
        //String queryKeywords = new String();

        for(String key : keywords) {
            //queryKeywords += "'" + key.toLowerCase() + "' or ";
            query += "sentence LIKE [^a-Z]'" + key.toLowerCase() + "' OR ";
            query += "offence LIKE [^a-Z]'" + key.toLowerCase() + "' OR ";
            query += "keywords LIKE [^a-Z]'" + key.toLowerCase() + "' OR ";
        }

        int len = 0;
        //len = queryKeywords.length();
        len = query.length();
        //queryKeywords = queryKeywords.substring(0, len - 3);
        query = query.substring(0, len - 3);
        if(lawsector != null) {
            //queryKeywords += ") AND LOWER(law_sector) = LOWER('" + lawsector + "');";
            query += " AND LOWER(law_sector) = LOWER('" + lawsector + "');";
        } else {
            //queryKeywords += ");";
            query += ";";
        }

        //query += queryKeywords;

        return query;
    }
}

package de.hwrberlin.it2014.sweproject.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import de.hwrberlin.it2014.sweproject.model.Committee;
import de.hwrberlin.it2014.sweproject.model.Judgement;
import de.hwrberlin.it2014.sweproject.model.Result;
import de.hwrberlin.it2014.sweproject.model.enums.LawSector;

public class DatabaseConnection {
    private Connection con;

    public Connection getConnection(){
        return this.con;
    }

    public DatabaseConnection(){
        connectToMysql();
    }


    /**
     * Stellt eine Verbindung zu einer MySQL-Datenbank �ber einen JDBC-Driver mit Parametern
     * @author Dominik Habel
     *
     * @param host Host IP oder Domain
     * @param database Name der Datenbank
     * @param user Nutzername
     * @param pwd Passwort als Plain-Text
     * @return true, wenn erfolgreich verbunden - false, wenn nicht
     */
    public boolean connectToMysql(final String host, final String database, final String user, final String pwd){
        assert this.con == null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String connectionCommand = "jdbc:mysql://" + host + "/" + database + "?user=" + user + ((pwd != null) ? "&password=" + pwd : "");
            this.con = DriverManager.getConnection(connectionCommand);
            return true;

        } catch(Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Stellt eine Verbindung zu einer MySQL-Datenbank �ber einen JDBC-Driver mit der DatabaseConfig
     *
     * @author Dominik Habel
     *
     * @return true, wenn erfolgreich verbunden - false, wenn nicht
     */
    public boolean connectToMysql(){
        assert this.con == null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String connectionCommand = "jdbc:mysql://" + DatabaseConfig.DB_HOST + "/" + DatabaseConfig.DB_NAME + "?user=" + DatabaseConfig.DB_USER + ((DatabaseConfig.DB_PASSWORD != null) ? "&password=" + DatabaseConfig.DB_PASSWORD : "");
            this.con = DriverManager.getConnection(connectionCommand);
            return true;

        } catch(Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Testet, ob die Verbindung noch besteht
     *
     * @author Dominik Habel
     *
     * @return true, wenn verbunden - false, wenn nicht
     */
    public boolean isConnected(){
        try {
            return this.con != null && !this.con.isClosed();
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Ausf�hren einer Abfrage (SQL-Select)
     *
     * @author Dominik Habel
     *
     * @param query SQL code
     * @return ResultSet der Abfrage
     * @throws SQLException
     */
    public ResultSet executeQuery(final String query) throws SQLException{
        Statement st = this.con.createStatement();
        ResultSet rs = st.executeQuery(query);
        return rs;
    }

    /**
     * Konvertiert ein ResultSet in eine ArrayList von Judgements
     *
     * @author Dominik Habel
     *
     * @param rs ResultSet einer Abfrage (Abfrage muss folgendes Schema haben SELECT * [...])
     * @return ArrayList von Judgement
     * @throws SQLException
     */
    public ArrayList<Judgement> convertResultSetToJudgementList(final ResultSet rs) throws SQLException{
        ArrayList<Judgement> list = new ArrayList<Judgement>();
        HashMap<Integer, Committee> c = new HashMap<Integer, Committee>();
        while(rs.next()){
            Judgement judge = new Judgement(null, null);
            judge.setFileReference(rs.getString("file_reference"));
            judge.setKeywords(rs.getString("keywords"));
            judge.setOffence(rs.getString("offence"));
            judge.setSentence(rs.getString("sentence"));
            judge.setPdfFileName(rs.getString("pdf_filename"));
            judge.setPdfLink(rs.getString("pdf_link"));
            judge.setDate(rs.getDate("date"));
            judge.setPageRank(rs.getFloat("page_rank"));

            int id = rs.getInt("law_sector");
            judge.setLawSector(LawSector.values()[id]);

            int id2=rs.getInt("committee");
            Committee committee = c.get(id2);
            if(committee==null){
                ResultSet newC = executeQuery("SELECT name FROM tbl_committee WHERE id="+id2+";");
                newC.next();
                Committee dummy = new Committee(newC.getString("name"));
                c.put(id2, dummy);
                judge.setComittee(dummy);
            }else{
                judge.setComittee(committee);
            }

            list.add(judge);
        }
        return list;
    }

    /**
     * Konvertiert ein ResultSet in eine ArrayList von Result
     *
     * @author Dominik Habel
     *
     * @param rs ResultSet einer Abfrage (Abfrage muss folgendes Schema haben SELECT * [...])
     * @return ArrayList von Result
     * @throws SQLException
     */
    public ArrayList<Result> convertResultSetToResultList(final ResultSet rs) throws SQLException{
        ArrayList<Result> list = new ArrayList<Result>();
        HashMap<Integer, Judgement> j = new HashMap<Integer, Judgement>();
        HashMap<Integer, Committee> c = new HashMap<Integer, Committee>();
        while(rs.next()){
            Result result = new Result(null, null, 0, null);
            result.setID(rs.getInt("id")); // edit by Max Bock
            result.setUserInput(rs.getString("user_input"));
            result.setSimilarity(rs.getFloat("similarity"));
            result.setDate(rs.getDate("date"));
            result.setUserRating(rs.getFloat("user_rating"));

            int id = rs.getInt("picked_file");
            Judgement judge = j.get(id);
            if(judge==null){
                ResultSet newLS = executeQuery("SELECT * FROM tbl_judgement WHERE id="+id+";");
                newLS.next();
                //questionable but should save some memory
                Judgement dummy = new Judgement(null, null);
                dummy.setFileReference(newLS.getString("file_reference"));
                dummy.setKeywords(newLS.getString("keywords"));
                dummy.setOffence(newLS.getString("offence"));
                dummy.setSentence(newLS.getString("sentence"));
                dummy.setPdfFileName(newLS.getString("pdf_filename"));
                dummy.setPdfLink(newLS.getString("pdf_link"));
                dummy.setDate(newLS.getDate("date"));
                dummy.setPageRank(newLS.getFloat("page_rank"));

                int id2 = newLS.getInt("law_sector");
                dummy.setLawSector(TableLawSectorSQL.getLawSectorById(id2, this));

                id2=newLS.getInt("committee");
                Committee committee = c.get(id2);
                if(committee==null){
                    ResultSet newC = executeQuery("SELECT name FROM tbl_committee WHERE id="+id2+";");
                    newC.next();
                    Committee dummy2 = new Committee(newC.getString("name"));
                    c.put(id2, dummy2);
                    dummy.setComittee(dummy2);
                }else{
                    dummy.setComittee(committee);
                }
                //end of questionable stuff
                //next line: alternative
                //				ArrayList<Judgement> dummy = convertResultSetToJudgementList(newLS);
                j.put(id, dummy);
                result.setJudgement(dummy);
            }else{
                result.setJudgement(judge);
            }
            list.add(result);
        }
        return list;
    }

    /**
     * Konvertiert ein ResultSet in eine ArrayList von Committee
     *
     * @author Dominik Habel
     *
     * @param ResultSet einer Abfrage (Abfrage muss folgendes Schema haben SELECT * [...])
     * @return ArrayList von Committee
     * @throws SQLException
     */
    public ArrayList<Committee> convertResultSetToCommitteeList(final ResultSet rs) throws SQLException{
        ArrayList<Committee> list = new ArrayList<Committee>();
        while(rs.next()){
            Committee dummy = new Committee(rs.getString("name"));
            list.add(dummy);
        }
        return list;
    }

    /**
     * Ausf�hren eines Updates (SQL-Update, -Delete, -Insert)
     *
     * @author Dominik Habel
     *
     * @param query SQL Code
     * @return entweder (1) die Anzahl der Zeilen f�r die SQL Data Manipulation Language (DML) Statements oder (2) 0 f�r SQL Statements die nichts zur�ckgeben
     * @throws SQLException
     */
    public synchronized int executeUpdate(final String query) throws SQLException{
        Statement st = this.con.createStatement();
        return st.executeUpdate(query);
    }

    /**
     * Execute Update on database (SQL-Insert) for a single dataset only
     * not tested
     * @author Max Bock
     * @param query - SQL-Insert Statement only
     * @return (hopefully) the ID of the inserted Set; -1 if something went wrong
     * @throws SQLException
     */
    public int executeUpdateRetrieveID(final String query) throws SQLException
    {
        PreparedStatement st = this.con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        st.executeUpdate();
        ResultSet rs = st.getGeneratedKeys();
        int id;
        if(rs.next())
        {
            id = rs.getInt("id");
        }
        else
        {
            id = -1;
        }
        return id;
    }
    public int executeUpdateRetrieveID(PreparedStatement st) throws SQLException
    {
        st.executeUpdate();
        ResultSet rs = st.getGeneratedKeys();
        int id;
        if(rs.next())
        {
            id = rs.getInt(1);
        }
        else
        {
            id = -1;
        }
        return id;
    }

    /**
     * Schlie�t die Datenbank-Verbindung
     * @author Dominik Habel
     *
     */
    public void close(){
        try {
            this.con.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}

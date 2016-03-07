package de.hwrberlin.it2014.sweproject.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.hwrberlin.it2014.sweproject.model.LawSector;

public class TableLawSectorSQL {

    public static final String SELECT_BY_NAME_QUERY_STRING = "SELECT * FROM tbl_law_sector WHERE name = ?";

    public static String getInsertSQLCode(final LawSector ls){
        return "INSERT INTO tbl_law_sector (name) VALUES ('"+ls.getName()+"');";
    }

    public static int getLawSectorIdByName(final LawSector ls, final DatabaseConnection con){
        Connection c =  con.getConnection();
        Integer id = null;
        try {
            PreparedStatement stmt = c.prepareStatement(TableLawSectorSQL.SELECT_BY_NAME_QUERY_STRING);
            stmt.setString(1,ls.getName());
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
}

package de.hwrberlin.it2014.sweproject.database;

import java.sql.SQLException;
import java.util.Date;

public class TestDBStuff {

	public static void main(String[] args) {
		DatabaseConnection con = new DatabaseConnection();
		boolean connected = con.connectToMysql("localhost", "swe_project", "root", "");
		
		Judgement j = new Judgement("C:/Test", "pdf1");
		j.setDate(new Date());
		j.setKeywords("keys");
		j.setOffence("totschlag");
		j.setPageRank(1.231f);
		j.setSector(new LawSector("strafzeug"));
		j.setSentence("30 Jahre im Bau");
		j.setComittee(new Committee("com1"));
		j.setFileReference("pdf1");
		TableJudgement t = new TableJudgement();
		try {
			con.executeUpdate(t.getInsertSQLCode(j));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		con.close();
	}

}

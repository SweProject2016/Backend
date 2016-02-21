package de.hwrberlin.it2014.sweproject.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class TestDBStuff {

	public static void main(String[] args) {
		DatabaseConnection con = new DatabaseConnection();
		boolean connected = con.connectToMysql("localhost", "swe_project", "root", "");
		
//		Judgement j = new Judgement("C:/Test2", "pdf2");
//		j.setDate(new Date());
//		j.setKeywords("keys");
//		j.setOffence("totschlag");
//		j.setPageRank(1.231f);
//		j.setSector(new LawSector("zivilzeug"));
//		j.setSentence("30 Jahre im Bau");
//		j.setComittee(new Committee("com2"));
//		j.setFileReference("pdf2");
//		TableJudgement t = new TableJudgement();
//		try {
//			con.executeUpdate(t.getInsertSQLCode(j));
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		try {
			ResultSet test = con.executeQuery("SELECT * FROM tbl_judgement WHERE id = 5 OR id = 7");
			
			ArrayList<Judgement> dummy = con.convertResultSetToJudgementList(test);
			System.out.println(dummy.get(0).getSector()==dummy.get(1).getSector());
			System.out.println(dummy.get(0).getComittee()==dummy.get(1).getComittee());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			ResultSet test = con.executeQuery("SELECT * FROM tbl_results WHERE id = 1 OR id=2");
			ArrayList<Result> dummy = con.convertResultSetToResultList(test);
			System.out.println(dummy.get(0).getJudgement().getSector()==dummy.get(1).getJudgement().getSector());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		con.close();
	}

}

package de.hwrberlin.it2014.sweproject.convert;

import java.io.IOException;
import java.nio.file.Path;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hwrberlin.it2014.sweproject.model.Committee;
import de.hwrberlin.it2014.sweproject.model.Judgement;
import de.hwrberlin.it2014.sweproject.model.LawSector;
import de.hwrberlin.it2014.sweproject.model.Result;

public class Import {

    public Import() {
    	
    }
    
    public void importCases() throws IOException{	
    	File[] files = new File("imports\\").listFiles();
    	for(File file : files){
    	  if(file.isFile()){
    	    Fall fall = new FallParser.getFromPdf(file.getAbsolutePath());
    	    String[] text = new String[];
    	    text+=fall.gruende;
    	    text+=fall.vergehen;
    	    text+=fall.rechtsbereich;
    	    ArrayList<String> keywords = new ArrayList<String>();
    	    keywords = UserInput.getLawTermsFromInput(text);
    	    
    	    DatabaseConnection con = new DatabaseConnection();
    		boolean connected = con.connectToMysql();
    		System.out.println(connected);
    		Date da = new Date();
    		java.sql.Date d = new java.sql.Date(da.getTime());
    		
    		Judgement j = new Judgement(file.getAbsolutePath(), file.getName());
    		j.setDate(d);
    		j.setKeywords(keywords);
    		j.setOffence(fall.vergehen);
    		j.setPageRank(1.231f);
    		j.setSector(new LawSector(fall.rechtsbereich));
    		j.setSentence(fall.strafmass);
    		j.setComittee(new Committee("com2"));
    		j.setFileReference(file.getName());
    		
    		TableJudgement t = new TableJudgement();
    		try {
  			con.executeUpdate(t.getInsertSQLCode(j));
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		
    		con.close;
    	  }
    	}
    }
}

package de.hwrberlin.it2014.sweproject.cbr;

import de.hwrberlin.it2014.sweproject.cbr.Case;
import de.hwrberlin.it2014.sweproject.database.DatabaseConnection;
import de.hwrberlin.it2014.sweproject.database.Judgement;

import java.sql.SQLException;
import java.util.ArrayList;
import javax.json.*;

/**
 * controls the cbr-cycle. Start the algorithm with startCBR(ArrayList<String>) method.
 * @author Max Bock & Felix Lehmann
 * 
 */
public class CBR {
	
	private ArrayList<Case> activeCases; //all cases from current userRequests
	DatabaseConnection dbc;
	
	public CBR()
	{
		dbc=new DatabaseConnection();
        String host="localhost";
        String database="swe_project";
        String user="user";
        String pwd="pwd";
		dbc.connectToMysql(host, database, user, pwd);
		activeCases = new ArrayList<Case>();
	}
	
	public String startCBR(ArrayList<String> usersInput)
	{
		ArrayList<Judgement> judgList;
		try {
			judgList=retrieve(usersInput);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			judgList=new ArrayList<Judgement>();
			e.printStackTrace();
		}
		return judgmentListToJson(judgList);
		//calls retrieve:CaseList
		
		//calls reuse and revise
		//build HTTPResponse
	}
	
	private String judgmentListToJson(ArrayList<Judgement> judgList) {
		// TODO Auto-generated method stub
		String build="[";
		for(Judgement j : judgList)
		{
			build+="{\"sentence\":\""+j.getSentence()+"\",";
			build+="\"keywords\":\""+j.getKeywords()+"\",";
			build+="\"offence\":\""+j.getOffence()+"\",";
			build+="\"date\":\""+j.getDate().toString()+"\"";
			//build+="\"comitte\":\""+j.getComittee()+"\",";
			build+="}";
		}
		build+="]";
		return build;
	}

	public String saveUserEvaluate(String evaluation)
	{
		return evaluation;
		//calls retain and write Case to DB
		//build response if successful or not
	}
	
	private ArrayList<Judgement> retrieve(ArrayList<String> usersInput) throws SQLException
	{
		Case c = new Case(usersInput);
		activeCases.add(c);
		return c.getSimiliarFromDB(dbc);
		//recieves and returns a CaseList with similiar cases
	}
	
	private void retain(Case evaluatedCase)
	{
		//return something; boolean or HTTPCode(int)?
		//save the evalution from userResponse to the DB
	}
}

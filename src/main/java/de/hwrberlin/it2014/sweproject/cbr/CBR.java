package de.hwrberlin.it2014.sweproject.cbr;

import de.hwrberlin.it2014.sweproject.cbr.Case;
import de.hwrberlin.it2014.sweproject.database.DatabaseConnection;

import java.sql.SQLException;
import java.util.ArrayList;

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
	}
	
	public String startCBR(ArrayList<String> usersInput)
	{
		try {
			retrieve(usersInput);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		//calls retrieve:CaseList
		
		//calls reuse and revise
		//build HTTPResponse
	}
	
	public String saveUserEvaluate(String evaluation)
	{
		return evaluation;
		//calls retain and write Case to DB
	}
	
	private ArrayList<Case> retrieve(ArrayList<String> usersInput) throws SQLException
	{
		Case c = new Case(usersInput);
		activeCases.add(c);
		return c.getSimiliarFromDB(dbc);
		//create new CaseObject
		//save case in activeCases
		//new query to DB
		//recieves and returns a CaseList with similiar cases
	}
	
	private ArrayList<Case> reuse(ArrayList<Case> caseListFromDBQuery)
	{
		return caseListFromDBQuery;
		//edit and control the caselist from DB-query
	}
	
	private ArrayList<Case> revise(ArrayList<Case> caseListFromReuse)
	{
		return caseListFromReuse;
		//build HTTPResponse
	}
	
	private void retain(Case evaluatedCase)
	{
		//return something; boolean or HTTPCode(int)?
		//save the evalution from userResponse to the DB
	}
}

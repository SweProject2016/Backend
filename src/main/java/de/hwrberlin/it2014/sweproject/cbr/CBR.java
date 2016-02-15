package de.hwrberlin.it2014.sweproject.cbr;

import de.hwrberlin.it2014.sweproject.cbr.Case.java;
import java.util.ArrayList;

/**
 * controls the cbr-cycle. Start the algorithm with startCBR(ArrayList<String>) method.
 * @author Max Bock
 * 
 */
public class CBR {
	
	private ArrayList<Case> activeCases; //all cases from current userRequests
	
	
	public String startCBR(ArrayList<String> usersInput)
	{
		//calls retrieve:CaseList
		
		//calls reuse and revise
		//build HTTPResponse
	}
	
	public String saveUserEvaluate(Case evaluatedCase)
	{
		//calls retain and write Case to DB
	}
	
	private ArrayList<Case> retrieve(ArrayList<String> usersInput)
	{
		//create new CaseObject
		//save case in activeCases
		//new query to DB
		//recieves a CaseList with similiar cases
	}
	
	private ArrayList<Case> reuse(ArrayList<Case> caseListFromDBQuery)
	{
		//edit and control the caselist from DB-query
	}
	
	private ArrayList<Case> revise(ArrayList<Case> caseListFromReuse)
	{
		//build HTTPResponse
	}
	
	private void retain(Case evaluatedCase)
	{
		//save the evalution from userResponse to the DB
	}
}

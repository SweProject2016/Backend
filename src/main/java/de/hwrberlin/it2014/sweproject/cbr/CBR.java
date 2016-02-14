package de.hwrberlin.it2014.sweproject.cbr;

//import ArrayList;

/**
 * controls the cbr-cycle. Start the algorithm with startCBR(ArrayList<String>) method.
 * @author Max
 * 
 */
public class CBR {
	
	private ArrayList<Case> activeCases; //all cases from current userRequests
	
	
	public HTTPResponse startCBR(ArrayList<String> usersInput)
	{
		//calls retrieve:CaseList
		//calls reuse and revise
		//build HTTPResponse
	}
	
	public HTTPResponse saveUserEvaluate(Case evaluatedCase)
	{
		//calls retain and write Case to DB
	}
	
	private ArrayList<Case> retrieve(ArrayList<String> usersInput)
	{
		//create new CaseObject
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

package de.hwrberlin.it2014.sweproject.cbr;

import java.util.ArrayList;
import java.util.Map;

/**
 * 
 * @author Max Bock & Felix Lehmann
 *
 */
public class Case {
	
	private String description;
	private QueryBuilder queryBuilder;
	
	public Case(ArrayList<String> userInput)
	{
	    queryBuilder = new QueryBuilder();
	    String query = queryBuilder.buildquery(userInput);
		//description=userInput.toString();
	}
	
	public void getSimiliarFromDB()
	{
		//load similiar cases from DB for step: retrieve
	}
	
	public void saveEvaluation(Map<Case, String> evaluatedCases) 
	//alt. parameter: (ArrayList<Case> evaluatedCases) and add evaluation as attribute to case
	{
		//save the evaluated cases to DB for step: retain
	}
}

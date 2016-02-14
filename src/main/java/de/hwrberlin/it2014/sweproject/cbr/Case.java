package de.hwrberlin.it2014.sweproject.cbr;

/**
 * 
 * @author Max
 *
 */
public class Case {
	
	private String description;
	
	public Case(ArrayList<String> userInput)
	{
		description=userInput.toString();
	}
	
	public void getSimiliarFromDB()
	{
		//load similiar cases from DB for step: retrieve
	}
	
	public void saveEvaluation(Map<Case, Evaluation> evaluatedCases) 
	//alt. parameter: (ArrayList<Case> evaluatedCases) and add evaluation as attribute to case
	{
		//save the evaluated cases to DB for step: retain
	}
}

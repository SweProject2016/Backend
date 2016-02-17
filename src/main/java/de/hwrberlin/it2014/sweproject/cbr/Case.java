package de.hwrberlin.it2014.sweproject.cbr;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import de.hwrberlin.it2014.sweproject.database.DatabaseConnection;
import de.hwrberlin.it2014.sweproject.database.Judgement;

/**
 * 
 * @author Max Bock & Felix Lehmann
 *
 */
public class Case {
	
	private int id;
	private ArrayList<String> description;
	private String offense;
	
	public Case(ArrayList<String> userInput) //constructor for userrequest
	{
		description=userInput;
	}
		
	public ArrayList<Judgement> getSimiliarFromDB(DatabaseConnection dbc) throws SQLException
	{
		//queryBuilder = new QueryBuilder(); buildQuery is static
	    String query = QueryBuilder.buildQuery(description);
	    ResultSet rs=dbc.executeQuery(query);
	    ArrayList<Judgement> judgListFromQuery = dbc.convertResultSetToJudgementList(rs);
	    return judgListFromQuery;
		//load similiar cases from DB for step: retrieve
	}
	
	public void saveEvaluation(Case ca, String eval) 
	//alt. parameter: (ArrayList<Case> evaluatedCases) and ad	d evaluation as attribute to case
	{
		//save the evaluated cases to DB for step: retain
	}
	
	public String getDescription()
	{
		String sstream="";
		for (String s : description)
		{
			sstream+=s+" ";
		}
		return sstream;
	}
	
	public int getID()
	{
		return id;
	}
}

package de.hwrberlin.it2014.sweproject.cbr;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import de.hwrberlin.it2014.sweproject.database.DatabaseConnection;

/**
 * 
 * @author Max Bock & Felix Lehmann
 *
 */
public class Case {
	
	private ArrayList<String> description;
	private String offense;
	private QueryBuilder queryBuilder;
	
	public Case(ArrayList<String> userInput) //constructor for userrequest
	{
	    queryBuilder = new QueryBuilder();
	    String query = queryBuilder.buildQuery(userInput);
		description=userInput;
	}
	
	public Case(String sentence, String offense) //constructor for resultset
	{
		description = new ArrayList<String>();
		description.add(sentence);
		this.offense=offense;
	}
	
	public ArrayList<Case> getSimiliarFromDB(DatabaseConnection dbc) throws SQLException
	{
		//queryBuilder = new QueryBuilder(); buildQuery is static
	    String query = QueryBuilder.buildQuery(description);
	    ResultSet rs=dbc.executeQuery(query);
	    ArrayList<Case> casesFromQuery= new ArrayList<Case>();
	    int colSent=5; //right column? //column of sentence
	    int colOff=6; //column of offense
	    while (rs.next())
	    {
	    	Case c = new Case(rs.getString(colSent),rs.getString(colOff));
	    	casesFromQuery.add(c);
	    }
	    return casesFromQuery;
		//load similiar cases from DB for step: retrieve
	}
	
	public void saveEvaluation(Map<Case, String> evaluatedCases) 
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
}

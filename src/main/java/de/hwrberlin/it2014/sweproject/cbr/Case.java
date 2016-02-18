package de.hwrberlin.it2014.sweproject.cbr;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import de.hwrberlin.it2014.sweproject.database.DatabaseConnection;
import de.hwrberlin.it2014.sweproject.database.Judgement;
import de.hwrberlin.it2014.sweproject.database.Result;

/**
 * 
 * @author Max Bock & Felix Lehmann
 *
 */
public class Case {
	
	private int id;
	private ArrayList<String> description;
	private ArrayList<Judgement> similiarCases;
	
	/**
	 * @author Max Bock
	 * @param interne id, um später die evaluation zur Anfrage zu zu ordnen
	 * @param userInput
	 */
	public Case(int id, ArrayList<String> userInput) //constructor for userrequest
	{
		description=userInput;
		this.id=id;
	}
	
	/**
	 * @author Max Bock
	 * @param DatabaseConnection
	 * @return ArrayList of Sets containing all similiar cases from DB
	 * @throws SQLException
	 */
	public ArrayList<Result> getSimiliarFromDB(DatabaseConnection dbc) throws SQLException
	{
		//queryBuilder = new QueryBuilder(); buildQuery is static
	    String query = QueryBuilder.buildQuery(description);
	    ResultSet rs = dbc.executeQuery(query);
	    similiarCases = dbc.convertResultSetToJudgementList(rs);
	    return JudgementToResultList(similiarCases);
		//load similiar cases from DB for step: retrieve
	}
	
	/**
	 * 
	 * @param Case
	 * @param Evaluation
	 */
	public void saveEvaluation(Case ca, String eval) //soll Jean-Pierre machen
	//alt. parameter: (ArrayList<Case> evaluatedCases) and add evaluation as attribute to case
	{
		//TODO
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
	
	/**
	 * @author Max Bock
	 * @param JudgementList from DBQuery
	 * @return a ArrayList containing Result (userInput, Judgement and similiarity)
	 */
	private ArrayList<Result> JudgementToResultList(ArrayList<Judgement> judgList) {
		ArrayList<Result> rl=new ArrayList<Result>();
		for(Judgement j : judgList)
		{
			float sim=Result.calcSimilarity();
			rl.add(new Result(this.getDescription(), j, sim)); //wie berechnet sich die similiarity?!
		}
		return rl;
	}
}

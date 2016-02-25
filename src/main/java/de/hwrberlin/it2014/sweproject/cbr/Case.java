package de.hwrberlin.it2014.sweproject.cbr;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import de.hwrberlin.it2014.sweproject.database.DatabaseConnection;
import de.hwrberlin.it2014.sweproject.database.TableResultsSQL;
import de.hwrberlin.it2014.sweproject.model.Judgement;
import de.hwrberlin.it2014.sweproject.model.Result;

/**
 * 
 * @author Max Bock & Felix Lehmann
 *
 */
public class Case {
	
	private int id;
	private ArrayList<String> description;
	private ArrayList<Result> similiarCases;
	//private String evaluation;
	
	/**
	 * @author Max Bock
	 * @param interne id, um sp�ter die evaluation zur Anfrage zu zu ordnen
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
	    similiarCases = judgementToResultList(dbc.convertResultSetToJudgementList(rs));
	    return similiarCases;
		//load similiar cases from DB for step: retrieve
	}
	
	/**
	 * 
	 * @param Case
	 * @param Evaluation
	 */
	public void saveEvaluation(String eval) //soll Jean-Pierre machen
	//alt. parameter: (ArrayList<Case> evaluatedCases) and add evaluation as attribute to case
	{
		//TODO
		//save the evaluated cases to DB for step: retain
		ArrayList<String> insertQuery=new ArrayList<>(); 
		for(Result r : similiarCases)
		{
			insertQuery.add(TableResultsSQL.getInsertSQLCode(r));
		}
		for(String query : insertQuery)
		{
			//execute query
		}
		
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
	private ArrayList<Result> judgementToResultList(ArrayList<Judgement> judgList) {
		ArrayList<Result> rl=new ArrayList<Result>();
//		ScoreProcessor sp=new ScoreProcessor();
		for(Judgement j : judgList)
		{
			float sim=1.0f;
			rl.add(new Result(this.getDescription(), j, sim));
		}
		return rl;
	}
}

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
//	    String query = QueryBuilder.buildQuery(description);
//	    ResultSet rs = dbc.executeQuery(query);
		ScoreProcessor<Judgement> scoreProc=new ScoreProcessor<Judgement>();
		ArrayList<Judgement> judgList= scoreProc.getBestMatches(description, 15, (long) 100, "");
	    similiarCases = judgementToResultList(judgList);
	    return similiarCases;
	    //maybe use ScoreProcessor.getBestMatches() instead
	}
	
	/**
	 * 
	 * @param Case
	 * @param Evaluation (true = passend; false = unpassend)
	 */
	public void saveEvaluation(DatabaseConnection dbc, boolean[] evaluation)
	//alt. parameter: (ArrayList<Case> evaluatedCases) and add evaluation as attribute to case
	{
		//TODO
		//save the evaluated cases to DB for step: retain
		//add evaluation evaluation to query
		ArrayList<String> insertQueries=new ArrayList<>(); 
		for(Result r : similiarCases)
		{
			insertQueries.add(TableResultsSQL.getInsertSQLCode(r));
			//add the evaluation?
		}
		for(String insertQuery : insertQueries)
		{
			try {
				dbc.executeUpdate(insertQuery);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		ScoreProcessor<Judgement> sp=new ScoreProcessor<Judgement>();
		for(Judgement j : judgList)
		{
			rl.add(new Result(this.getDescription(), j, (float) sp.getDistance(j, description, j.getTimestamp()), j.getDate()));
		}
		return rl;
	}
}

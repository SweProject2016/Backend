package de.hwrberlin.it2014.sweproject.cbr;

import java.sql.SQLException;
import java.util.ArrayList;

import de.hwrberlin.it2014.sweproject.database.DatabaseConnection;
import de.hwrberlin.it2014.sweproject.database.DatabaseConfig;
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
	private ArrayList<Judgement> similiarCases;
	private ScoreProcessor<Judgement> scoreProc;
	
	/**
	 * @author Max Bock
	 * @param interne id, um später die evaluation zur Anfrage zu zu ordnen
	 * @param userInput
	 */
	public Case(int id, ArrayList<String> userInput)
	{
		description=userInput;
		this.id=id;
		scoreProc = new ScoreProcessor<Judgement>();
	}
	
	/**
	 * @author Max Bock
	 * @param count of sets to return 
	 * @return ArrayList of Sets containing all similiar cases from DB
	 * @throws SQLException
	 */
	public ArrayList<Judgement> getSimiliarFromDB(int number) throws SQLException
	{
		
		similiarCases= scoreProc.getBestMatches(description, number, (long) 100, ""); //TODO whats the correct long
	    return similiarCases;
	}
	
	/**
	 * @author Max Bock
	 * @param Evaluation 
	 */
	public void saveEvaluation(float[] evaluation)
	{
		ArrayList<String> insertQueries=new ArrayList<>(); 
		for(int i=0; i < similiarCases.size(); i++)
		{
			Judgement j = similiarCases.get(i);
			Result result = newResultFromJudgement(j);
			result.setEvaluation(evaluation[i]);
			insertQueries.add(TableResultsSQL.getInsertSQLCode(result));
		}
		for(String insertQuery : insertQueries)
		{
			DatabaseConnection dbc=new DatabaseConnection();
			dbc.connectToMysql(DatabaseConfig.DB_HOST, DatabaseConfig.DB_NAME, 
					DatabaseConfig.DB_USER, DatabaseConfig.DB_PASSWORD);
			try {
				dbc.executeUpdate(insertQuery);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * erzeugt ein Resultobjekt aus einem Judgement für diesen Fall(also mit der aktuellen Useranfrage)
	 * @author Max Bock
	 * @param Judgement
	 * @return Result
	 */
	private Result newResultFromJudgement(Judgement j) {
		Result r=new Result(
				this.getDescription(), 
				j, 
				(float) scoreProc.getCachedScore(j), 
				j.getDate());
		return r;
	}

	
	public ArrayList<String> getDescription(boolean asList)
	{
		return description;
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
	 *
	private ArrayList<Result> judgementToResultList(ArrayList<Judgement> judgList) {
		ArrayList<Result> rl=new ArrayList<Result>();
		ScoreProcessor<Judgement> sp=new ScoreProcessor<Judgement>();
		for(Judgement j : judgList)
		{
			rl.add(new Result(this.getDescription(), j, (float) sp.getDistance(j, description, j.getTimestamp()), j.getDate()));
		}
		return rl;
	}/* */
}

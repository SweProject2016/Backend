package de.hwrberlin.it2014.sweproject.cbr;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

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
public class Request {
	
	private ArrayList<String> description;
	private ScoreProcessor<Judgement> scoreProc;
	private Date dateOfRequest;
	
	/**
	 * @author Max Bock
	 * @param userInput
	 */
	public Request(ArrayList<String> userInput)
	{
		description=userInput;
		scoreProc = new ScoreProcessor<Judgement>();
		dateOfRequest=new Date();
	}
	
	/**
	 * @author Max Bock
	 * @param count of sets to return 
	 * @return ArrayList of Sets containing all similiar cases from DB
	 * @throws SQLException
	 */
	public ArrayList<Result> getSimiliarFromDB(int number) throws SQLException
	{
		ArrayList<Judgement> similarCases = scoreProc.getBestMatches(description, number, (long) -1, null); 
		//TODO separate the timestamp and lawsector from userinput
	    return writeJudgementToDBAsResult(similarCases);
	}
	
	/**
	 * @author Max Bock
	 * @param judgList
	 * @return
	 */
	private ArrayList<Result> writeJudgementToDBAsResult(ArrayList<Judgement> judgList)
	{
		DatabaseConnection dbc=new DatabaseConnection();
		dbc.connectToMysql(DatabaseConfig.DB_HOST, DatabaseConfig.DB_NAME, 
				DatabaseConfig.DB_USER, DatabaseConfig.DB_PASSWORD);
		ArrayList<Result> resList = new ArrayList<>();
		for(Judgement j : judgList)
		{
			Result r = newResultFromJudgement(j);
			String sql = TableResultsSQL.getInsertSQLCode(r);
			try { //write to DB
				dbc.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			//read the id, r.setID()
			resList.add(r);
		}
		return resList;
	}
	
	/**
	 * erzeugt ein Resultobjekt aus einem Judgement für diesen Fall(also mit der aktuellen Useranfrage)
	 * @author Max Bock
	 * @param Judgement
	 * @return Result
	 */
	private Result newResultFromJudgement(Judgement j) 
	{
		Result r=new Result(
				this.getDescription(), 
				j, 
				(float) scoreProc.getCachedScore(j), 
				this.dateOfRequest);
		return r;
	}
	
	/**
	 * gibt die Beschreibung (User Input) als ArrayList zurück
	 * @author Max Bock
	 * @param asList boolean (wert egal)
	 * @return UserInput as ArrayList<String>
	 */
	public ArrayList<String> getDescription(boolean asList)
	{
		return description;
	}
	
	/**
	 * @author Max Bock
	 * @return UserInput as String
	 */
	public String getDescription()
	{
		String sstream="";
		for (String s : description)
		{
			sstream+=s+" ";
		}
		return sstream;
	}

	public Date getDateOfRequest() {
		return dateOfRequest;
	}
	
	/**
	 * @author Max Bock
	 * @param evaluation
	 *
	public void saveEvaluation(int numberOfJudgement, float evaluation)
	{
		DatabaseConnection dbc=new DatabaseConnection();
		dbc.connectToMysql(DatabaseConfig.DB_HOST, DatabaseConfig.DB_NAME, 
				DatabaseConfig.DB_USER, DatabaseConfig.DB_PASSWORD);
		Judgement j = similarCases.get(numberOfJudgement);
		if(null!=j && !evaluatedJudgements.contains(j))
		{
			evaluatedJudgements.add(j);
			Result result = newResultFromJudgement(j);
			result.setUserRating(evaluation);
			String insertQuery=TableResultsSQL.getInsertSQLCode(result);
			try {
				dbc.executeUpdate(insertQuery);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * prüft, ob alle ähnlichen Fälle dieser Anfrage bewertet wurden.
	 * @author Max Bock
	 * @return true, alle bewertet; false, mind. ein Fall nicht bewertet
	 *
	public boolean isCompletelyEvaluated() {
		if(evaluatedJudgements.isEmpty())
			return false;
		for(Judgement e : similarCases)
		{
			if(!evaluatedJudgements.contains(e))
				return false;
		}
		return true;
	}*/
}

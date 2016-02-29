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
public class Case {
	
	private int id;
	private ArrayList<String> description;
	private ArrayList<Judgement> similarCases;
	private ScoreProcessor<Judgement> scoreProc;
	private ArrayList<Judgement> evaluatedJudgements;
	private Date dateOfRequest;
	
	/**
	 * @author Max Bock
	 * @param interne id, um spï¿½ter die evaluation zur Anfrage zu zu ordnen
	 * @param userInput
	 */
	public Case(int id, ArrayList<String> userInput)
	{
		description=userInput;
		this.id=id;
		scoreProc = new ScoreProcessor<Judgement>();
		setDateOfRequest(new Date());
	}
	
	/**
	 * @author Max Bock
	 * @param count of sets to return 
	 * @return ArrayList of Sets containing all similiar cases from DB
	 * @throws SQLException
	 */
	public ArrayList<Judgement> getSimiliarFromDB(int number) throws SQLException
	{
		similarCases = scoreProc.getBestMatches(description, number, (long) -1, null); 
		//TODO separate the timestamp and lawsector from userinput
	    return similarCases;
	}
	
	/**
	 * @author Max Bock
	 * @param evaluation
	 */
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
				j.getDate());
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
	
	public int getID()
	{
		return id;
	}

	/**
	 * prüft, ob alle ähnlichen Fälle dieser Anfrage bewertet wurden.
	 * @author Max Bock
	 * @return true, alle bewertet; false, mind. ein Fall nicht bewertet
	 */
	public boolean isCompletelyEvaluated() {
		if(evaluatedJudgements.isEmpty())
			return false;
		for(Judgement e : similarCases)
		{
			if(!evaluatedJudgements.contains(e))
				return false;
		}
		return true;
	}

	public Date getDateOfRequest() {
		return dateOfRequest;
	}
}

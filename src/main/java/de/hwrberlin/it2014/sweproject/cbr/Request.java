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
	public ArrayList<Result> getSimilarFromDB(int number) throws SQLException
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
		dbc.connectToMysql();
		ArrayList<Result> resList = new ArrayList<>();
		for(Judgement j : judgList)
		{
			Result r = newResultFromJudgement(j);
			int id=0;
			String sql = TableResultsSQL.getInsertSQLCode(r);
			try { //write to DB
				id = dbc.executeUpdateRetrieveID(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			//read the id, r.setID()
			r.setID(id);
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

	public Date getDateOfRequest() 
	{
		return dateOfRequest;
	}
}

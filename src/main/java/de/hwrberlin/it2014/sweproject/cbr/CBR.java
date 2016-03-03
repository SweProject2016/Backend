package de.hwrberlin.it2014.sweproject.cbr;

import de.hwrberlin.it2014.sweproject.cbr.Request;
import de.hwrberlin.it2014.sweproject.database.DatabaseConfig;
import de.hwrberlin.it2014.sweproject.database.DatabaseConnection;
import de.hwrberlin.it2014.sweproject.database.TableResultsSQL;
import de.hwrberlin.it2014.sweproject.model.Result;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * controls the cbr-cycle. Start the algorithm with startCBR(ArrayList<String>) method.
 * 
 * @author Max Bock & Felix Lehmann
 * 
 */
public class CBR {
	
	//private ArrayList<Case> activeCases; //all cases from current userRequests
	private int COUNT_TO_RETURN;
	
	public CBR()
	{
		//activeCases = new ArrayList<Case>();
		COUNT_TO_RETURN=30;
	}
	public CBR(int count)
	{
		//activeCases = new ArrayList<Case>();
		COUNT_TO_RETURN=count;
	}
	
	/* ********************** Retrieve ****************************** */
	/**
	 * leitet Nutzeranfrage weiter und gibt aehnliche Faelle zurueck
	 * @author Max Bock
	 * @param usersInput (String[])
	 * @return aehnliche Faelle
	 */
	public ArrayList<Result> startCBR(String[] usersInput)
	{
		ArrayList<String> al = new ArrayList<>();
		for(String s : usersInput){ al.add(s); }
		return startCBR(al);
	}
	
	/**
	 * leitet Nutzeranfrage weiter und gibt aehnliche Faelle zurueck
	 * @author Max Bock
	 * @param usersInput (String)
	 * @return aehnliche Faelle
	 */
	public ArrayList<Result> startCBR(String usersInput)
	{
		String[] ar = usersInput.split(" ");
		return startCBR(ar);
	}
	
	/**
	 * leitet Nutzeranfrage weiter und gibt aehnliche Faelle zurueck
	 * @author Max Bock
	 * @param usersInput (ArrayList<String>)
	 * @return aehnliche Faelle
	 */
	public ArrayList<Result> startCBR(ArrayList<String> usersInput)
	{
		ArrayList<Result> resList;
		try {
			Request rq = new Request(usersInput);
			resList=rq.getSimilarFromDB(COUNT_TO_RETURN);
		} catch (SQLException e) {
			resList=new ArrayList<>();
			e.printStackTrace(); 
		}
		return resList;
	}
	/* ********************** End Retrieve ****************************** */
	
	/* ********************** User Rating ****************************** */
	/**
	 * speichert die Bewertung zu einem Fall einer Anfrage
	 * @param id der Anfrage
	 * @param evaluation Bewertung
	 * @return
	 * @throws SQLException 
	 */
	public String saveUserRating(int idOfResult, float rating) throws SQLException
	{
		//Request r = new Request();
		String query = TableResultsSQL.getSelectSQLCode(idOfResult);
		DatabaseConnection dbc=new DatabaseConnection();
		dbc.connectToMysql();
		ResultSet rs = dbc.executeQuery(query);
		ArrayList<Result> rl = dbc.convertResultSetToResultList(rs);
		Result result = rl.get(0);
		result.setUserRating(rating);
		String updateQuery = TableResultsSQL.getUpdateSQLCodeForUserRating(result);
		dbc.executeUpdate(updateQuery);
		return null;
	}
}
package de.hwrberlin.it2014.sweproject.cbr;

import de.hwrberlin.it2014.sweproject.cbr.Request;
import de.hwrberlin.it2014.sweproject.database.DatabaseConnection;
import de.hwrberlin.it2014.sweproject.database.TableResultsSQL;
import de.hwrberlin.it2014.sweproject.model.Result;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * implementiert den FBS-Zyklus. Im Konstruktor kann die Anzahl der Ergebnisse angegeben werden.
 * Zum Starten aufrufen: startCBR(ArrayList<String> oder String[] oder String).
 * saveUserRating(int,float) speichert die NutzerWertung zu einem bestimmten Fall
 * 
 * @author Max Bock & Felix Lehmann
 * 
 */
public class CBR {
	
	private int COUNT_TO_RETURN;
	
	public CBR()
	{
		COUNT_TO_RETURN=30;
	}
	public CBR(int count)
	{
		COUNT_TO_RETURN=count;
	}
	
	/* ********************** Retrieve ****************************** */
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
	 * @param usersInput (ArrayList<String>)
	 * @return aehnliche Faelle
	 */
	public ArrayList<Result> startCBR(ArrayList<String> usersInput)
	{
		ArrayList<Result> resList;
		try 
		{
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
	 * @return null
	 * @throws SQLException 
	 */
	public String saveUserRating(int idOfResult, float rating) throws SQLException
	{
		if(idOfResult<=-1)
			return "invalid id";
		String query = TableResultsSQL.getSelectSQLCode(idOfResult);
		DatabaseConnection dbc=new DatabaseConnection();
		dbc.connectToMysql();
		ResultSet rs = dbc.executeQuery(query);
		ArrayList<Result> rl = dbc.convertResultSetToResultList(rs);
		Result result = rl.get(0);
		if(result.getUserRating()!=0.0f){
			result.setUserRating(rating);
			String updateQuery = TableResultsSQL.getUpdateSQLCodeForUserRating(result);
			dbc.executeUpdate(updateQuery);
			return "success";
		}
		else
		{
			return "fail: is already rated";
		}
	}
	/**
	 * speichert die Bewertung eines Falles
	 * 
	 * @param id ID des gelieferten Results
	 * @param rating Bewertung des Results
	 * @return @code true, wenn die Bewertung erfolgreich war, @code false bei allen anderen Fehlern
	 */
	public boolean saveRating(int id, float rating) {
		
		//TODO: Check, ob Result bereits bewertet wurde
		DatabaseConnection con = new DatabaseConnection();
		int update = TableResultsSQL.saveUserRating(id, rating, con);
		if(update <=0){
			return false;
		}
		return true;
	}
}
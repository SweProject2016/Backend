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
		ArrayList<Result> judgList;
		try {
			judgList=retrieve(usersInput);
		} catch (SQLException e) {
			judgList=new ArrayList<>();
			e.printStackTrace(); 
		}
		return judgList;
	}
	
	/**
	 * speichert die Bewertung zu einem Fall einer Anfrage
	 * @param id der Anfrage
	 * @param numberOfJudgement ist die Nummer des Falls in der bestimmten Anfrage
	 * @param evaluation Bewertung
	 * @return
	 * @throws SQLException 
	 */
	public String saveUserRating(int idOfResult, float rating) throws SQLException
	{
		//Request r = new Request();
		String query = TableResultsSQL.getSelectSQLCode(idOfResult);
		DatabaseConnection dbc=new DatabaseConnection();
		dbc.connectToMysql(DatabaseConfig.DB_HOST, DatabaseConfig.DB_NAME, 
				DatabaseConfig.DB_USER, DatabaseConfig.DB_PASSWORD);
		ResultSet rs = dbc.executeQuery(query);
		ArrayList<Result> rl = dbc.convertResultSetToResultList(rs);
		Result result = rl.get(0);
		result.setUserRating(rating);
		//TableResultsSQL.getUpdateSQLCodeForUserRating(r);
		return null;
	}

	/**
	 * interne Methode fuer den CBR-Zyklus
	 * @author Max Bock
	 * @param usersInput
	 * @return aehnliche Faelle
	 * @throws SQLException
	 */
	private ArrayList<Result> retrieve(ArrayList<String> usersInput) throws SQLException
	{
		Request c = new Request(usersInput);
		//activeCases.add(c);
		return c.getSimilarFromDB(COUNT_TO_RETURN);
	}
	
	/**
	 * Loescht einen Fall aus den activeCases
	 * @author Max Bock
	 * @param CaseID
	 * @return boolean
	 *
	private boolean removeCaseByID(int id)
	{
		Case c = getCaseByID(id);
		if(null!=c)
			return activeCases.remove(c);
		else
			return false;
	}
	
	/**
	 * liefert fuer die Bewertung anhand der ID den Case zurueck
	 * @author Max Bock
	 * @param interne Case ID
	 * @return Case
	 *
	private Case getCaseByID(int id) 
	{
		for(Case c :activeCases)
		{
			if(c.getID()==id)
			{
				return c;
			}
		}
		return null;
	}
	
	
	
	/**
	 * @author Max Bock
	 * @return highest ID in activeCases
	 *
	private int getHighestID()
	{
		int id=0;
		for(Case c: activeCases)
		{
			if(c.getID()>id)
			{
				id=c.getID();
			}
		}
		return id;
	}
	
	public ArrayList<Case> getActiveCases()
	{
		return activeCases;
	}
	
	/**
	 * löscht alle Fälle aus den activeCases, die älter als ein Tag sind
	 * @author Max Bock
	 * @return count of deleted cases(/requests)
	 *
	public int removeOldCases()
	{	
		return removeOldCases((int)24);
	}
	
	/**
	 * löscht alle Fälle aus den activeCases, die älter als hours (Parameter int) sind
	 * @author Max Bock
	 * @param hours - int
	 * @return count of deleted cases(/requests)
	 *
	private int removeOldCases(int hours) 
	{
		long time = (long) hours * 60 * 60 * 1000;
		return removeOldCases(time);
	}

	/**
	 * löscht alle Fälle aus den active Cases (sind eigentlich die Nutzeranfragen!), die älter als der Parameter(miliseconds) sind
	 * sollte regelmäßig benutzt werden, damit nicht komplett bewertete Anfragen gelöscht werden
	 * miliseconds <= 1 löscht alle Fälle
	 * @author Max Bock
	 * @param miliseconds
	 * @return count of deleted cases(/requests)
	 *
	public int removeOldCases(long miliseconds)
	{
		int count = 0;
		if(1>=miliseconds)
		{
			count=activeCases.size();
			activeCases.clear();
		}
		else
		{
			Date current = new Date();
			Date before = new Date(current.getTime()-miliseconds);
			for(Case c : activeCases)
			{
				if(c.getDateOfRequest().before(before))
				{
					removeCaseByID(c.getID());
					count++;
				}
			}
		}
		return count;
	}*/
}
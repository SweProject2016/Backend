package de.hwrberlin.it2014.sweproject.cbr;

import de.hwrberlin.it2014.sweproject.cbr.Case;
import de.hwrberlin.it2014.sweproject.database.DatabaseConnection;
import de.hwrberlin.it2014.sweproject.database.Result;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * controls the cbr-cycle. Start the algorithm with startCBR(ArrayList<String>) method.
 * 
 * @author Max Bock & Felix Lehmann
 * 
 */
public class CBR {
	
	private ArrayList<Case> activeCases; //all cases from current userRequests
	DatabaseConnection dbc;
	
	/**
	 * erstellt eine Datenbankverbindung
	 * @author Max Bock
	 */
	public CBR()
	{
		dbc=new DatabaseConnection();
        String host="localhost";
        String database="swe_project";
        String user="user";
        String pwd="pwd";
		dbc.connectToMysql(host, database, user, pwd);
		activeCases = new ArrayList<Case>();
	}
	
	/**
	 * @author Max Bock
	 * @param usersInput (String[])
	 * @return ähnliche Fälle
	 */
	public ArrayList<Result> startCBR(String[] usersInput)
	{
		ArrayList<String> al = new ArrayList<>();
		for(String s:usersInput){
			al.add(s);
		}
		return startCBR(al);
	}
	
	/**
	 * @author Max Bock
	 * @param usersInput (String)
	 * @return ähnliche Fälle
	 */
	public ArrayList<Result> startCBR(String usersInput)
	{
		String[] ar = usersInput.split(" ");
		return startCBR(ar);
	}
	
	/**
	 * @author Max Bock
	 * @param usersInput (ArrayList<String>)
	 * @return ähnliche Fälle
	 */
	public ArrayList<Result> startCBR(ArrayList<String> usersInput)
	{
		ArrayList<Result> resultList;
		try {
			resultList=retrieve(usersInput);
		} catch (SQLException e) {
			resultList=new ArrayList<Result>();
			//TODO write to log
			e.printStackTrace(); 
		}
		return resultList;
	}

	/**
	 * 
	 * @param evaluation
	 * @return
	 */
	public String saveUserEvaluate(String evaluation) //jean-pierre
	{
		//TODO
		int id=0;
		retain (getCaseByID(id));
		return evaluation;
		//calls retain and write Case to DB
		//build response if successful or not
	}

	/**
	 * Löscht einen Fall aus den activeCases
	 * @author Max Bock
	 * @param CaseID
	 * @return boolean
	 */
	private boolean removeCaseByID(int id)
	{
		Case c = getCaseByID(id);
		if(null!=c)
			return activeCases.remove(c);
		else
			return false;
	}
	
	/**
	 * liefert für die Bewertung anhand der ID den Case zurück
	 * @author Max Bock
	 * @param interne Case ID
	 * @return Case
	 */
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
	 * interne Methode für den CBR-Zyklus
	 * @author Max Bock
	 * @param usersInput
	 * @return ähnliche Fälle
	 * @throws SQLException
	 */
	private ArrayList<Result> retrieve(ArrayList<String> usersInput) throws SQLException
	{
		Case c = new Case(getHighestID()+1,usersInput);
		activeCases.add(c);
		return c.getSimiliarFromDB(dbc);
		//recieves and returns a CaseList with similiar cases
	}
	
	/**
	 *
	 * @param evaluatedCase
	 */
	private void retain(Case evaluatedCase) //jean-pierre
	{
		//TODO
		//return something; boolean or HTTPCode(int)?
		//save the evalution from userResponse to the DB
		//remove Case from activeCases
	}
	
	/**
	 * @author Max Bock
	 * @return highest ID in activeCases
	 */
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
}

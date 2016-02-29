package de.hwrberlin.it2014.sweproject.cbr;

import de.hwrberlin.it2014.sweproject.cbr.Case;
import de.hwrberlin.it2014.sweproject.database.DatabaseConnection;
import de.hwrberlin.it2014.sweproject.model.Judgement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

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
		activeCases = new ArrayList<Case>();
	}
	
	/**
	 * @author Max Bock
	 * @param usersInput (String[])
	 * @return ï¿½hnliche Fï¿½lle
	 */
	public ArrayList<Judgement> startCBR(String[] usersInput)
	{
		ArrayList<String> al = new ArrayList<>();
		for(String s : usersInput){ al.add(s); }
		return startCBR(al);
	}
	
	/**
	 * @author Max Bock
	 * @param usersInput (String)
	 * @return ï¿½hnliche Fï¿½lle
	 */
	public ArrayList<Judgement> startCBR(String usersInput)
	{
		String[] ar = usersInput.split(" ");
		return startCBR(ar);
	}
	
	/**
	 * @author Max Bock
	 * @param usersInput (ArrayList<String>)
	 * @return ï¿½hnliche Fï¿½lle
	 */
	public ArrayList<Judgement> startCBR(ArrayList<String> usersInput)
	{
		ArrayList<Judgement> judgList;
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
	 */
	public String saveUserEvaluate(int id, int numberOfJudgement, float evaluation)
	{
		Case c = getCaseByID(id);
		c.saveEvaluation(numberOfJudgement, evaluation);
		if(c.isCompletelyEvaluated())
			removeCaseByID(c.getID());
		return null;
	}

	/**
	 * Lï¿½scht einen Fall aus den activeCases
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
	 * liefert fï¿½r die Bewertung anhand der ID den Case zurï¿½ck
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
	 * interne Methode fï¿½r den CBR-Zyklus
	 * @author Max Bock
	 * @param usersInput
	 * @return ï¿½hnliche Fï¿½lle
	 * @throws SQLException
	 */
	private ArrayList<Judgement> retrieve(ArrayList<String> usersInput) throws SQLException
	{
		Case c = new Case(getHighestID()+1,usersInput);
		activeCases.add(c);
		return c.getSimiliarFromDB(30); //change for more cases
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
	
	public ArrayList<Case> getActiveCases()
	{
		return activeCases;
	}
	
	/**
	 * löscht alle Fälle aus den activeCases, die älter als ein Tag sind
	 * @author Max Bock
	 * @return count of deleted cases(/requests)
	 */
	public int removeOldCases()
	{	
		return removeOldCases((int)24);
	}
	
	/**
	 * löscht alle Fälle aus den activeCases, die älter als hours (Parameter int) sind
	 * @author Max Bock
	 * @param hours - int
	 * @return count of deleted cases(/requests)
	 */
	private int removeOldCases(int hours) 
	{
		long time = (long) hours * 60 * 60 * 1000;
		return removeOldCases(time);
	}

	/**
	 * löscht alle Fälle aus den active Cases, die älter als der Parameter(miliseconds) sind
	 * sollte regelmäßig benutzt werden, damit nicht komplett bewertete Anfragen gelöscht werden
	 * miliseconds <= 1 löscht alle Fälle
	 * @author Max Bock
	 * @param miliseconds
	 * @return count of deleted cases(/requests)
	 */
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
	}
}
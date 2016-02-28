package de.hwrberlin.it2014.sweproject.cbr;

import de.hwrberlin.it2014.sweproject.cbr.Case;
import de.hwrberlin.it2014.sweproject.database.DatabaseConnection;
import de.hwrberlin.it2014.sweproject.model.Judgement;

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
		activeCases = new ArrayList<Case>();
	}
	
	/**
	 * @author Max Bock
	 * @param usersInput (String[])
	 * @return �hnliche F�lle
	 */
	public ArrayList<Judgement> startCBR(String[] usersInput)
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
	 * @return �hnliche F�lle
	 */
	public ArrayList<Judgement> startCBR(String usersInput)
	{
		String[] ar = usersInput.split(" ");
		return startCBR(ar);
	}
	
	/**
	 * @author Max Bock
	 * @param usersInput (ArrayList<String>)
	 * @return �hnliche F�lle
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
	 * @author Max Bock
	 * @param evaluation
	 * @return
	 */
	public String saveUserEvaluate(int id, float[] evaluation)
	{
		Case c = getCaseByID(id);
		retain(c, evaluation);
		return null;
	}

	/**
	 * L�scht einen Fall aus den activeCases
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
	 * liefert f�r die Bewertung anhand der ID den Case zur�ck
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
	 * interne Methode f�r den CBR-Zyklus
	 * @author Max Bock
	 * @param usersInput
	 * @return �hnliche F�lle
	 * @throws SQLException
	 */
	private ArrayList<Judgement> retrieve(ArrayList<String> usersInput) throws SQLException
	{
		Case c = new Case(getHighestID()+1,usersInput);
		activeCases.add(c);
		return c.getSimiliarFromDB(30); //change for more casess
	}
	
	/**
	 * @author Max Bock
	 * @param evaluatedCase
	 */
	private void retain(Case evaluatedCase, float[] evaluation)
	{
		evaluatedCase.saveEvaluation(evaluation);
		removeCaseByID(evaluatedCase.getID());
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

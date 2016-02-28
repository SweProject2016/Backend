package de.hwrberlin.it2014.sweproject.model;

import java.util.Date;

public class Result {
	private String userInput;
	private Judgement judgement;
	private float similarity;
	private Date date;
	
	public Result(String userInput, Judgement judgement, float similarity, Date date){
		this.userInput=userInput;
		this.judgement=judgement;
		this.similarity=similarity;
		this.date = date;
	}
	
	public String getUserInput(){
		return userInput;
	}
	
	public void setUserInput(String userInput){
		this.userInput=userInput;
	}
	
	public Judgement getJudgement(){
		return judgement;
	}
	
	public void setJudgement(Judgement judgement){
		this.judgement=judgement;
	}
	
	public float getSimilarity(){
		return similarity;
	}
	
	public void setSimilarity(float similarity){
		this.similarity=similarity;
	}
	
	public Date getDate(){
		return date;
	}
	
	/**
	 * @author Dominik Habel
	 *
	 * @param date MUST be YYYY/MM/DD
	 */
	public void setDate(Date date){
		this.date=date;
	}
}

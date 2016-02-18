package de.hwrberlin.it2014.sweproject.database;

public class Result {
	private String userInput;
	private Judgement judgement;
	private float similarity;
	
	public Result(String userInput, Judgement judgement, float similarity){
		this.userInput=userInput;
		this.judgement=judgement;
		this.similarity=similarity;
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

	public static float calcSimilarity() {
		// TODO Auto-generated method stub
		return 0;
	}
}

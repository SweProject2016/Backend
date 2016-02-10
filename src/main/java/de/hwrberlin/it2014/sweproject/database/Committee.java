package de.hwrberlin.it2014.sweproject.database;

public class Committee {
	private String name;
	
	public Committee(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name=name;
	}
}

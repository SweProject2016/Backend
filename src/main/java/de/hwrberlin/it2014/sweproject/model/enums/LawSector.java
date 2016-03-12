package de.hwrberlin.it2014.sweproject.model.enums;

public enum LawSector {
	UNDEFINED("undefined"),
	STRAFRECHT("Strafrecht"),
	ZIVILRECHT("Zivilrecht"),
	OEFFENTLICHES_RECHT("öffentliches Recht");
	
	private final String value;
	
	private LawSector(String value){
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}
   
	
}


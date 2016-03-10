package de.hwrberlin.it2014.sweproject.model.enums;

public enum LawSector {
	//Don't change the order of following elements
	UNDEFINED,STRAFRECHT,ZIVILRECHT,OEFFENTLICHES_RECHT;
   
	public int getDatabaseID(){
		return this.ordinal();
	}
}


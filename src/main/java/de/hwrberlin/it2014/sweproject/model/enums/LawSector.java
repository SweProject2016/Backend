package de.hwrberlin.it2014.sweproject.model.enums;

import java.util.HashMap;
import java.util.Map;

public enum LawSector {
	UNDEFINED("undefined"),
	STRAFRECHT("Strafrecht"),
	ZIVILRECHT("Zivilrecht"),
	OEFFENTLICHES_RECHT("öffentliches Recht");
	
	private final String value;
	private static final Map<String, LawSector> lookup = new HashMap<String, LawSector>();
	
	static {
		for(LawSector ls : LawSector.values()){
			lookup.put(ls.getValue(),ls);
		}
	}
	
	private LawSector(String value){
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}
	
	public static LawSector get(String name){
		return lookup.get(name);
	}
   
	
}


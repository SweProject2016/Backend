package de.hwrberlin.it2014.sweproject.database;

import de.hwrberlin.it2014.sweproject.model.LawSector;

public class TableLawSectorSQL {
	
	public static String getInsertSQLCode(LawSector ls){
		return "INSERT INTO tbl_law_sector (name) VALUES ('"+ls.getName()+"');";
	}
}

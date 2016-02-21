package de.hwrberlin.it2014.sweproject.database;

public class TableLawSectorSQL {
	
	public String getInsertSQLCode(LawSector ls){
		return "INSERT INTO tbl_law_sector (name) VALUES ('"+ls.getName()+"');";
	}
}

package de.hwrberlin.it2014.sweproject.database;

public class TableCommitteeSQL {
	
	public String getInsertSQLCode(Committee com){
		return "INSERT INTO tbl_committee (name) VALUES ('"+com.getName()+"');";
	}
}

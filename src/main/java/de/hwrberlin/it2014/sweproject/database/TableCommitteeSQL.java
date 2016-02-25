package de.hwrberlin.it2014.sweproject.database;

import de.hwrberlin.it2014.sweproject.model.Committee;

public class TableCommitteeSQL {
	
	public String getInsertSQLCode(Committee com){
		return "INSERT INTO tbl_committee (name) VALUES ('"+com.getName()+"');";
	}
}

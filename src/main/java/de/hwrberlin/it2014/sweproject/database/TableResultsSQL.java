package de.hwrberlin.it2014.sweproject.database;

import de.hwrberlin.it2014.sweproject.model.Result;

public class TableResultsSQL {
	public static String getInsertSQLCode(Result result){
		String sql = "INSERT INTO tbl_results (user_input, picked_file, similarity) SELECT ";
		sql+="'"+result.getUserInput()+"',";
		sql+="j.id,";
		sql+=result.getSimilarity()+" FROM tbl_judgement j WHERE j.file_reference = '"+result.getJudgement().getFileReference()+"';";
		
		return sql;
	}
}
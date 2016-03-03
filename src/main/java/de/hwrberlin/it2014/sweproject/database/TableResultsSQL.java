package de.hwrberlin.it2014.sweproject.database;

import de.hwrberlin.it2014.sweproject.model.Result;

public class TableResultsSQL {
	public static String getInsertSQLCode(Result result){
		String sql = "INSERT INTO tbl_results (user_input, picked_file, similarity, date, user_rating) SELECT ";
		sql+="'"+result.getUserInput()+"',";
		sql+="j.id,";
		sql+=result.getSimilarity()+",'"+result.getDate()+"',";
		sql+=(result.getUserRating()==0.0f)?"null":result.getUserRating();
		sql+=" FROM tbl_judgement j WHERE j.file_reference = '"+result.getJudgement().getFileReference()+"';";
		return sql;
	}
	
	public static String getUpdateSQLCode(Result result){
		String sql="";
		return sql;
	}
}

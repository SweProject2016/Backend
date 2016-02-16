package de.hwrberlin.it2014.sweproject.database;

public class TableJudgement {

	public String getInsertSQLCode(Judgement judgement){
		String sql = "INSERT INTO tbl_judgement (file_reference, committee, law_sector,"
				+ "date, sentence, offence, page_rank, pdf_filename,"
				+ "pdf_link, keywords) VALUES(";
		sql+=judgement.getFileReference()+",";
		//TODO: committee+law_sector-parse
		sql+=judgement.getDate()+",";
		sql+=judgement.getSentence()+",";
		sql+=judgement.getOffence()+",";
		sql+=judgement.getPageRank()+",";
		sql+=judgement.getPdfFileName()+",";
		sql+=judgement.getPdfLink()+",";
		sql+=judgement.getKeywords();
		sql+=");";
		return sql;
	}
}

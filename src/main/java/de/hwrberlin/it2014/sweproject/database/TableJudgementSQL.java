package de.hwrberlin.it2014.sweproject.database;

import de.hwrberlin.it2014.sweproject.model.Judgement;

public class TableJudgementSQL {

	public static String getInsertSQLCode(Judgement judgement){
		String sql = "INSERT INTO tbl_judgement (file_reference, committee, law_sector,"
				+ "date, sentence, offence, page_rank, pdf_filename,"
				+ "pdf_link, keywords) SELECT ";
		sql+="'"+judgement.getFileReference()+"',";
		//TODO: committee+law_sector-parse
		sql+="c.id,ls.id,";
		sql+="'"+judgement.getDate()+"',";
		sql+="'"+judgement.getSentence()+"',";
		sql+="'"+judgement.getOffence()+"',";
		sql+=judgement.getPageRank()+",";
		sql+="'"+judgement.getPdfFileName()+"',";
		sql+="'"+judgement.getPdfLink()+"',";
		sql+="'"+judgement.getKeywords();
		sql+="' FROM tbl_committee c, tbl_law_sector ls WHERE c.name='"+judgement.getComittee().getName();
		sql+="' AND ls.name='"+judgement.getSector().getName()+"';";
		System.out.println(sql);
		return sql;
	}
}

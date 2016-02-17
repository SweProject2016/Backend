package de.hwrberlin.it2014.sweproject.database;

import java.util.Date;

public class TestDBStuff {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Judgement j = new Judgement("C:/Test", "pdf1");
		j.setDate(new Date());
		TableJudgement t = new TableJudgement();
		System.out.println(t.getInsertSQLCode(j));
	}

}

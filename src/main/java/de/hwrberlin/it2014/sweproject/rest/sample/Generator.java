package de.hwrberlin.it2014.sweproject.rest.sample;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hwrberlin.it2014.sweproject.database.Committee;
import de.hwrberlin.it2014.sweproject.database.Judgement;
import de.hwrberlin.it2014.sweproject.database.LawSector;
import de.hwrberlin.it2014.sweproject.database.Result;

public class Generator {

	public List generate(String type, int entries, String userInput){
		switch(type.toLowerCase()){
		case "committee":
			return generateCommittee(entries);
		case "judgement":
			return generateJudgement(entries);
		case "lawsector":
			return generateLawSector(entries);
		case "result":
			return generateResult(entries, userInput);
		default:
			break;
		}
		return null;
	}

	private List<Result> generateResult(int entries, String userInput) {
		List<Result> resultList = new ArrayList<>();
		for(int i=0;i<entries;i++){
			float similarity = randomFloat();
			Result result = new Result(userInput, 
							generateJudgement(1).get(0), 
							similarity);
			resultList.add(result);
		}
		return resultList;
	}

	private float randomFloat(){
		return (float) Math.random();
	}
	
	private String createPdfLink() {
		return "pdfLink/"+randomInt();
	}

	private int randomInt() {
		return 10000 + (int)(Math.random() * 99999);
	}

	private String createPdfFileName() {
		return "pdfFile_"+randomInt();
	}

	private List<LawSector> generateLawSector(int entries) {
		List<LawSector> lawSectorList = new ArrayList<>();
		for(int i=0;i<entries;i++){
			LawSector lawsector = new LawSector("LawSector"+randomInt());
			lawSectorList.add(lawsector);
		}
		return lawSectorList;
	}

	private List<Judgement> generateJudgement(int entries) {
		List<Judgement> judgementList = new ArrayList<>();
		for(int i=0;i<entries;i++){
			Judgement judgement = new Judgement(createPdfLink(), createPdfFileName());
			judgement.setComittee(generateCommittee(1).get(0));
			judgement.setFileReference("fileref"+randomInt());
			judgement.setSentence("Sentence"+randomInt());
			judgement.setOffence("Offence"+randomInt());
			judgement.setKeywords(generateKeywords(5));
			judgement.setSector(generateLawSector(1).get(0));
			judgement.setDate(getDate());
			judgement.setPageRank(randomFloat());
			judgementList.add(judgement);
		}
		
		return judgementList;
	}
	
	private Date getDate(){
		Date date = new Date();
		return date;
	}

	private String generateKeywords(int size) {
		String str = "";
		for(int i=0;i<size;i++){
			str = str + " " + "keyword"+randomInt();
		}
		return str;
	}

	private List<Committee> generateCommittee(int entries) {
		List<Committee> committeeList = new ArrayList<>();
		for(int i=0;i<entries;i++){
			Committee aCommittee = new Committee("name"+randomInt());
			committeeList.add(aCommittee);
		}
		return committeeList;
	}	
}
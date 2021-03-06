package de.hwrberlin.it2014.sweproject.rest.sample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hwrberlin.it2014.sweproject.model.Committee;
import de.hwrberlin.it2014.sweproject.model.Judgement;
import de.hwrberlin.it2014.sweproject.model.Result;
import de.hwrberlin.it2014.sweproject.model.enums.LawSector;
/**
 * Generator Klasse für Sample REST API
 * 
 * Generiert zufällig prototypische Antworten für Anfragen an die API
 * 
 * @author csc
 *
 */
public class Generator {

	public List<?> generate(String type, int entries, String userInput, int startindex){
		switch(type.toLowerCase()){
		case "committee":
			return generateCommittee(entries);
		case "judgement":
			return generateJudgement(entries);
		/*case "lawsector":
			return generateLawSector(entries);*/
		case "result":
			return generateResult(entries, userInput,startindex);
		default:
			break;
		}
		return null;
	}

	private List<Result> generateResult(int entries, String userInput, int startindex) {
		List<Result> resultList = new ArrayList<>();
		for(int i=0;i<entries;i++){
			float similarity = generateSimilarity(i,startindex);
			Result result = new Result(userInput, 
							generateJudgement(1).get(0), 
							similarity, getDate());
			resultList.add(result);
		}
		return resultList;
	}

	private float generateSimilarity(int entry, int startindex) {
		float range = 0.1f;
		int size = 5;

	    return (float) (1 - (range * (startindex/size)) + range * Math.random());
	}

	private String createPdfFileName() {
		return "pdfFile_"+randomInt();
	}

	private String createPdfLink() {
		return "pdfLink/"+randomInt();
	}

	private float randomFloat(){
		return (float) Math.random();
	}

	private int randomInt() {
		return 10000 + (int)(Math.random() * 99999);
	}
	
	private int randomInt(int min, int max) {
		return min + (int)(Math.random() * max);
	}
	
	private List<Committee> generateCommittee(int entries) {
		List<Committee> committeeList = new ArrayList<>();
		for(int i=0;i<entries;i++){
			Committee aCommittee = new Committee("name"+randomInt());
			committeeList.add(aCommittee);
		}
		return committeeList;
	}

	private LawSector generateLawSector() {
		List<LawSector> sectors = new ArrayList<>();
			sectors.add(LawSector.OEFFENTLICHES_RECHT);
			sectors.add(LawSector.STRAFRECHT);
			sectors.add(LawSector.ZIVILRECHT);
			sectors.add(LawSector.UNDEFINED);
		return sectors.get(randomInt(0,3));
	}

	private List<Judgement> generateJudgement(int entries) {
		List<Judgement> judgementList = new ArrayList<>();
		for(int i=0;i<entries;i++){
			Judgement judgement = new Judgement(createPdfLink(), createPdfFileName());
			judgement.setComittee(generateCommittee(1).get(0));
			judgement.setFileReference("fileref"+randomInt());
			judgement.setSentence("Sentence"+randomInt());
			judgement.setOffence(generateOffence(20));
			judgement.setKeywords(generateKeywords(5));
			judgement.setLawSector(generateLawSector());
			judgement.setDate(getDate());
			judgement.setPageRank(randomFloat());
			judgementList.add(judgement);
		}
		
		return judgementList;
	}
	
	private String generateOffence(int size) {
		StringBuffer response = new StringBuffer();

		try {
			String url = "https://baconipsum.com/api/?type=all-meat&sentences=2&start-with-lorem=1&format=text";
			
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	
			// optional default is GET
			con.setRequestMethod("GET");
	
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
	
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		} catch(Exception e){
			e.printStackTrace();
		}
		return response.toString();
	}

	private String generateKeywords(int size) {
		String str = "";
		for(int i=0;i<size;i++){
			str = str + " " + "keyword"+randomInt();
		}
		return str;
	}

	private Date getDate(){
		Date date = new Date();
		return date;
	}

	public ArrayList<Result> setSampleSim(ArrayList<Result> resultList, int startindex) {
		for(int i=0;i<resultList.size();i++){
			Result aResult = resultList.get(i);
			aResult.setSimilarity(generateSimilarity(i, startindex));
		}
		return resultList;
	}	
}
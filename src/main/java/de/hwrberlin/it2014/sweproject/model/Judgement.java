package de.hwrberlin.it2014.sweproject.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import de.hwrberlin.it2014.sweproject.cbr.Scoreable;
import de.hwrberlin.it2014.sweproject.model.enums.LawSector;

public class Judgement implements Scoreable {

	public static final String KEYWORD_SEPERATOR = " ";

	private String fileReference, sentence, offence, pdfLink, pdfFileName, keywords;
	private Committee comittee;
	private LawSector lawSector;
	private Date date;
	private float pageRank;

	public Judgement(String pdfLink, String pdfFileName){
		this.pdfLink = pdfLink;
		this.pdfFileName = pdfFileName;
	}

	public LawSector getLawSector(){
		return lawSector;
	}

	public void setLawSector(LawSector sector){
		this.lawSector = sector;
	}

	public String getKeywords(){
		return keywords;
	}

	public void setKeywords(String keywords){
		this.keywords = keywords;
	}

	public String getPdfFileName(){
		return pdfFileName;
	}

	public void setPdfFileName(String pdfFileName){
		this.pdfFileName = pdfFileName;
	}

	public String getPdfLink(){
		return pdfLink;
	}

	public void setPdfLink(String pdfLink){
		this.pdfLink = pdfLink;
	}

	public String getFileReference(){
		return fileReference;
	}

	public void setFileReference(String fileReference){
		this.fileReference = fileReference;
	}

	public Committee getComittee(){
		return comittee;
	}

	public void setComittee(Committee comittee){
		this.comittee = comittee;
	}

	public Date getDate(){
		return date;
	}

	public void setDate(Date date){
		this.date = date;
	}

	public String getSentence(){
		return sentence;
	}

	public void setSentence(String sentence){
		this.sentence = sentence;
	}

	public String getOffence(){
		return offence;
	}

	public void setOffence(String offence){
		this.offence = offence;
	}

	@Override
	public float getPageRank(){
		return pageRank;
	}

	public void setPageRank(float pageRank){
		this.pageRank = pageRank;
	}

	/**
	 * @author Tobias Glaeser
	 */
	@Override
	public ArrayList<String> getKeywordsAsList(){
		/*
		 * keywords in arraylist parsen
		 * 
		 * dabei nicht nur die eigentlichen "keywords" wie in der db nutzen, sondern auch allen anderen textfelder des falles
		 * dadurch wird die präzision verbesser
		 */
		ArrayList<String> all = new ArrayList<>();
		all.addAll(Arrays.asList(keywords.split(KEYWORD_SEPERATOR)));
		all.addAll(Arrays.asList(offence.split(KEYWORD_SEPERATOR)));
		all.addAll(Arrays.asList(sentence.split(KEYWORD_SEPERATOR)));
		ArrayList<String> cleared = new ArrayList<>();
		for(String w : all) {
			if(w.trim().length() > 0) { // leere strings entfernen
				cleared.add(w);
			}
		}
		return cleared;
	}

	/**
	 * @author Tobias Glaeser
	 */
	@Override
	public long getTimestamp(){
		return date.getTime();
	}
}

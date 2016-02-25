package de.hwrberlin.it2014.sweproject.model;

import java.util.ArrayList;
import java.util.Date;

import de.hwrberlin.it2014.sweproject.cbr.Scoreable;

public class Judgement implements Scoreable {

	public static final String KEYWORD_SEPERATOR = " ";

	private String fileReference, sentence, offence, pdfLink, pdfFileName,
			keywords;
	private Committee comittee;
	private LawSector sector;
	private Date date;
	private float pageRank;

	public Judgement(String pdfLink, String pdfFileName){
		this.pdfLink = pdfLink;
		this.pdfFileName = pdfFileName;
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

	public LawSector getSector(){
		return sector;
	}

	public void setSector(LawSector sector){
		this.sector = sector;
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
		// parse keywords in arraylist
		String[] split = keywords.split(KEYWORD_SEPERATOR);
		ArrayList<String> l = new ArrayList<>(split.length);
		for(String s : split) {
			l.add(s);
		}
		return l;
	}

	/**
	 * @author Tobias Glaeser
	 */
	@Override
	public long getTimestamp(){
		return date.getTime();
	}
}

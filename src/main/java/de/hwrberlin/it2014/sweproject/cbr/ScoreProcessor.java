package de.hwrberlin.it2014.sweproject.cbr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import de.hwrberlin.it2014.sweproject.thesaurus.ThesaurusLoader;

/**
 * Klasse für Ähnlichkeitsanalyse
 * 
 * @author Tobias Glaeser
 * @since 11.02.2016 09:43:34
 */
public class ScoreProcessor {

	/**
	 * ignore timestamp flag
	 */
	public static final long IGNORE_TIMESTAMP = -1;

	private final double weights[];

	/**
	 * @author Tobias Glaeser
	 * @since 11.02.2016 09:45:21
	 * @param weights Gewichte für Scoring:
	 *            [0] : keyword (normal)
	 *            [1] : keyword (jura)
	 *            [2] : Weight pro Tag Unterschied
	 *            [3] : Weight fuer Location (TBD)
	 */
	public ScoreProcessor(double weights[]){
		this.weights = weights;
	}

	/**
	 * Constructor mit Default Weight
	 * 
	 * @author Tobias Glaeser
	 * @since 11.02.2016 09:46:36
	 */
	public ScoreProcessor(){
		this(new double[]{10, 20, 2, 0});
	}

	/**
	 * Distanz von keywords zu einem Fall
	 * 
	 * @author Tobias Glaeser
	 * @since 08.02.2016 11:22:09
	 * @param s entry to check
	 * @param queryKeywords query keywords with synonyms
	 * @param timestamp Zeitangabe im query (in ms)
	 * @return distance value
	 */
	private double getDistance(Scoreable s, ArrayList<String> filteredKeywords, long timestamp){
		double dist = 0d;

		// check keyword
		for(String keyword : filteredKeywords) {
			ArrayList<String> synynoms = expandSynonyms(keyword);
			boolean found = false;
			boolean isJura = false; // TODO check keyword ist Rechtsbegriff
			check: for(String sy : synynoms) {
				if(s.getKeywordsAsList().contains(sy.toLowerCase())) {
					found = true;
					break check;
				}
			}
			if(!found) {
				dist += isJura ? weights[1] : weights[0];
			}

		}

		// check timestamp
		if(timestamp != IGNORE_TIMESTAMP) {
			dist += (Math.abs(timestamp - s.getTimestamp()) / (1000 * 60 * 60 * 24)) * weights[2];
		}

		// TODO check geo/policatal distance

		return dist;
	}

	/**
	 * Findet die besten Matches
	 * 
	 * @author Tobias Glaeser
	 * @since 08.02.2016 11:33:09
	 * @param queryKeywords Raw query keywords (ungefiltert)
	 * @param number Anzahl der Fälle die max. zurückgegeben werden sollen
	 * @param timestamp Zeitangabe im query (in ms)
	 * @param lawsector Rechtsbereich (prefilter exclude only)
	 * @return liste mit ähnlichen Fällen (absteigende Ähnlichkeit)
	 */
	public ArrayList<Scoreable> getBestMatches(ArrayList<String> queryKeywords, int number, long timestamp, String lawsector){
		ArrayList<String> filteredKeywords = filterKeywords(queryKeywords);
		/**
		 * 
		 * TODO
		 * make sql prefilter req here
		 * filter by: lawsector
		 * build query: contains at leasgt one of the keywords
		 */
		// ArrayList<String> allKeywords = expandSynonyms(filteredKeywords); TODO use for sql prefilter
		ArrayList<Scoreable> prefilter = new ArrayList<>(); // TODO retrieve by prefilter sql
		final HashMap<Scoreable, Double> scores = new HashMap<>();
		for(Scoreable s : prefilter) {
			scores.put(s, getDistance(s, filteredKeywords, timestamp));
		}
		// sort
		ArrayList<Scoreable> ordered = new ArrayList<>(prefilter);
		Collections.sort(ordered, new Comparator<Scoreable>(){

			@Override
			public int compare(Scoreable o1, Scoreable o2){
				double d1 = scores.get(o1);
				double d2 = scores.get(o2);
				if(d1 < d2) {
					return -1;
				} else if(d2 > d2) {
					return 1;
				} else
					return 0;
			}
		});
		for(int i = 0; i < ordered.size() - number; i++) {
			ordered.remove(ordered.size() - 1);
		}
		return ordered;
	}

	/**
	 * Entfernt alle Keywords aus der Liste, die Synonym eines anderen sind
	 * 
	 * @author Tobias Glaeser
	 * @since 08.02.2016 11:48:20
	 * @param queryKeywords raw user query keywords
	 * @return Gefilterte Keyword Liste
	 */
	private ArrayList<String> filterKeywords(ArrayList<String> queryKeywords){
		ArrayList<String> filtered = new ArrayList<>();
		filtered.add(queryKeywords.get(0));
		for(int i = 1; i < queryKeywords.size(); i++) {
			String word = queryKeywords.get(i);
			ArrayList<String> preSublist = new ArrayList<>();
			for(int a = 0; a < i; a++) {
				preSublist.add(queryKeywords.get(a));
			}
			if(!expandSynonyms(filtered).contains(word.toLowerCase())) {
				filtered.add(word);
			}
		}
		return filtered;
	}

	/*public static ArrayList<String> getSynonyms(String word){
		// TODO forward call: alle synonyme für ein keyword
		return null;
	}*/

	/**
	 * Erweitert ein Keyword um Synonyme
	 * 
	 * @author Tobias Glaeser
	 * @since 08.02.2016 11:30:17
	 * @param keyword Keyword
	 * @return keyword Liste mit input keyword und synonymen
	 */
	public ArrayList<String> expandSynonyms(String keyword){
		ArrayList<String> words = new ArrayList<>();
		words.add(keyword);
		return expandSynonyms(words);
	}

	/**
	 * Erweitert Keyword Liste um Synonyme
	 * 
	 * @author Tobias Glaeser
	 * @since 08.02.2016 11:30:17
	 * @param keywords raw keyword list
	 * @return keyword liste mit input keywords und synonymen
	 */
	public ArrayList<String> expandSynonyms(ArrayList<String> keywords){
		ArrayList<String> allKeywords = new ArrayList<>(keywords);
		for(String keyword : keywords) {
			ArrayList<String> synonyms = ThesaurusLoader.getSynonyms(keyword.toLowerCase());
			for(String word : synonyms) {
				if(!allKeywords.contains(word.toLowerCase())) {
					allKeywords.add(word.toLowerCase());
				}
			}
		}
		return allKeywords;
	}
}

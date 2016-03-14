package de.hwrberlin.it2014.sweproject.cbr;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import de.hwrberlin.it2014.sweproject.database.DatabaseConnection;
import de.hwrberlin.it2014.sweproject.database.TableJudgementSQL;
import de.hwrberlin.it2014.sweproject.model.enums.LawSector;
import de.hwrberlin.it2014.sweproject.synonym.LawTester;
import de.hwrberlin.it2014.sweproject.synonym.ThesaurusLoader;

/**
 * Klasse f�r �hnlichkeitsanalyse
 * 
 * @author Tobias Glaeser
 * @since 11.02.2016 09:43:34
 */
public class ScoreProcessor<T extends Scoreable> {

	/**
	 * flag zum ignorieren der zeit
	 */
	public static final long IGNORE_TIMESTAMP = -1;

	private HashMap<T, Double> scoreCache = new HashMap<>();
	private final double weights[];
	private final double[] sortWeights;
	private HashMap<String, ArrayList<String>> synonymCache = new HashMap<>();
	private HashMap<String, Boolean> wordTypeCache = new HashMap<>(); // true = rechtsbegriff

	/**
	 * @author Tobias Glaeser
	 * @since 11.02.2016 09:45:21
	 * @param weights Gewichte f�r Scoring:
	 *            [0] : keyword (normal)
	 *            [1] : keyword (jura)
	 *            [2] : Weight pro Tag Unterschied
	 * 
	 * @param sortWeights Gewichte f�r Sortierung
	 *            [0] : -score
	 *            [1] : pagerank value
	 */
	public ScoreProcessor(double weights[], double sortWeights[]){
		this.weights = weights;
		this.sortWeights = sortWeights;
	}

	/**
	 * Constructor mit Default Weight
	 * 
	 * @author Tobias Glaeser
	 * @since 11.02.2016 09:46:36
	 */
	public ScoreProcessor(){
		this(new double[]{10, 20, 0.2}, new double[]{1, 0.2d});
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
	public double getDistance(T s, ArrayList<String> filteredKeywords, long timestamp){
		double dist = 0d;

		// check keyword
		for(String keyword : filteredKeywords) {
			ArrayList<String> synynoms = expandSynonyms(keyword);
			boolean found = false;
			boolean isJura = false;
			if(wordTypeCache.containsKey(keyword)) {
				isJura = wordTypeCache.get(keyword);
			} else {
				isJura = LawTester.testIfWordIsLawTerm(keyword);
				wordTypeCache.put(keyword, isJura);
			}
			check: for(String sy : synynoms) {
				for(String kw : s.getKeywordsAsList()) {
					if(kw.equalsIgnoreCase(sy)) { // check
						found = true;
						break check;
					}
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

		return dist;
	}

	/**
	 * Findet die besten Matches
	 * 
	 * @author Tobias Glaeser
	 * @since 08.02.2016 11:33:09
	 * @param queryKeywords Raw query keywords (ungefiltert)
	 * @param number Anzahl der F�lle die max. zur�ckgegeben werden sollen
	 * @param timestamp Zeitangabe im query (in ms)
	 * @param lawsector Rechtsbereich (nur effektiv fuer prefilter)
	 * @return liste mit �hnlichen F�llen (absteigende �hnlichkeit)
	 * @throws SQLException db error
	 */
	public ArrayList<T> getBestMatches(ArrayList<String> queryKeywords, int number, long timestamp, LawSector lawsector) throws SQLException{
		// alle keywords fuer query, gefilterte fuer distanz-berechnung
		ArrayList<String> filteredKeywords = filterKeywords(queryKeywords);
		ArrayList<String> allKeywords = expandSynonyms(filteredKeywords);
		DatabaseConnection con = new DatabaseConnection();
		PreparedStatement stmt = TableJudgementSQL.prepareSelect(allKeywords, con);
		ArrayList<T> prefilter = (ArrayList<T>) con.convertResultSetToJudgementList(stmt.executeQuery()); // cast ist sicher da Judgement implements scoreable
		con.close();
		final HashMap<T, Double> scores = new HashMap<>();
		for(T s : prefilter) {
			double dist = getDistance(s, filteredKeywords, timestamp);
			double score = -(dist * sortWeights[0]) + s.getPageRank() * sortWeights[1];
			scores.put(s, score);
		}
		scoreCache.putAll(scores);
		// sortieren absteigend
		ArrayList<T> ordered = new ArrayList<>(prefilter);
		Collections.sort(ordered, new Comparator<T>(){

			@Override
			public int compare(T o1, T o2){
				double v1 = scores.get(o1);
				double v2 = scores.get(o2);
				if(v1 > v2) {
					return -1;
				} else if(v1 < v2) {
					return 1;
				} else
					return 0;
			}
		});
		ArrayList<T> ret = new ArrayList<>();
		for(int i = 0; i < ordered.size() && i < number; i++) {
			ret.add(ordered.get(i));
		}
		return ret;
	}

	/**
	 * gibt gecachten score wert zur�ck
	 * achtung: vorher muss getBestMatches() ausgef�hrt worden sein.
	 * 
	 * @author Tobias Glaeser
	 * @param t case/judgement
	 * @return �hnlichkeitswert der f�r diesen fall berechnet wurde
	 */
	public double getCachedScore(T t){
		return scoreCache.get(t);
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

			ArrayList<String> synonyms;

			// test cache
			if(synonymCache.containsKey(keyword)) {
				synonyms = new ArrayList<>(synonymCache.get(keyword));
			} else {
				synonyms = ThesaurusLoader.getSynonyms(keyword.toLowerCase());
				synonymCache.put(keyword, synonyms);
			}
			for(String word : synonyms) {
				if(!allKeywords.contains(word.toLowerCase())) {
					allKeywords.add(word.toLowerCase());
				}
			}
		}
		return allKeywords;
	}
}

package de.hwrberlin.it2014.sweproject.cbr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Klasse zum Errechnen der PageRanks
 * 
 * @author Tobias Glaeser
 *
 */
public class PageRankProcessor {

	private final double dampingFactor;

	/**
	 * @author Tobias Glaeser
	 * @param dampingFactor Dämpfungsfaktor
	 */
	public PageRankProcessor(double dampingFactor){
		this.dampingFactor = dampingFactor;
	}

	/**
	 * constructor mit standard Dämpfungsfaktor
	 * 
	 * @author Tobias Glaeser
	 */
	public PageRankProcessor(){
		this(0.85d);
	}

	/**
	 * errechnet neue pageranks für eine liste von objekten
	 * 
	 * @author Tobias Glaeser
	 * @param rankables liste mit objekten deren PageRank berechnet werden soll
	 * @param iterations berechnungsiterationen für pagerank (lt. Google sind 100 ok)
	 * @return liste mit pageranks, sortiert nach elementen der eingabeliste
	 */
	public ArrayList<Double> recomputePageRanks(ArrayList<PageRankable> rankables, int iterations){
		// rueckwaerts mapping
		HashMap<PageRankable, ArrayList<PageRankable>> reverseLookup = new HashMap<>();
		LinkedHashMap<PageRankable, Double> cache = new LinkedHashMap<>();
		for(PageRankable p : rankables) {
			reverseLookup.put(p, new ArrayList<PageRankable>());
			cache.put(p, 1d); // startwerte setzen
		}
		for(PageRankable p : rankables) {
			for(PageRankable r : p.getReferenced()) {
				reverseLookup.get(r).add(p);
			}
		}
		for(int i = 0; i < iterations; i++) {
			LinkedHashMap<PageRankable, Double> newCache = new LinkedHashMap<>(cache); // temp cache clonen
			for(PageRankable site : rankables) {
				double r = (1d - rankables.size()) / dampingFactor;
				double r_sum = 0d;
				for(PageRankable referee : reverseLookup.get(site)) {
					r_sum += (cache.get(referee) / referee.getReferenced().size());
				}
				r += dampingFactor * r_sum;
				newCache.put(site, r);
			}
			// neuen cache als aktuellen setzen
			cache.putAll(newCache);
		}
		// in liste umformen
		ArrayList<Double> ranks = new ArrayList<>();
		for(PageRankable p : rankables) {
			ranks.add(cache.get(p));
		}
		return ranks;
	}
}

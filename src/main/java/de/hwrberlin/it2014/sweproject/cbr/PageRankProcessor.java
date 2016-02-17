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
	 * @param dampingFactor D�mpfungsfaktor
	 */
	public PageRankProcessor(double dampingFactor){
		this.dampingFactor = dampingFactor;
	}

	/**
	 * constructor mit standard D�mpfungsfaktor
	 * 
	 * @author Tobias Glaeser
	 */
	public PageRankProcessor(){
		this(0.85d);
	}

	/**
	 * errechnet neue pageranks f�r eine liste von objekten
	 * 
	 * @author Tobias Glaeser
	 * @param rankables liste mit objekten deren PageRank berechnet werden soll
	 * @param iterations berechnungsiterationen f�r pagerank
	 * @return liste mit pageranks, sortiert nach elementen der eingabeliste
	 */
	public ArrayList<Double> recomputePageRanks(ArrayList<PageRankable> rankables, int iterations){
		// reverse mapping for a->referee relation
		HashMap<PageRankable, ArrayList<PageRankable>> reverseLookup = new HashMap<>();
		LinkedHashMap<PageRankable, Double> cache = new LinkedHashMap<>();
		for(PageRankable p : rankables) {
			reverseLookup.put(p, new ArrayList<PageRankable>());
			cache.put(p, 1d); // set initial
		}
		for(PageRankable p : rankables) {
			for(PageRankable r : p.getReferenced()) {
				reverseLookup.get(r).add(p);
			}
		}
		for(int i = 0; i < iterations; i++) {
			LinkedHashMap<PageRankable, Double> newCache = new LinkedHashMap<>(cache); // clone cache temp
			for(PageRankable site : rankables) {
				double r = (1d - rankables.size()) / dampingFactor;
				double r_sum = 0d;
				for(PageRankable referee : reverseLookup.get(site)) {
					r_sum += (cache.get(referee) / referee.getReferenced().size());
				}
				r += dampingFactor * r_sum;
				newCache.put(site, r);
			}
			// set new cache as current
			cache.putAll(newCache);
		}
		// create list
		ArrayList<Double> ranks = new ArrayList<>();
		for(PageRankable p : rankables) {
			ranks.add(cache.get(p));
		}
		return ranks;
	}
}

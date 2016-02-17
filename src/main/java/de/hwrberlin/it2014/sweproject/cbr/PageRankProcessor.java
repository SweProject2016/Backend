package de.hwrberlin.it2014.sweproject.cbr;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Klasse zum Errechnen der PageRanks
 * 
 * @author Tobias Glaeser
 *
 */
public class PageRankProcessor {

	private final double attenuationFactor;

	/**
	 * @author Tobias Glaeser
	 * @param attenuationFactor Dämpfungsfaktor
	 */
	public PageRankProcessor(double attenuationFactor){
		this.attenuationFactor = attenuationFactor;
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
	 * Methode terminiert wenn valida Pagerank Konstellation gefunden ist
	 * 
	 * @author Tobias Glaeser
	 * @param rankables liste mit objekten deren PageRank berechnet werden soll
	 * @return liste mit pageranks, sortiert nach elementen der eingabeliste
	 */
	public ArrayList<Double> recomputePageRanks(ArrayList<PageRankable> rankables){
		// reverse mapping for a->referee relation
		HashMap<PageRankable, ArrayList<PageRankable>> reverseLookup = new HashMap<>();
		for(PageRankable p : rankables) {
			reverseLookup.put(p, new ArrayList<PageRankable>());
		}
		for(PageRankable p : rankables) {
			for(PageRankable r : p.getReferenced()) {
				reverseLookup.get(r).add(p);
			}
		}
		// TODO
		return null;
	}

}

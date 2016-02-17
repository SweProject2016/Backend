package de.hwrberlin.it2014.sweproject.cbr;

import java.util.ArrayList;

/**
 * interface f�r Objekte die mit einem Pagerank versehen werden k�nnen
 * 
 * @author Tobias Glaeser
 *
 */
public interface PageRankable {

	/**
	 * @author Tobias Glaeser
	 * @return pagerank dieses objektes
	 */
	public double getPageRank();

	/**
	 * @author Tobias Glaeser
	 * @return liste mit von diesem objekt referenzierten objekten
	 */
	public ArrayList<PageRankable> getReferenced();

}

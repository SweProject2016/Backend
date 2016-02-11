package de.hwrberlin.it2014.sweproject.cbr;

import java.util.ArrayList;

/**
 * Interface fuer Objekte dessen Ähnlichkeit in CBS berechnet werden kann
 * 
 * @author Tobias Glaeser
 * @since 11.02.2016 09:48:12
 */
public interface Scoreable {

	/**
	 * @author Tobias Glaeser
	 * @since 08.02.2016 11:26:32
	 * @return Liste mit allen Keywords (ungefiltert, lowercase)
	 */
	public ArrayList<String> getKeywordsAsList();

	/**
	 * @author Tobias Glaeser
	 * @since 11.02.2016 09:50:58
	 * @return Zeitstempel in ms
	 */
	public long getTimestamp();

}

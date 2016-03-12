/*
 * ----------------------------------------------------------------------------
 *     (c) by data experts gmbh
 *            Postfach 1130
 *            Woldegker Str. 12
 *            17001 Neubrandenburg
 * ----------------------------------------------------------------------------
 *     Dieses Dokument und die hierin enthaltenen Informationen unterliegen
 *     dem Urheberrecht und duerfen ohne die schriftliche Genehmigung des
 *     Herausgebers weder als ganzes noch in Teilen dupliziert, reproduziert
 *     oder manipuliert werden.
 * ----------------------------------------------------------------------------
 *
 * ----------------------------------------------------------------------------
 */
package de.hwrberlin.it2014.sweproject.cbr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.hwrberlin.it2014.sweproject.cbr.CBR;

public class TestCBR {
    private CBR cbr;
    private String string;
    private String[] ar;
    private ArrayList<String> al;

    @Before
    public void setUp(){
        this.cbr = new CBR();
        this.string = new String();
        this.ar = new String[0];
        this.al = new ArrayList<>();
    }

    @Test
    public void testCBRNotNull_positive(){
        assertNotNull(this.cbr);
    }

    /**
     * testet, ob der Rückgabewert gleich ist,
     * wenn verschiedene Typen von Übergabe-Parametern mit Wert = null übergeben werden.
     *
     *  @author Nils Podlich
     */

    @Test
    public void testStringEqualsStringArray_positive() {
        assertEquals(this.cbr.startCBR(this.string),this.cbr.startCBR(this.ar));
    }

    @Test
    public void testStringEqualsArrayList_positive(){
        assertEquals(this.cbr.startCBR(this.string),this.cbr.startCBR(this.al));
    }

    /**
     * testet, ob startCBR kein leeres Objekt zurückgibt,
     * wenn der Wert der unterstützten Übergabe-Parameter-Typen leer ist.
     *
     * @author Nils Podlich
     *
     */

    @Test
    public void testEmptyStringStartCBRNotNull_positive(){
        assertNotNull(this.cbr.startCBR(this.string));
    }

    @Test
    public void testEmptyStringArrayStartCBRNotNull_positive(){
        assertNotNull(this.cbr.startCBR(this.ar));
    }

    @Test
    public void testEmptyArrayListStartCBRNotNull_positive(){
        assertNotNull(this.cbr.startCBR(this.al));
    }

    /**
     * testet, ob startCBR kein leeres Objekt zurückgibt,
     * wenn der Wert der unterstützten Übergabe-Parameter-Typen nicht leer ist.
     *
     * @author Nils Podlich
     *
     */

    @Test
    public void testFilledStringStartCBRNotNull_positive(){
        this.string = "Anfrage";
        assertNotNull(this.cbr.startCBR(this.string));
    }

    @Test
    public void testFilledStringArrayStartCBRNotNull_positive(){
        this.ar[0] = "Anfrage";
        assertNotNull(this.cbr.startCBR(this.ar));
    }

    @Test
    public void testFilledArrayListStartCBRNotNull_positive(){
        this.al.add("Anfrage");
        assertNotNull(this.cbr.startCBR(this.al));
    }

    @After
    public void tearDown(){
        this.cbr = null;
        this.string = null;
        this.ar = null;
        this.al = null;
        assertNull(this.cbr);
        assertNull(this.string);
        assertNull(this.ar);
        assertNull(this.al);
    }

}

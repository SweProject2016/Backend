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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.hwrberlin.it2014.sweproject.cbr.Request;

public class TestRequest {

    private Request request;
    private ArrayList<String> al;

    @Before
    public void setUp(){
        this.al = new ArrayList<>();
    }

    @Test
    public void testRequestNotNull(){
        this.request = new Request(this.al);
        assertNotNull(this.request);
    }

    @Test
    public void testGetRequestDescriptionString() {
        this.al.add("BlaBla");
        this.request = new Request(this.al);
        assertEquals("BlaBla ",this.request.getDescription());
    }

    @Test
    public void testGetRequestDescriptionArrayList() {
        this.al.add("BlaBla");
        this.request = new Request(this.al);
        assertEquals(this.al,this.request.getDescription(true));
    }

    @Test
    public void testGetRequestDateAsExpected(){
        Date currentDate = new Date();
        this.request = new Request(this.al);
        assertEquals(currentDate,this.request.getDateOfRequest());
    }

    @After
    public void tearDown(){
        this.al = null;
        assertNull(this.al);
    }

}

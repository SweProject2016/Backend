package de.hwrberlin.it2014.sweproject.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.hwrberlin.it2014.sweproject.rest.sample.Generator;

/**
 * Created by csc on 13.01.16. Sample Resource für den REST-WS
 * Aufruf bei installiertem Wildfly:
 * http://localhost:8080/swe/api/sample/get?type=result&size=3&input=Test
 * 
 *  type=result --> bitte nicht verändern
 *  size=3 --> stellt ein, wie viele Ergebnisse zurückgeliefert werden
 *  input=Test --> Usereingabe
 *
 */
@Path("/sample")
public class SampleResource extends Resource {

    /**
     * Testmethode um den REST WS zu prüfen
     * 
     * @param type Der Typ der Testantwort (Result|Committee|Judgement|LawSector)
     * @param size Anzahl der Datensätze
     * @param input Benutzereingabe
     * 
     * @return Response
     */
    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public Response testMethod(@QueryParam("type") String type,
    						   @QueryParam("size") int size,
    						   @QueryParam("input") String input,
    						   @QueryParam("delay") int delay) {
    	Generator generator = new Generator();
    	List<?> output = generator.generate(type, size, input);
    	
    	try{
    		Thread.sleep(delay*1000);
    		if(size>1){
    			return build(Status.OK,output);
    		} else {
    			return build(Status.OK,output.get(0));
    		}
    	    
    	} catch(Exception e){
    		return build(Status.INTERNAL_SERVER_ERROR);
    	}
    
    }
}
package de.hwrberlin.it2014.sweproject.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

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
public class SampleResource {

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
    						   @QueryParam("input") String input) {
    	Generator generator = new Generator();
    	List output = generator.generate(type, size, input);
    	
    	try{
    		if(size>1){
    			return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(output).build();
    		} else {
    			return Response.status(Response.Status.OK).header("Access-Control-Allow-Origin", "*/*").entity(output.get(0)).build();
    		}
    	    
    	} catch(Exception e){
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    	}
    
    }
}
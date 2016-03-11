package de.hwrberlin.it2014.sweproject.rest;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.hwrberlin.it2014.sweproject.database.DatabaseConnection;
import de.hwrberlin.it2014.sweproject.database.TableJudgementSQL;
import de.hwrberlin.it2014.sweproject.model.enums.LawSector;
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
    public Response testMethod(@HeaderParam("X-Api-Key") final String apiKey,
            @HeaderParam("X-Access-Token") final String accessToken,
            @QueryParam("type") final String type,
            @QueryParam("size") final int size,
            @QueryParam("input") final String input,
            @QueryParam("delay") final int delay,
            @QueryParam("start") final int start) {
        try{
            Generator generator = new Generator();
            List<?> output = generator.generate(type, size, input, start);
            final String path="/sample/get";
            if(!auth(apiKey,accessToken)){
                return build(Status.FORBIDDEN);
            } else {
                Thread.sleep(delay*1000);
                if(size>1){
                    return build(Status.OK,output,path);
                } else {
                    return build(Status.OK,output.get(0),path);
                }
            }
        } catch(Exception e){
            return build(Status.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GET
    @Path("/test")
    @Produces(MediaType.APPLICATION_JSON)
    public Response generalTestMethod(@QueryParam("input") String input){
    	try{
    		ArrayList<String> keywords = new ArrayList<>();
    		keywords.add("offence");
    		keywords.add("sentence");
    		DatabaseConnection dbc = new DatabaseConnection();
    		TableJudgementSQL.prepareSelect(keywords, LawSector.STRAFRECHT, dbc);
    		return build(Status.OK);
    	} catch(Exception e){
    		e.printStackTrace();
    		return build(Status.INTERNAL_SERVER_ERROR);
    	}
    }
}
package de.hwrberlin.it2014.sweproject.rest;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.hwrberlin.it2014.sweproject.cbr.CBR;
import de.hwrberlin.it2014.sweproject.model.Result;
import de.hwrberlin.it2014.sweproject.rest.sample.Generator;

/**
 * Produktive REST Resource, die auf Basis der Nutzereingabe eine Liste an Results
 * zurückgibt
 * 
 * @author Christian Schlesing
 *
 */
@Path("/result")
public class ResultResource extends Resource {

	/**
	 * REST-Methode, die passende Results zu einem Suchterm liefert
	 * 
	 * @param apiKey API-Key aus dem HTTP-Request-Header
	 * @param accessToken Access Token aus dem HTTP-Request-Header
	 * @param input Suchterm
	 * @param size Gr&ouml;&szlig;e der zur&uuml;ckegegebenen Liste
	 * @param startIndex Start-Index f&uuml;r die Liste, wird ben&ouml;tigt vom Frontend
	 * @return Response mit HTTP-Code 200 bei Erfolg, 500 bei Fehler, 403 bei falscher Authentifizierung
	 */
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getResults(@HeaderParam("X-Api-Key") String apiKey,
			   				   @HeaderParam("X-Access-Token") String accessToken,
			   				   @QueryParam("input") String input,
			   				   @QueryParam("size") int size,
			   				   @QueryParam("startindex") int startIndex){
		try{
			final String path = "/result/get";
			if(!auth(apiKey,accessToken)){
				return build(Status.FORBIDDEN);
			} else {
				CBR cbr = new CBR();
				ArrayList<Result> resultList = cbr.startCBR(input);
				if(resultList.isEmpty()){
					return build(Status.OK,resultList,path);
				} else {
					Generator gen = new Generator();
					resultList = gen.setSampleSim(resultList, startIndex);
					
					try {
						return build(Status.OK,resultList.subList(startIndex, startIndex+size),path);
					} catch(IndexOutOfBoundsException e){
						return build(Status.OK,resultList.subList(startIndex, resultList.size()),path);
					}
				}
			} 
		} catch(Exception e){
			e.printStackTrace();
			return build(Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * REST-Methode, um ein Result zu bewerten
	 * 
	 * @param apiKey API-Key aus dem HTTP-Request-Header
	 * @param accessToken Access Token aus dem HTTP-Request-Header
	 * @param id ID des Results, welches bewertet werden soll
	 * @param rating Bewertung des Nutzers
	 * @return Response mit HTTP-Code 201 bei Erfolg, 500 bei Fehler, 403 bei falscher Authentifizierung
	 */
	@POST
	@Path("rate")
	public Response rateResult(@HeaderParam("X-Api-Key") String apiKey,
			   				   @HeaderParam("X-Access-Token") String accessToken,
			   				   @QueryParam("id") int id,
							   @QueryParam("rating") float rating
							   ){
		try{
			if(!auth(apiKey,accessToken)){
				return build(Status.FORBIDDEN);
			} else {
				CBR cbr = new CBR();
				cbr.saveRating(id,rating);
				return build(Status.CREATED);
			}
		} catch(Exception e){
			e.printStackTrace();
			return build(Status.INTERNAL_SERVER_ERROR);
		}
		
		
	}
	
}

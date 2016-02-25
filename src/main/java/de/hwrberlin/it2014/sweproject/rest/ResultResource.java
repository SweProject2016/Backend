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

/**
 * Produktive REST Resource, die auf Basis der Nutzereingabe eine Liste an Results
 * zurückgibt
 * 
 * @author csc
 *
 */
@Path("/result")
public class ResultResource extends Resource {

	/**
	 * REST Methode
	 * 
	 * @param input String Nutzereingabe
	 * @return Response
	 */
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getResults(@HeaderParam("X-Api-Key") String apiKey,
			   				   @HeaderParam("X-Access-Token") String accessToken,
			   				   @QueryParam("input") String input){
		try{
			if(!auth(apiKey,accessToken)){
				return build(Status.FORBIDDEN);
			} else {
				CBR cbr = new CBR();
				ArrayList<Result> resultList = cbr.startCBR(input);
				return build(Status.OK,resultList);
			} 
		} catch(Exception e){
			e.printStackTrace();
			return build(Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	@POST
	@Path("rate")
	public Response rateResult(@HeaderParam("X-Api-Key") String apiKey,
			   				   @HeaderParam("X-Access-Token") String accessToken,
			   				   @QueryParam("id") int id,
							   @QueryParam("rating") float rating,
							   @QueryParam("delay") int delay){
		//Sample Verarbeitung
		try{
			if(!auth(apiKey,accessToken)){
				return build(Status.FORBIDDEN);
			} else {
				String str = null;
				if(id>0){
					Thread.sleep(delay*1000);
					str = id + " - " + rating;
					System.out.println(str);
					return build(Status.NO_CONTENT);
				} else {
					return build(Status.NOT_FOUND);
				}
			}
		} catch(Exception e){
			e.printStackTrace();
			return build(Status.INTERNAL_SERVER_ERROR);
		}
		
		
	}
	
}

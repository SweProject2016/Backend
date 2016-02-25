package de.hwrberlin.it2014.sweproject.rest;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
	public Response getResults(@QueryParam("input") String input){
		try{
			CBR cbr = new CBR();
			ArrayList<Result> resultList = cbr.startCBR(input);
			return build(Status.OK,resultList);
		} catch(Exception e){
			e.printStackTrace();
			return build(Status.INTERNAL_SERVER_ERROR);
		}
	}
	
}

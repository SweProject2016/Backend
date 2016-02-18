package de.hwrberlin.it2014.sweproject.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response;
import org.json.simple.JSONObject;

@Path("/status")
public class StatusResource extends Resource{
	
	public StatusResource(){
		super();
	}
	
	@GET
	@Path("/server")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getServerStatus(){
		try{
			JSONObject json = new JSONObject();
			json.put("server", "running");
			return build(Status.OK, json);
		} catch(Exception e){
			e.printStackTrace();
			return build(Status.INTERNAL_SERVER_ERROR);
		}
	}
}
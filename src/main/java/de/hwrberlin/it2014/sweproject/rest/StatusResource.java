package de.hwrberlin.it2014.sweproject.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response;

import de.hwrberlin.it2014.sweproject.model.Status.ServerStatus;

@Path("/")
public class StatusResource extends Resource{
	
	@GET
	@Path("status")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getServerStatus(){
		try{
			de.hwrberlin.it2014.sweproject.model.Status status = new de.hwrberlin.it2014.sweproject.model.Status();
			status.setDatabaseStatus(ServerStatus.RUNNING);
			status.setServer(ServerStatus.RUNNING);
			return build(Status.OK,status);
		} catch(Exception e){
			e.printStackTrace();
			return build(Status.INTERNAL_SERVER_ERROR);
		}
	}
}
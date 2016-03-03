package de.hwrberlin.it2014.sweproject.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.hwrberlin.it2014.sweproject.convert.Import;

@Path("/")
public class ImportResource extends Resource {

	@GET
	@Path("/import")
	public Response startImport(@QueryParam("path") String path){
		try{
			Import im = new Import();
			im.importCases(path);
			return build(Status.OK);
		} catch(Exception e){
			e.printStackTrace();
			return build(Status.INTERNAL_SERVER_ERROR);
		}
	}
	
}

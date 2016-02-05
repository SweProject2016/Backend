package de.hwrberlin.it2014.sweproject.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by csc on 13.01.16. Test Resource für REST WS
 */
@Path("/helloworld")
public class HelloWorldResource {

    /**
     * Testmethode um den REST WS zu prüfen
     * 
     * @return Response
     */
    @GET
    @Path("/test")
    @Produces(MediaType.APPLICATION_JSON)
    public Response testMethod() {
        return Response.status(Response.Status.OK).entity("Hallo, Welt!").build();
    }
}
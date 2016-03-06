package de.hwrberlin.it2014.sweproject.rest;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

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
    public Response startImport(@QueryParam("path") final String path) {
        try {
            Import.importCases(Paths.get(path));
            return build(Status.OK);
        } catch (InvalidPathException e) {
            return build(Status.BAD_REQUEST);
        }
    }
}

package de.hwrberlin.it2014.sweproject.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public abstract class Resource {
	
	private final static String ALLOW_ORIGIN_HEADER="Access-Control-Allow-Origin";
	private final static String WILDCARD="*";
	
	protected Response build(Status status, Object entity){
		return Response.status(status)
				.header(ALLOW_ORIGIN_HEADER,WILDCARD)
				.entity(entity)
				.build();
	}
	
	protected Response build(Response.Status status){
		return Response
				.status(status)
				.header(ALLOW_ORIGIN_HEADER,WILDCARD)
				.build();
	}
	
}

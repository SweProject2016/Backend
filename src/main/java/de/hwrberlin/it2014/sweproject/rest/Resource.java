package de.hwrberlin.it2014.sweproject.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public abstract class Resource {
	
	private final static String ALLOW_ORIGIN_HEADER="Access-Control-Allow-Origin";
	private final static String WILDCARD="*";
	private final static String API_KEY="$A$9af4d8381781baccb0f915e554f8798d";
	private final static String ACCESS_TOKEN = "$T$de61425667e2e4ac0884808b769cd042";
	
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
	
	protected boolean auth(String apiKey, String accessToken){
		if(apiKey.equals(API_KEY) && accessToken.equals(ACCESS_TOKEN)){
			return true;
		} else{
			return false;
		}
	}
	
}

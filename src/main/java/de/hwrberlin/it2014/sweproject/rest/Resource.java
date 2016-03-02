package de.hwrberlin.it2014.sweproject.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.hwrberlin.it2014.sweproject.model.Entity;

public abstract class Resource {
	
	private final static String API_KEY="$A$9af4d8381781baccb0f915e554f8798d";
	private final static String ACCESS_TOKEN = "$T$de61425667e2e4ac0884808b769cd042";
	
	protected Response build(Status status, Object entity, String path){
		return Response.status(status)
				.entity(createEntity(path, status, entity))
				.build();
	}
	
	protected Response build(Response.Status status){
		return Response
				.status(status)
				.build();
	}
	
	protected boolean auth(String apiKey, String accessToken){
		if(apiKey == null || accessToken == null) return false;
		apiKey=normalize(apiKey);
		accessToken=normalize(accessToken);
		if(apiKey.equals(API_KEY) && accessToken.equals(ACCESS_TOKEN)){
			return true;
		} else{
			return false;
		}
	}
	
	private Entity createEntity(String path, Status status, Object entity){
		Entity theEntity = new Entity(path,
									  status.getStatusCode(),
									  entity);
		return theEntity;
	}
	
	private String normalize(String str){
		if(str.startsWith("\"")){
			str = str.substring(1, str.length());
		}
		if(str.endsWith("\"")) {
			str = str.substring(0,str.length()-1);
		}
		return str;
	}
	
}

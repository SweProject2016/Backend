package de.hwrberlin.it2014.sweproject.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.hwrberlin.it2014.sweproject.model.Entity;

/**
 *
 * Abstrakte Vaterklasse fuer alle REST Resourcen
 *
 * @author Christian Schlesing
 *
 */
public abstract class Resource {

    private final static String API_KEY="$A$9af4d8381781baccb0f915e554f8798d";
    private final static String ACCESS_TOKEN = "$T$de61425667e2e4ac0884808b769cd042";

    /**
     * Methode, um einheitliche Response-Objekte zu erzeugen
     * 
     * @param status der HTTP-Antwort-Status
     * @param entity die eigentliche Antwort
     * @param path der aufgerufene API-Pfad
     * @return Response Objekt
     */
    protected Response build(final Status status, final Object entity, final String path){
        return Response.status(status)
                .entity(createEntity(path, status, entity))
                .build();
    }

    /**
     * Methode, um einheitliche Response-Objekte ohne Response-Body zu erzeugen
     * 
     * @param status der HTTP-Status-Code
     * @return Response Objekt
     */
    protected Response build(final Response.Status status){
        return Response
                .status(status)
                .build();
    }
    
    /**
     * Methode zur Basis-Authentifizierung API-Anfrage
     * 
     * @param apiKey der API-Key aus dem HTTP-Request-Header
     * @param accessToken der Access Token aus dem HTTP-Request-Header
     * @return @code true, bei erfolgreicher Authentifizierung. @code false, bei Fehlern.
     */
    protected boolean auth(String apiKey, String accessToken){
        if(apiKey == null || accessToken == null) {
            return false;
        }
        apiKey=normalize(apiKey);
        accessToken=normalize(accessToken);
        if(apiKey.equals(Resource.API_KEY) && accessToken.equals(Resource.ACCESS_TOKEN)){
            return true;
        } else{
            return false;
        }
    }

    /**
     * Erstellt das entsprechende Body-Objekt für eine Ressource
     * 
     * @param path Pfad des API-Aufrufs
     * @param status HTTP-Status der Response
     * @param entity die zu übermittelnden Daten
     * 
     * @return Entity
     */
    private Entity createEntity(final String path, final Status status, final Object entity){
        Entity theEntity = new Entity(path,
                status.getStatusCode(),
                entity);
        return theEntity;
    }

    /**
     * Normalisiert die eingegebenen Header-Parametern und löscht Anfuehrungszeichen vorn und hinten
     * 
     * @param str
     *
     * @return String
     */
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

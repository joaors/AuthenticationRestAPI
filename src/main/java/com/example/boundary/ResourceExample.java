package com.example.boundary;

import com.example.authentication.Autenticator;
import com.example.authentication.Headers;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.security.auth.login.LoginException;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author joao.rodrigo
 */
@Path(value = "example")
@Produces( MediaType.APPLICATION_JSON )
public class ResourceExample implements Serializable {
    
    
    
    @POST
    @Path( "login" )    
    public Response login(@Context HttpHeaders httpHeaders,
        @FormParam( "user" ) String user,
        @FormParam( "password" ) String password ) {

        Autenticator autenticator = Autenticator.getInstance();
        String serviceKey = httpHeaders.getHeaderString( Headers.KEY );
        try {
            String authToken = autenticator.login(user, password, serviceKey);
            JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
            jsonObjBuilder.add( "token", authToken );
            JsonObject jsonObj = jsonObjBuilder.build();
            return Response.ok(jsonObj.toString()).build();

        } catch ( final LoginException ex ) {
            JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
            jsonObjBuilder.add( "message", "Problem matching key, user and password" );
            JsonObject jsonObj = jsonObjBuilder.build();

            return Response
                    .status(Status.UNAUTHORIZED)
                    .entity( jsonObj.toString() )
                    .build();
        }
    }

    @GET
    @Path("get")
    public Response get() {
        JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
        jsonObjBuilder.add( "message", "Get executed!" );
        JsonObject jsonObj = jsonObjBuilder.build();

        return Response.ok(jsonObj.toString()).build();
    }
    
    @POST
    @Path("logout")
    public Response logout(@Context HttpHeaders httpHeaders ) {
        try {
            Autenticator demoAuthenticator = Autenticator.getInstance();
            String serviceKey = httpHeaders.getHeaderString( Headers.KEY );
            String authToken = httpHeaders.getHeaderString( Headers.TOKEN );
            demoAuthenticator.logout( serviceKey, authToken );
            return Response.ok().build();
        } catch ( GeneralSecurityException ex ) {
            return Response
                    .status(Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }   
    
}

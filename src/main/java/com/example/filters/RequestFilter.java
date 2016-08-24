package com.example.filters;

import com.example.autentication.Autenticator;
import com.example.autentication.Headers;
import java.io.IOException;
import java.util.logging.Logger;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author joao.rodrigo
 */
@Provider
@PreMatching
public class RequestFilter implements ContainerRequestFilter{
    
    private final static Logger LOG = Logger.getLogger( RequestFilter.class.getName() );

    @Override
    public void filter( ContainerRequestContext requestCtx ) throws IOException {

        String path = requestCtx.getUriInfo().getPath();
        LOG.info( "Filtering request path: " + path );

        // IMPORTANT!!! First, OPTIONS
        if ( requestCtx.getRequest().getMethod().equals( "OPTIONS" ) ) {
            requestCtx.abortWith(Response.status( Response.Status.OK ).build() );
            return;
        }

        Autenticator autenticator = Autenticator.getInstance();
        String serviceKey = requestCtx.getHeaderString( Headers.KEY );

        if ( !autenticator.isKeyValid(serviceKey) ) {
            requestCtx.abortWith( Response.status( Response.Status.UNAUTHORIZED ).build() );
            return;
        }

        if ( !path.startsWith( "example/login" ) ) {
            String authToken = requestCtx.getHeaderString( Headers.TOKEN );

            if ( !autenticator.isTokenValid( serviceKey, authToken ) ) {
                requestCtx.abortWith( Response.status( Response.Status.UNAUTHORIZED ).build() );
            }
        }
    }    
    
}

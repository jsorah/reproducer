package com.example;
import org.jboss.logging.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

@Path("/hello")
public class ExampleResource {
    protected static final Logger logger = Logger.getLogger(ExampleResource.class);

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello RESTEasy";
    }

    @GET
    @Path("/res/{path}")
    @Produces("text/html; charset=utf-8")
    public Response getResource(@PathParam("path") String path) {
        try {
            logger.info("Path: " + path);
            InputStream resource = this.getClass().getClassLoader().getResourceAsStream("/keycloak/"+path);
            if (resource != null) {
                String contentType = "text/css";
                Response.ResponseBuilder builder = Response.ok(resource).type(contentType);
                return builder.build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @GET
    @Path("/fixed/{path}")
    @Produces("text/html; charset=utf-8")
    public Response getFixedResource(@PathParam("path") String path) {
        try {
            logger.info("Path: " + path);
            InputStream resource = this.getClass().getClassLoader().getResourceAsStream("/keycloak/"+path);

            if (resource != null) {
                String contentType = "text/css";
                Response.ResponseBuilder builder = Response.ok(resource.readAllBytes()).type(contentType);
                return builder.build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}
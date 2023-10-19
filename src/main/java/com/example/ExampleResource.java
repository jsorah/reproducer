package com.example;
import org.jboss.logging.Logger;


import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.jboss.resteasy.reactive.server.core.CurrentRequestManager;
import org.jboss.resteasy.reactive.server.core.ResteasyReactiveRequestContext;
import org.jboss.resteasy.reactive.server.handlers.FormBodyHandler;
import org.jboss.resteasy.reactive.server.multipart.FormValue;
import org.jboss.resteasy.reactive.server.spi.DefaultRuntimeConfiguration;

import java.nio.charset.Charset;
import java.time.Duration;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

@Path("/hello")
public class ExampleResource {
    protected static final Logger logger = Logger.getLogger(ExampleResource.class);


    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello RESTEasy";
    }

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @POST
    public Response processPostFormRequest() throws Exception {
        ResteasyReactiveRequestContext resteasyReactiveRequestContext = CurrentRequestManager.get();

        //Taken from Keycloak code base.
        FormBodyHandler fbh = new FormBodyHandler(true, () -> null, Set.of());
        fbh.configure(new DefaultRuntimeConfiguration(Duration.ofMillis(3000), true, "tmp", List.of("txt"), Charset.defaultCharset(), Optional.of(Long.valueOf(300l)), 300l));        fbh.handle(resteasyReactiveRequestContext);

        Deque<FormValue> a = resteasyReactiveRequestContext.getFormData().get("a");
        logger.info("Writing byte info");
        for (byte aByte : a.getFirst().getValue().getBytes(Charset.defaultCharset())) {
            logger.info(aByte);

            if(aByte == 0) {
                return Response.ok(":-( 0x00 bytes found", MediaType.APPLICATION_JSON_TYPE).build();
            }
        }
        return Response.ok(":-) No 0x00 bytes!" , MediaType.APPLICATION_JSON_TYPE).build();
    }
}
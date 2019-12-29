package com.jcrochavera.messages.boundary;

import com.jcrochavera.messages.control.GreetingService;
import com.jcrochavera.messages.entity.Message;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by julio.rocha on 28/12/19.
 *
 * @author julio.rocha
 */
@Path("/greeting")
public class GreetingResource {
    @Inject
    GreetingService service;

    @Inject
    @ConfigProperty(name = "message")
    String configMessage;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String ping() {
        return this.configMessage;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/say-hello")
    public Response sayHello() {
        Message greeting = service.distributedHello();
        return Response.ok(greeting).build();
    }
}

package com.jcrochavera.messages.boundary;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.Bulkhead;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.IntStream;

/**
 * Created by julio.rocha on 28/12/19.
 *
 * @author julio.rocha
 */
@Path("/greeting")
public class GreetingResource {
    @Context
    HttpHeaders headers;

    @Inject
    @ConfigProperty(name = "message.key")
    String messageKey;

    @POST
    @Path("/say-hello")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Bulkhead(value = 1, waitingTaskQueue = 1)
    //public String greeting(JsonObject serviceKey) {
    public CompletionStage<String> greeting(JsonObject serviceKey) {
        int processTime = process();
        String key = serviceKey.getString("key", "-");
        String message;
        if (messageKey.equals(key)) {
            message = "Greetings from ServiceB!!";
        } else {
            message = "You need to pay a license to call me $$$$$";
        }
        message += " [processTime = " + processTime + "]";
        StringBuilder allHeaders = new StringBuilder();
        headers.getRequestHeaders().forEach((k, v) -> allHeaders.append("\n").append(k).append(" = ").append(v));
        System.out.println("********************************************");
        System.out.println("\nHeaders: " + allHeaders);
        System.out.println("********************************************");
        return CompletableFuture.completedStage(message);
    }

    private int process() {
        IntStream ints = new Random().ints(1, 50);
        int processTime = ints.findAny().orElse(0) * 100;
        try {
            Thread.sleep(processTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return processTime;
    }
}

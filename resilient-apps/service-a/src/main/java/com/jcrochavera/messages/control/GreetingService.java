package com.jcrochavera.messages.control;

import com.jcrochavera.messages.entity.Message;
import com.jcrochavera.messages.entity.ServiceKey;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.time.temporal.ChronoUnit;

/**
 * Created by julio.rocha on 28/12/19.
 *
 * @author julio.rocha
 */
@ApplicationScoped
public class GreetingService {
    @Inject
    @ConfigProperty(name = "serviceB.url", defaultValue = "http://service-b:8080/service-b/resources/greeting/say-hello")
    String sbBaseUrl;

    Client client;
    WebTarget target;

    @PostConstruct
    void init() {
        client = ClientBuilder.newClient();
        target = client.target(sbBaseUrl);
    }

    //@Retry(retryOn = Exception.class, maxRetries = 10, delay = 1L, delayUnit = ChronoUnit.SECONDS)
    @CircuitBreaker(successThreshold = 2, requestVolumeThreshold = 10, failureRatio = 0.90, delay = 1, delayUnit = ChronoUnit.SECONDS)
    @Fallback(fallbackMethod = "serviceBDown")
    public Message distributedHello() {
        Message g = new Message();
        g.main = "Hello from main host";
        ServiceKey serviceKey = new ServiceKey();
        serviceKey.key = "@BolivianJUG";
        g.serviceB = getGreeting(serviceKey);
        return g;
    }

    public String getGreeting(ServiceKey serviceKey) {
        String payload = JsonbBuilder.create().toJson(serviceKey);
        Response response = sendRequest(payload);
        return response.readEntity(String.class);
    }

    public Message serviceBDown() {
        Message g = new Message();
        g.main = "It's just me, the other guy is down";
        g.serviceB = "I'm not available";
        return g;
    }

    private Response sendRequest(String requestBody) {
        try {
            return target.request().post(Entity.json(requestBody));
        } catch (Exception e) {
            throw new IllegalStateException("Could not start request, reason: " + e.getMessage(), e);
        }
    }
}

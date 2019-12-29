package com.jcrochavera.messages;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;

import javax.enterprise.context.ApplicationScoped;

/**
 * Created by julio.rocha on 28/12/19.
 *
 * @author julio.rocha
 */
@Readiness
@ApplicationScoped
public class ReadyResource implements HealthCheck {

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder health = HealthCheckResponse.named("resilient-apps");
        if (isKafkaAlive()) {
            health.up();
        } else {
            health.down();
        }

        return health
                .build();
    }

    boolean isKafkaAlive() {
        return true;
    }
}


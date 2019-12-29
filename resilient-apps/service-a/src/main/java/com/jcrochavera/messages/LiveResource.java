package com.jcrochavera.messages;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

import javax.enterprise.context.ApplicationScoped;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

/**
 * Created by julio.rocha on 28/12/19.
 *
 * @author julio.rocha
 */
@Liveness
@ApplicationScoped
public class LiveResource implements HealthCheck {
    @Override
    public HealthCheckResponse call() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        long memUsed = memoryMXBean.getHeapMemoryUsage().getUsed();
        long memMax = memoryMXBean.getHeapMemoryUsage().getMax();
        return HealthCheckResponse.named("resilient-intro-liveness")
                .withData("memory_used", memUsed)
                .withData("memory_max", memMax)
                .state(memUsed < memMax * 0.80)
                //.state(memUsed < memMax * 0.001)
                .build();
    }
}


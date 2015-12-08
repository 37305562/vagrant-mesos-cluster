package name.krestjaninoff.mesos.fibonacci.service.balancing;

import org.apache.commons.lang3.tuple.Pair;

/**
 * A client-based load balancer
 */
public interface LoadBalancer {

    /**
     * Get random instance of a specific service
     */
    Pair<String, Integer> getInstance(String serviceName);
}

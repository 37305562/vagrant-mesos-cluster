package name.krestjaninoff.mesos.fibonacci.service.discovery;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * A service discovery client
 */
public interface DiscoveryService {

    /**
     * Get a list of [ip, port] pairs for a specific service
     */
    List<Pair<String, Integer>> getServiceInstances(String serviceName);
}

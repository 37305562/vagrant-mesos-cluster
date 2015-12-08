package name.krestjaninoff.mesos.fibonacci.service.balancing;

import name.krestjaninoff.mesos.fibonacci.service.discovery.DiscoveryService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * Random load balancer
 */
@Service
public class RandomLoadBalancer implements LoadBalancer {

    private Random randomGenerator = new Random();

    @Autowired
    private DiscoveryService discoveryService;

    /**
     * {@inheritDoc}
     */
    public Pair<String, Integer> getInstance(String serviceName) {
        Pair<String, Integer> instance = null;

        List<Pair<String, Integer>> instances = discoveryService.getServiceInstances(serviceName);
        if (instances.size() > 0) {
            int index = randomGenerator.nextInt(instances.size());
            instance = instances.get(index);
        }

        return instance;
    }
}

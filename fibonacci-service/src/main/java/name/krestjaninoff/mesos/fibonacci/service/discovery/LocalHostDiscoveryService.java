package name.krestjaninoff.mesos.fibonacci.service.discovery;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * A simple stub which always returns "localhost:port"
 */
@Service
@ConditionalOnExpression("!${dns.enabled}")
public class LocalHostDiscoveryService implements DiscoveryService {

    @Value("${server.port}")
    private Integer port;

    @Override
    public List<Pair<String, Integer>> getServiceInstances(String serviceName) {
        return Arrays.asList(new ImmutablePair<>("localhost", port));
    }
}

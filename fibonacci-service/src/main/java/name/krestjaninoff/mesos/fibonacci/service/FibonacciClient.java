package name.krestjaninoff.mesos.fibonacci.service;

import name.krestjaninoff.mesos.fibonacci.service.balancing.LoadBalancer;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.AsyncRestTemplate;

import javax.annotation.PostConstruct;
import java.util.concurrent.Future;

/**
 * A simple HTTP client to invoke another app's instances
 */
@Service
public class FibonacciClient {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(FibonacciClient.class);

    @Value("${service.name}")
    private String serviceName = "fibonacci";

    @Autowired
    private LoadBalancer loadBalancer;

    private AsyncRestTemplate restTemplate;


    @PostConstruct
    public void init() {
        this.restTemplate = new AsyncRestTemplate();
    }

    public Future<ResponseEntity<Integer>> invoke(Integer n) {

        Pair<String, Integer> instance = loadBalancer.getInstance(serviceName);
        String url = "http://" + instance.getLeft() + ":" + instance.getRight() + "/" + n;

        LOG.info("Invoking Fibonacci calculation: " + url);
        Future<ResponseEntity<Integer>> result = restTemplate.getForEntity(url, Integer.class);

        return result;
    }
}

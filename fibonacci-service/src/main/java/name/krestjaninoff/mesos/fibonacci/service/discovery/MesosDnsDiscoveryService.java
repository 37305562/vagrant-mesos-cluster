package name.krestjaninoff.mesos.fibonacci.service.discovery;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * A service discovery based on MesosDns HTTP interfaces
 *
 * @link https://mesosphere.github.io/mesos-dns/docs/http.html
 */
@Service
@ConditionalOnExpression("${dns.enabled:true}")
public class MesosDnsDiscoveryService implements DiscoveryService {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(MesosDnsDiscoveryService.class);

    @Value("${dns.host}")
    private String host;

    @Value("${dns.port:8123}")
    private String port;

    private String url;
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier("dnsClientHttpRequestFactory")
    private SimpleClientHttpRequestFactory dnsClientHttpRequestFactory;

    @PostConstruct
    public void init() {
        this.url = "http://" + host + ":" + port + "/v1/services/";
        this.restTemplate = new RestTemplate(dnsClientHttpRequestFactory);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Pair<String, Integer>> getServiceInstances(String serviceName) {
        List<Pair<String, Integer>> instances = new ArrayList<>();

        try {
            String srvName = "_" + serviceName + "._tcp.marathon.mesos.";
            String request = url + srvName;

            LOG.debug("DNS request: {}", request);
            String response = restTemplate.getForObject(request, String.class);
            LOG.debug("DNS Response: {}", response);

            JSONArray jsonServices = new JSONArray(response);

            if (0 == jsonServices.length()) {
                throw new DiscoveryServiceException("Service " + serviceName + " has no instances");
            }

            for (int i = 0; i < jsonServices.length(); i++) {

                String ip = jsonServices.getJSONObject(i).getString("ip");
                Integer port = Integer.valueOf(jsonServices.getJSONObject(i).getString("port"));

                Pair<String, Integer> instance = new ImmutablePair<>(ip, port);
                instances.add(instance);
            }

        } catch (DiscoveryServiceException e) {
            throw e;

        } catch (Exception e) {
            throw new DiscoveryServiceException("Service " + serviceName + " can't be resolved", e);
        }

        return instances;
    }
}

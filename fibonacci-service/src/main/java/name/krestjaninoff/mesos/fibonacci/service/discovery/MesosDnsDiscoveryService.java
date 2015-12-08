package name.krestjaninoff.mesos.fibonacci.service.discovery;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONArray;
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
public class MesosDnsDiscoveryService implements DiscoveryService {

    @Value("${dns.host}")
    private String host;

    @Value("${dns.port}")
    private String port;

    private String url;
    private RestTemplate restTemplate;

    @PostConstruct
    public void init() {
        this.url = "http://" + host + ":" + port + "/v1/services/";
        this.restTemplate = new RestTemplate();
    }

    /**
     * {@inheritDoc}
     */
    public List<Pair<String, Integer>> getServiceInstances(String serviceName) {
        List<Pair<String, Integer>> instances = new ArrayList<>();

        try {
            String srvName = "_" + serviceName + "._tcp.marathon.mesos.";
            String response = restTemplate.getForObject(url + srvName, String.class);

            JSONArray jsonServices = new JSONArray(response);

            if (0 == jsonServices.length()) {
                throw new DiscoveryServiceException("Service " + serviceName + " has no instances");
            }

            for (int i = 0; i < jsonServices.length(); i++) {

                String ip = jsonServices.getJSONObject(i).getString("ip");
                Integer port = jsonServices.getJSONObject(i).getInt("port");

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

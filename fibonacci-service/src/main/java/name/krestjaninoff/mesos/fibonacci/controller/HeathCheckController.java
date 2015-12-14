package name.krestjaninoff.mesos.fibonacci.controller;

import name.krestjaninoff.mesos.fibonacci.service.discovery.DiscoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Health check endpoint
 */
@RestController
public class HeathCheckController {

    @Value("${srv.name}")
    private String serviceName = "fibonacci-service";

    @Autowired
    private DiscoveryService discoveryService;

    @RequestMapping(value = "/healthcheck/", method = RequestMethod.GET)
    @ResponseBody
    public String check() {
        discoveryService.getServiceInstances(serviceName);
        return "OK";
    }
}

package name.krestjaninoff.mesos.fibonacci.controller;

import name.krestjaninoff.mesos.fibonacci.service.FibonacciClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Fibonacci controller
 *
 * Calculates N Fibonacci number delegating the process to another instances
 * according to the formula: F(N) = F(N-1) + F(N-2)
 */
@RestController
public class FibonacciController {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(FibonacciController.class);

    @Autowired
    private FibonacciClient client;

    @RequestMapping(value = "/{n}", method = RequestMethod.GET)
    @ResponseBody
    public Integer calculate(@PathVariable("n") Integer n) throws ExecutionException, InterruptedException {
        LOG.info("Invoked Fibonacci calculation for " + n);

        Integer value;
        if (n < 0) {
            throw new IllegalArgumentException("N can't be less than 0");
        }

        if (n <= 1) {
            value = n;

        } else {
            // Invoke both calculations in parallel to double the computation speed
            // (yes, this solution has huge overhead, but this example is about distributed systems, not effective calculations)
            Future<ResponseEntity<Integer>> n1Future = client.invoke(n - 1);
            Future<ResponseEntity<Integer>> n2Future = client.invoke(n - 2);

            Integer n1 = n1Future.get().getBody();
            LOG.debug("Calculated N1");

            Integer n2 = n2Future.get().getBody();
            LOG.debug("Calculated N2");

            value = n1 + n2;
        }

        LOG.info("Calculation for " + n + " completed: " + value);
        return value;
    }
}

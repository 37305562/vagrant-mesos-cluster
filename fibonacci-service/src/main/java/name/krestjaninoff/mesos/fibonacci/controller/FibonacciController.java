package name.krestjaninoff.mesos.fibonacci.controller;

import org.springframework.web.bind.annotation.*;

/**
 * Fibonacci controller
 *
 * Calculates N Fibonacci number delegating the process to another instances
 * according to the formula: F(N) = F(N-1) + F(N-2)
 */
@RestController
public class FibonacciController {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(FibonacciController.class);

    @RequestMapping(value = "/{n}", method = RequestMethod.GET)
    @ResponseBody
    public Integer calculate(@PathVariable("n") Integer n) {

        Integer value;
        if (n < 0) {
            throw new IllegalArgumentException("N can't be less than 0");
        }

        if (n <= 1) {
            value = n;

        } else {
            // TODO Add service discovery and client-based load balancing
            // TODO Async invocation

            value = calculate(n - 1) + calculate(n - 2);
        }

        return value;
    }
}

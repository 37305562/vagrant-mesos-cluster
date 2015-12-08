package name.krestjaninoff.mesos.fibonacci.service.discovery;

/**
 * DiscoveryService exception
 */
public class DiscoveryServiceException extends RuntimeException {

    public DiscoveryServiceException() {
    }

    public DiscoveryServiceException(String message) {
        super(message);
    }

    public DiscoveryServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

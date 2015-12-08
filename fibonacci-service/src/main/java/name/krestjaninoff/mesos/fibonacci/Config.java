package name.krestjaninoff.mesos.fibonacci;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Configuration
 */
@Configuration
public class Config {

    @Bean
    public SimpleClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();

        // Set timeouts (1 second to connect, unlimited - for response)
        clientHttpRequestFactory.setConnectTimeout(1000);
        clientHttpRequestFactory.setReadTimeout(0);

        // Setting the ThreadPoolTaskExecutor for the Async calls
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(5);
        pool.setMaxPoolSize(10);
        pool.setWaitForTasksToCompleteOnShutdown(true);
        pool.initialize();

        // Setting the TaskExecutor to the ThreadPoolTaskExecutor
        clientHttpRequestFactory.setTaskExecutor(pool);

        return clientHttpRequestFactory;
    }
}

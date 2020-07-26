package dev.drugowick.distributedtracingdemo.serviceone;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClient;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryProperties;
import org.springframework.cloud.client.discovery.simple.reactive.SimpleReactiveDiscoveryProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerRequest;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class ServiceOneApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceOneApplication.class, args);
    }

}

@RestController @RequestMapping("info")
@RequiredArgsConstructor @Slf4j
class MyController {

    private final ServiceClient client;

    @GetMapping
    AllServicesInfo getInfoFromAllServices() {
        Map<String, String> allServicesInfoMap = new HashMap<>();
        allServicesInfoMap.put("Service 1", "ok");

        log.info("returning ok from " + "Service One");

        AllServicesInfo infoFromChildServices = client.getInfo();
        infoFromChildServices.getInfo().forEach(allServicesInfoMap::put);

        return new AllServicesInfo(allServicesInfoMap);
    }
}

@Getter @NoArgsConstructor @AllArgsConstructor
class AllServicesInfo {
    private Map<String, String> info = new HashMap<>();
}

@FeignClient("layer-2-services")
interface ServiceClient {

    @GetMapping(value = "/info", consumes = MediaType.APPLICATION_JSON_VALUE)
    AllServicesInfo getInfo();
}

@Configuration
class LoadBalancerConfig {
    
    @Bean
    SimpleDiscoveryClient discoveryClient() {
        List<SimpleDiscoveryProperties.SimpleServiceInstance> instances = null;
        try {
            instances = List.of(
                    new SimpleDiscoveryProperties.SimpleServiceInstance(new URI("http://localhost:8081")),
                    new SimpleDiscoveryProperties.SimpleServiceInstance(new URI("http://localhost:8082"))
            );
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        Map<String, List<SimpleDiscoveryProperties.SimpleServiceInstance>> service = new HashMap<>();
        service.put("layer-2-services", instances);

        SimpleDiscoveryProperties simpleDiscoveryProperties = new SimpleDiscoveryProperties();
        simpleDiscoveryProperties.setInstances(service);

        return new SimpleDiscoveryClient(simpleDiscoveryProperties);
    }
}

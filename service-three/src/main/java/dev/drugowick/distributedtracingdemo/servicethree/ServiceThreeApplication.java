package dev.drugowick.distributedtracingdemo.servicethree;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableFeignClients
public class ServiceThreeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceThreeApplication.class, args);
    }

}

@RestController
@RequestMapping("info")
@RequiredArgsConstructor
@Slf4j
class MyController {

    private final ServiceClient client;

    @GetMapping
    AllServicesInfo getInfoFromAllServices() {
        Map<String, String> allServicesInfoMap = new HashMap<>();
        allServicesInfoMap.put("Service 3", "ok");

        log.info("returning ok from " + "Service Three");

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        AllServicesInfo infoFromChildServices = client.getInfo();
        infoFromChildServices.getInfo().forEach(allServicesInfoMap::put);

        return new AllServicesInfo(allServicesInfoMap);
    }
}

@Getter
@NoArgsConstructor
@AllArgsConstructor
class AllServicesInfo {
    private Map<String, String> info = new HashMap<>();
}


@FeignClient(name = "service", url = "${my.client.url}")
interface ServiceClient {

    @GetMapping(value = "/info", consumes = MediaType.APPLICATION_JSON_VALUE)
    AllServicesInfo getInfo();
}

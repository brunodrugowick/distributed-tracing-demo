package dev.drugowick.distributedtracingdemo.servicefour;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class ServiceFourApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceFourApplication.class, args);
    }

}

@RestController
@RequestMapping("info")
@RequiredArgsConstructor
@Slf4j
class MyController {

    @GetMapping
    AllServicesInfo getInfoFromAllServices() {
        Map<String, String> allServicesInfoMap = new HashMap<>();
        allServicesInfoMap.put("Service 4", "Wow, request arrived this far!");

        log.info("returning ok from " + "Service Four");

        return new AllServicesInfo(allServicesInfoMap);
    }
}

@Getter
@NoArgsConstructor
@AllArgsConstructor
class AllServicesInfo {
    private Map<String, String> info = new HashMap<>();
}

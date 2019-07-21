package com.poc.centralizdswaggerdocumentaion.eurekaserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.shared.Applications;
import com.netflix.eureka.EurekaServerContextHolder;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class ServiceDescriptionUpdater {

    private static final Logger logger = LoggerFactory.getLogger(ServiceDescriptionUpdater.class);

    private static final String DEFAULT_SWAGGER_URL = "v2/api-docs";


    private final RestTemplate template;

    public ServiceDescriptionUpdater() {
        this.template = new RestTemplate();
    }

    @Autowired
    private ServiceDefinitionsContext definitionContext;

    @Scheduled(fixedDelayString = "${swagger.config.refreshrate}")
    public void refreshSwaggerConfigurations() {
        logger.debug("Starting Service Definition Context refresh");

        PeerAwareInstanceRegistry registry = EurekaServerContextHolder.getInstance()
                                                                      .getServerContext()
                                                                      .getRegistry();
        Applications applications = registry.getApplications();

        applications.getRegisteredApplications()
                    .forEach((registeredApplication) -> {
                        registeredApplication.getInstances()
                                             .forEach((instance) -> {
                                                 System.out.println(instance.getAppName() + " (" + instance.getInstanceId() + ") : " + instance.getHomePageUrl() + " ID " + instance.getId() + "  " + instance.getSecureVipAddress());

                                                 String swaggerURL = instance.getHomePageUrl() + DEFAULT_SWAGGER_URL;

                                                 String serviceId = instance.getSecureVipAddress();

                                                 Optional<Object> jsonData = getSwaggerDefinitionForAPI(serviceId, swaggerURL);

                                                 if (jsonData.isPresent()) {
                                                     String content = getJSON(serviceId, jsonData.get());
                                                     System.out.println("content calue :" + content);
                                                     definitionContext.addServiceDefinition(serviceId, content);
                                                 } else {
                                                     logger.error("Skipping service id : {} Error : Could not get Swagegr definition from API ", serviceId);
                                                 }

                                                 logger.info("Service Definition Context Refreshed at :  {}", LocalDate.now());

                                             });
                    });
    }


    private Optional<Object> getSwaggerDefinitionForAPI(String serviceName, String url) {
        logger.debug("Accessing the SwaggerDefinition JSON for Service : {} : URL : {} ", serviceName, url);
        try {
            System.out.println("serviceNAme :" + serviceName + "  url:" + url);
            Object jsonData = template.getForObject(url, Object.class);
            System.out.println("jonData :" + jsonData);
            return Optional.of(jsonData);
        } catch (RestClientException ex) {
            logger.error("Error while getting service definition for service : {} Error : {} ", serviceName, ex.getMessage());
            return Optional.empty();
        }

    }

    public String getJSON(String serviceId, Object jsonData) {
        try {
            return new ObjectMapper().writeValueAsString(jsonData);
        } catch (JsonProcessingException e) {
            logger.error("Error : {} ", e.getMessage());
            return "";
        }
    }
}

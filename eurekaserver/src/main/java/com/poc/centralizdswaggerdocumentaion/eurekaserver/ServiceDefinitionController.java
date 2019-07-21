package com.poc.centralizdswaggerdocumentaion.eurekaserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(
		allowCredentials = "true",
		origins = "*",
		allowedHeaders = "*",
		methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT}
)
@RestController
public class ServiceDefinitionController {
	
	@Autowired
	private ServiceDefinitionsContext definitionContext;
	
	@GetMapping("/service/{servicename}")
	public String getServiceDefinition(@PathVariable("servicename") String serviceName){

		return definitionContext.getSwaggerDefinition(serviceName);
		
	}
}

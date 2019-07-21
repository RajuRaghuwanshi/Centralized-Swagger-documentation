package com.poc.service1.demo1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.RequestHandlerSelectors.any;

@Configuration
@EnableSwagger2
public class SwaggerDocumentationConfiguration {


    ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Demo-1 API in Spring-Boot 2")
                                   .description(
                                           "Sample REST API for centalized documentation using Spring Boot and spring-fox swagger 2 ")
                                   .termsOfServiceUrl("")
                                   .version("0.0.1-SNAPSHOT")
                                   .contact(new Contact("raju", "https://github.com/rajuraghuwanshi", "https://github.com/rajuraghuwanshi"))
                                   .build();
    }

    @Bean
    public Docket configuration() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                                                      .apis(RequestHandlerSelectors.basePackage("com.poc.service1.demo1"))
                                                      .build()
                                                      .apiInfo(apiInfo());
    }


}

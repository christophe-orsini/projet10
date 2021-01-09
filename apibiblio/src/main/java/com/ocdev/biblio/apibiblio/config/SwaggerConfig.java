package com.ocdev.biblio.apibiblio.config;

import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig
{
    @Bean
    public Docket api()
    {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
        		.useDefaultResponseMessages(false)
        		.select()
                .paths(PathSelectors.ant("/api/**"))
                .apis(RequestHandlerSelectors.basePackage("com.ocdev.biblio.apibiblio.controllers"))
                .build()
                .apiInfo(apiInfo()
                );
        
        return docket;
    }
    
    private ApiInfo apiInfo() {
        return new ApiInfo(
                "REST API des bibliothèques de Bouquinville", //title
                "Mise à disposition d'endpoints pour la gestion des prêts des bibliothèques", //description
                "Version 1.0", //version
                "http://www.ocdev.com/conditions.html", //terms of service URL
                new Contact("OCDev", "http://www.ocdev.com", "info@ocdev.com"),// contact info
                "Licence GNU GPL", "https://www.gnu.org/licenses/gpl-3.0.html", Collections.emptyList()); 
    }
}
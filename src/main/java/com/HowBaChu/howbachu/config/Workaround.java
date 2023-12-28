package com.HowBaChu.howbachu.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.stereotype.Component;
import springfox.documentation.oas.web.OpenApiTransformationContext;
import springfox.documentation.oas.web.WebMvcOpenApiTransformationFilter;
import springfox.documentation.spi.DocumentationType;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Component
public class Workaround implements WebMvcOpenApiTransformationFilter {
    @Override
    public OpenAPI transform(OpenApiTransformationContext<HttpServletRequest> context) {
        OpenAPI openAPI = context.getSpecification();
        Server localServer = new Server();
        localServer.setDescription("local server");
        localServer.setUrl("http://localhost:8080");

        Server prodHttpsServer = new Server();
        prodHttpsServer.setDescription("prod Https Server");
        prodHttpsServer.setUrl("https://howbachu.kro.kr");

        Server prodHttpServer = new Server();
        prodHttpServer.setDescription("prod Http Server");
        prodHttpServer.setUrl("http://54.180.26.251:8080");

        openAPI.setServers(Arrays.asList(localServer, prodHttpsServer, prodHttpServer));
        return openAPI;
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return documentationType.equals(DocumentationType.OAS_30);
    }
}

package com.example.resilient_api.infrastructure.entrypoints;

import com.example.resilient_api.infrastructure.entrypoints.handler.TechHandlerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    
    @Bean
    public RouterFunction<ServerResponse> routerFunction(TechHandlerImpl techHandler) {
        return route(POST("/tech").and(accept(MediaType.APPLICATION_JSON)), techHandler::createTech);
    }
}

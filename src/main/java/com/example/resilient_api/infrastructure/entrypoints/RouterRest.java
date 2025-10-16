package com.example.resilient_api.infrastructure.entrypoints;

import com.example.resilient_api.infrastructure.entrypoints.handler.CapacityTechHandlerImpl;
import com.example.resilient_api.infrastructure.entrypoints.handler.TechHandlerImpl;
import com.example.resilient_api.infrastructure.entrypoints.handler.TechSagaHandlerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    
    @Bean
    public RouterFunction<ServerResponse> routerFunction(TechHandlerImpl techHandler, CapacityTechHandlerImpl capacityTechHandler) {
        return route(POST("/tech").and(accept(MediaType.APPLICATION_JSON)), techHandler::createTech)
                .andRoute(GET("/tech/{id}"), techHandler::getTech)
                .andRoute(POST("/capacity-tech").and(accept(MediaType.APPLICATION_JSON)), capacityTechHandler::assignTechToCapacity)
                .andRoute(GET("/capacity/{capacityId}/techs"), capacityTechHandler::getTechsByCapacity)
                .andRoute(DELETE("/capacity-tech/{capacityId}"), capacityTechHandler::deleteCapacityTechRelations);
    }
    
    @Bean
    public RouterFunction<ServerResponse> sagaRoutes(TechSagaHandlerImpl sagaHandler) {
        return route(DELETE("/saga/techs/orphan"), sagaHandler::deleteOrphanTechs);
    }
}

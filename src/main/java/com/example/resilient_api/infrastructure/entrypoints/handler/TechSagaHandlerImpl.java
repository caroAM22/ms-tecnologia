package com.example.resilient_api.infrastructure.entrypoints.handler;

import com.example.resilient_api.domain.usecase.TechSagaUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TechSagaHandlerImpl {
    
    private final TechSagaUseCase techSagaUseCase;
    
    public Mono<ServerResponse> deleteOrphanTechs(ServerRequest request) {
        return request.bodyToMono(List.class)
                .cast(List.class)
                .flatMap(techSagaUseCase::deleteOrphanTechs)
                .then(ServerResponse.ok().build())
                .onErrorResume(e -> ServerResponse.badRequest().build());
    }
}
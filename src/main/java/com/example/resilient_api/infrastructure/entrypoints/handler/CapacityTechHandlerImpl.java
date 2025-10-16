package com.example.resilient_api.infrastructure.entrypoints.handler;

import com.example.resilient_api.domain.api.CapacityTechServicePort;
import com.example.resilient_api.domain.enums.TechnicalMessage;
import com.example.resilient_api.domain.exceptions.BusinessException;
import com.example.resilient_api.infrastructure.entrypoints.dto.CapacityTechDTO;
import com.example.resilient_api.infrastructure.entrypoints.mapper.CapacityTechMapper;
import com.example.resilient_api.infrastructure.entrypoints.mapper.TechMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class CapacityTechHandlerImpl {

    private final CapacityTechServicePort capacityTechServicePort;
    private final CapacityTechMapper capacityTechMapper;
    private final TechMapper techMapper;

    public Mono<ServerResponse> assignTechToCapacity(ServerRequest request) {
        return request.bodyToMono(CapacityTechDTO.class)
                .flatMap(dto -> {
                    var capacityTech = capacityTechMapper.capacityTechDTOToCapacityTech(dto);
                    return capacityTechServicePort.assignTechToCapacity(capacityTech);
                })
                .flatMap(savedCapacityTech -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .bodyValue(TechnicalMessage.TECH_CREATED.getMessage()))
                .onErrorResume(BusinessException.class, ex -> 
                    ServerResponse.badRequest().bodyValue(ex.getMessage()));
    }

    public Mono<ServerResponse> getTechsByCapacity(ServerRequest request) {
        String capacityId = request.pathVariable("capacityId");
        return capacityTechServicePort.getTechsByCapacityId(capacityId)
                .map(techMapper::techToTechDTO)
                .collectList()
                .flatMap(techDTOs -> ServerResponse.ok().bodyValue(techDTOs));
    }
    
    public Mono<ServerResponse> deleteCapacityTechRelations(ServerRequest request) {
        String capacityId = request.pathVariable("capacityId");
        return capacityTechServicePort.deleteByCapacityId(capacityId)
                .then(ServerResponse.noContent().build())
                .onErrorResume(ex -> ServerResponse.badRequest().bodyValue("Error deleting capacity-tech relations: " + ex.getMessage()));
    }
}
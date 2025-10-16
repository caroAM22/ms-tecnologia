package com.example.resilient_api.domain.api;

import com.example.resilient_api.domain.model.CapacityTech;
import com.example.resilient_api.domain.model.Tech;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CapacityTechServicePort {
    Mono<CapacityTech> assignTechToCapacity(CapacityTech capacityTech);
    Flux<Tech> getTechsByCapacityId(String capacityId);
    Mono<Void> deleteByCapacityId(String capacityId);
}
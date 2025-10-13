package com.example.resilient_api.domain.spi;

import com.example.resilient_api.domain.model.CapacityTech;
import com.example.resilient_api.domain.model.Tech;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CapacityTechPersistencePort {
    Mono<CapacityTech> save(CapacityTech capacityTech);
    Mono<Boolean> existsByCapacityIdAndTechId(String capacityId, String techId);
    Flux<Tech> getTechsByCapacityId(String capacityId);
}
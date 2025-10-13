package com.example.resilient_api.infrastructure.adapters.persistenceadapter.repository;

import com.example.resilient_api.infrastructure.adapters.persistenceadapter.entity.CapacityTechEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CapacityTechRepository extends R2dbcRepository<CapacityTechEntity, String> {
    Mono<Boolean> existsByCapacityIdAndTechId(String capacityId, String techId);
    Flux<CapacityTechEntity> findByCapacityId(String capacityId);
}
package com.example.resilient_api.domain.spi;

import com.example.resilient_api.domain.model.Tech;
import reactor.core.publisher.Mono;

public interface TechPersistencePort {
    Mono<Tech> save(Tech user);
    Mono<Boolean> existByName(String name);
    Mono<Tech> findById(String id);
}

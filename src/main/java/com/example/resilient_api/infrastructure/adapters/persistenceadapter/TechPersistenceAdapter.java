package com.example.resilient_api.infrastructure.adapters.persistenceadapter;

import com.example.resilient_api.domain.spi.TechPersistencePort;
import com.example.resilient_api.infrastructure.adapters.persistenceadapter.repository.TechRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;
import com.example.resilient_api.infrastructure.adapters.persistenceadapter.mapper.TechEntityMapper;
import com.example.resilient_api.domain.model.Tech;

@AllArgsConstructor
public class TechPersistenceAdapter implements TechPersistencePort {
    private final TechRepository techRepository;
    private final TechEntityMapper techEntityMapper;

    @Override
    public Mono<Tech> save(Tech tech) {
        var entity = techEntityMapper.toEntity(tech);
        return techRepository.save(entity)
                .map(techEntityMapper::toModel);
    }

    @Override
    public Mono<Boolean> existByName(String name) {
        return techRepository.findByName(name)
                .map(techEntityMapper::toModel)
                .map(tech -> true)
                .defaultIfEmpty(false);
    }

    @Override
    public Mono<Tech> findById(String id) {
        return techRepository.findById(id)
                .map(techEntityMapper::toModel);
    }

}

package com.example.resilient_api.infrastructure.adapters.persistenceadapter;

import com.example.resilient_api.domain.model.CapacityTech;
import com.example.resilient_api.domain.model.Tech;
import com.example.resilient_api.domain.spi.CapacityTechPersistencePort;
import com.example.resilient_api.domain.spi.TechPersistencePort;
import com.example.resilient_api.infrastructure.adapters.persistenceadapter.mapper.CapacityTechEntityMapper;
import com.example.resilient_api.infrastructure.adapters.persistenceadapter.repository.CapacityTechRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class CapacityTechPersistenceAdapter implements CapacityTechPersistencePort {
    
    private final CapacityTechRepository capacityTechRepository;
    private final CapacityTechEntityMapper capacityTechEntityMapper;
    private final TechPersistencePort techPersistencePort;

    @Override
    public Mono<CapacityTech> save(CapacityTech capacityTech) {
        var entity = capacityTechEntityMapper.toEntity(capacityTech);
        return capacityTechRepository.save(entity)
                .map(capacityTechEntityMapper::toModel);
    }

    @Override
    public Mono<Boolean> existsByCapacityIdAndTechId(String capacityId, String techId) {
        return capacityTechRepository.existsByCapacityIdAndTechId(capacityId, techId);
    }

    @Override
    public Flux<Tech> getTechsByCapacityId(String capacityId) {
        return capacityTechRepository.findByCapacityId(capacityId)
                .map(entity -> entity.getTechId())
                .flatMap(techPersistencePort::findById);
    }
}
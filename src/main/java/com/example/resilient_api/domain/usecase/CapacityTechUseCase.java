package com.example.resilient_api.domain.usecase;

import com.example.resilient_api.domain.api.CapacityTechServicePort;
import com.example.resilient_api.domain.enums.TechnicalMessage;
import com.example.resilient_api.domain.exceptions.BusinessException;
import com.example.resilient_api.domain.model.CapacityTech;
import com.example.resilient_api.domain.model.Tech;
import com.example.resilient_api.domain.spi.CapacityTechPersistencePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CapacityTechUseCase implements CapacityTechServicePort {

    private final CapacityTechPersistencePort capacityTechPersistencePort;

    @Override
    public Mono<CapacityTech> assignTechToCapacity(CapacityTech capacityTech) {
        return validateIds(capacityTech)
                .then(capacityTechPersistencePort.existsByCapacityIdAndTechId(
                    capacityTech.capacityId(), capacityTech.techId()))
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new BusinessException(TechnicalMessage.TECH_ALREADY_EXISTS));
                    }
                    return capacityTechPersistencePort.save(capacityTech);
                });
    }

    private Mono<Void> validateIds(CapacityTech capacityTech) {
        String capacityId = capacityTech.capacityId();
        if (capacityId == null || capacityId.isEmpty()) {
            return Mono.error(new BusinessException(TechnicalMessage.INVALID_PARAMETERS));
        }
        
        String techId = capacityTech.techId();
        if (techId == null || techId.isEmpty()) {
            return Mono.error(new BusinessException(TechnicalMessage.INVALID_PARAMETERS));
        }
        
        return Mono.empty();
    }

    @Override
    public Flux<Tech> getTechsByCapacityId(String capacityId) {
        return capacityTechPersistencePort.getTechsByCapacityId(capacityId);
    }
    
    @Override
    public Mono<Void> deleteByCapacityId(String capacityId) {
        return capacityTechPersistencePort.deleteByCapacityId(capacityId);
    }
}
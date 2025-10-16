package com.example.resilient_api.domain.usecase;

import com.example.resilient_api.domain.spi.CapacityTechPersistencePort;
import com.example.resilient_api.domain.spi.TechPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class TechSagaUseCase {
    
    private final CapacityTechPersistencePort capacityTechPersistencePort;
    private final TechPersistencePort techPersistencePort;
    
    public Mono<Void> deleteOrphanTechs(List<String> deletedCapacityIds) {
        log.info("Checking for orphan techs from deleted capacities: {}", deletedCapacityIds);
        return Flux.fromIterable(deletedCapacityIds)
                .flatMap(capacityTechPersistencePort::getTechsByCapacityId)
                .map(tech -> tech.id().toString())
                .distinct()
                .collectList()
                .flatMapMany(techIds -> {
                    log.info("Found {} unique techs to check: {}", techIds.size(), techIds);
                    return Flux.fromIterable(techIds)
                            .filterWhen(techId -> checkIfTechIsOrphan(techId, deletedCapacityIds));
                })
                .flatMap(this::deleteTechAndRelations)
                .doOnNext(techId -> log.info("Tech {} deleted successfully", techId))
                .doOnComplete(() -> log.info("Orphan tech cleanup completed"))
                .then();
    }
    
    private Mono<Boolean> checkIfTechIsOrphan(String techId, List<String> deletedCapacityIds) {
        return getCapacitiesThatUseTech(techId)
                .collectList()
                .map(capacitiesThatUseTech -> {
                    // Verificar si TODAS las capacidades que usan esta tech están siendo eliminadas
                    boolean allCapacitiesBeingDeleted = deletedCapacityIds.containsAll(capacitiesThatUseTech);
                    log.info("Tech {} is used by capacities: {}, being deleted: {}, orphan: {}", 
                            techId, capacitiesThatUseTech, deletedCapacityIds, allCapacitiesBeingDeleted);
                    return allCapacitiesBeingDeleted;
                });
    }
    
    private Flux<String> getCapacitiesThatUseTech(String techId) {
        return capacityTechPersistencePort.getCapacitiesByTechId(techId);
    }
    
    private Mono<String> deleteTechAndRelations(String techId) {
        return capacityTechPersistencePort.deleteByTechId(techId)
                .then(techPersistencePort.deleteById(techId))
                .thenReturn(techId);
    }
}
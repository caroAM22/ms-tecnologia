package com.example.resilient_api.domain.usecase;

import com.example.resilient_api.domain.model.Tech;
import com.example.resilient_api.domain.spi.CapacityTechPersistencePort;
import com.example.resilient_api.domain.spi.TechPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TechSagaUseCaseTest {

    @Mock
    private CapacityTechPersistencePort capacityTechPersistencePort;
    @Mock
    private TechPersistencePort techPersistencePort;

    private TechSagaUseCase techSagaUseCase;

    @BeforeEach
    void setUp() {
        techSagaUseCase = new TechSagaUseCase(capacityTechPersistencePort, techPersistencePort);
    }

    @Test
    void deleteOrphanTechs_ShouldProcessCapacitiesAndLogTechs() {
        String capacityId1 = "cap1";
        String capacityId2 = "cap2";
        
        Tech tech1 = new Tech(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"), "Java", "Java tech");
        Tech tech2 = new Tech(UUID.fromString("550e8400-e29b-41d4-a716-446655440002"), "Spring", "Spring tech");
        
        when(capacityTechPersistencePort.getTechsByCapacityId(capacityId1))
                .thenReturn(Flux.just(tech1));
        when(capacityTechPersistencePort.getTechsByCapacityId(capacityId2))
                .thenReturn(Flux.just(tech2));
        when(capacityTechPersistencePort.getCapacitiesByTechId(tech1.id().toString()))
                .thenReturn(Flux.just(capacityId1));
        when(capacityTechPersistencePort.getCapacitiesByTechId(tech2.id().toString()))
                .thenReturn(Flux.just(capacityId2));
        when(capacityTechPersistencePort.deleteByTechId(tech1.id().toString()))
                .thenReturn(Mono.empty());
        when(capacityTechPersistencePort.deleteByTechId(tech2.id().toString()))
                .thenReturn(Mono.empty());
        when(techPersistencePort.deleteById(tech1.id().toString()))
                .thenReturn(Mono.empty());
        when(techPersistencePort.deleteById(tech2.id().toString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(techSagaUseCase.deleteOrphanTechs(Arrays.asList(capacityId1, capacityId2)))
                .verifyComplete();
    }

    @Test
    void deleteOrphanTechs_WhenNoCapacities_ShouldComplete() {
        StepVerifier.create(techSagaUseCase.deleteOrphanTechs(Arrays.asList()))
                .verifyComplete();
    }
}
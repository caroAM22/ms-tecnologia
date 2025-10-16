package com.example.resilient_api.domain.usecase;

import com.example.resilient_api.domain.exceptions.BusinessException;
import com.example.resilient_api.domain.model.CapacityTech;
import com.example.resilient_api.domain.model.Tech;
import com.example.resilient_api.domain.spi.CapacityTechPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.Mockito.when;

class CapacityTechUseCaseTest {

    private CapacityTechPersistencePort mockPort;
    private CapacityTechUseCase useCase;

    @BeforeEach
    void setUp() {
        mockPort = Mockito.mock(CapacityTechPersistencePort.class);
        useCase = new CapacityTechUseCase(mockPort);
    }

    @Test
    void shouldAssignTechToCapacitySuccessfully() {
        CapacityTech capacityTech = new CapacityTech("capacity-1", "tech-1");
        
        when(mockPort.existsByCapacityIdAndTechId("capacity-1", "tech-1")).thenReturn(Mono.just(false));
        when(mockPort.save(capacityTech)).thenReturn(Mono.just(capacityTech));

        StepVerifier.create(useCase.assignTechToCapacity(capacityTech))
                .expectNext(capacityTech)
                .verifyComplete();
    }

    @Test
    void shouldFailWhenRelationAlreadyExists() {
        CapacityTech capacityTech = new CapacityTech("capacity-1", "tech-1");
        
        when(mockPort.existsByCapacityIdAndTechId("capacity-1", "tech-1")).thenReturn(Mono.just(true));

        StepVerifier.create(useCase.assignTechToCapacity(capacityTech))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void shouldGetTechsByCapacityIdSuccessfully() {
        String capacityId = "capacity-1";
        Tech tech1 = new Tech(UUID.randomUUID(), "Java", "Programming language");
        Tech tech2 = new Tech(UUID.randomUUID(), "Python", "High-level language");
        
        when(mockPort.getTechsByCapacityId(capacityId)).thenReturn(Flux.just(tech1, tech2));

        StepVerifier.create(useCase.getTechsByCapacityId(capacityId))
                .expectNext(tech1)
                .expectNext(tech2)
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenNoTechsForCapacity() {
        String capacityId = "capacity-1";
        
        when(mockPort.getTechsByCapacityId(capacityId)).thenReturn(Flux.empty());

        StepVerifier.create(useCase.getTechsByCapacityId(capacityId))
                .verifyComplete();
    }
    
    @Test
    void shouldDeleteByCapacityIdSuccessfully() {
        String capacityId = "capacity-1";
        
        when(mockPort.deleteByCapacityId(capacityId)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.deleteByCapacityId(capacityId))
                .verifyComplete();
    }
}
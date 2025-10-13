package com.example.resilient_api.domain.usecase;

import com.example.resilient_api.domain.model.Tech;
import com.example.resilient_api.domain.spi.TechPersistencePort;
import com.example.resilient_api.domain.exceptions.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class TechUseCaseTest {

    private TechPersistencePort mockPort;
    private TechUseCase useCase;

    @BeforeEach
    void setUp() {
        mockPort = Mockito.mock(TechPersistencePort.class);
        useCase = new TechUseCase(mockPort);
        when(mockPort.existByName(anyString())).thenReturn(Mono.just(false));
        when(mockPort.existByName(null)).thenReturn(Mono.just(false));
    }

    @Test
    void shouldRegisterTechSuccessfully() {
        Tech inputTech = new Tech(null, "Java", "Programming language");
        Tech savedTech = new Tech(UUID.randomUUID(), "Java", "Programming language");
        
        when(mockPort.save(any(Tech.class))).thenReturn(Mono.just(savedTech));

        StepVerifier.create(useCase.registerTech(inputTech))
                .expectNext(savedTech)
                .verifyComplete();
    }

    @Test
    void shouldFailWhenNameExceeds50Characters() {
        String longName = "a".repeat(51);
        Tech inputTech = new Tech(null, longName, "Valid description");

        StepVerifier.create(useCase.registerTech(inputTech))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void shouldFailWhenNameNull() {
        Tech inputTech = new Tech(null, null, "Valid description");

        StepVerifier.create(useCase.registerTech(inputTech))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void shouldFailWhenNameIsEmpty() {
        Tech inputTech = new Tech(null, "", "Valid description");

        StepVerifier.create(useCase.registerTech(inputTech))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void shouldFailWhenDescriptionNull() {
        Tech inputTech = new Tech(null, "Programming language", null);

        StepVerifier.create(useCase.registerTech(inputTech))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void shouldFailWhenDescriptionIsEmpty() {
        Tech inputTech = new Tech(null, "Programming language", "");

        StepVerifier.create(useCase.registerTech(inputTech))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void shouldFailWhenDescriptionExceeds90Characters() {
        String longDescription = "b".repeat(91);
        Tech inputTech = new Tech(null, "ValidName", longDescription);

        StepVerifier.create(useCase.registerTech(inputTech))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void shouldFailWhenTechAlreadyExists() {
        Tech inputTech = new Tech(null, "Java", "Programming language");
        
        when(mockPort.existByName("Java")).thenReturn(Mono.just(true));

        StepVerifier.create(useCase.registerTech(inputTech))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void shouldGetTechByIdSuccessfully() {
        String techId = "123e4567-e89b-12d3-a456-426614174000";
        Tech expectedTech = new Tech(UUID.fromString(techId), "Java", "Programming language");
        
        when(mockPort.findById(techId)).thenReturn(Mono.just(expectedTech));

        StepVerifier.create(useCase.getTechById(techId))
                .expectNext(expectedTech)
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenTechNotFound() {
        String techId = "123e4567-e89b-12d3-a456-426614174000";
        
        when(mockPort.findById(techId)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.getTechById(techId))
                .verifyComplete();
    }
}
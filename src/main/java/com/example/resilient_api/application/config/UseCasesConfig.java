package com.example.resilient_api.application.config;

import com.example.resilient_api.domain.api.*;
import com.example.resilient_api.domain.spi.*;
import com.example.resilient_api.domain.usecase.*;
import com.example.resilient_api.infrastructure.adapters.persistenceadapter.*;
import com.example.resilient_api.infrastructure.adapters.persistenceadapter.mapper.*;
import com.example.resilient_api.infrastructure.adapters.persistenceadapter.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class UseCasesConfig {
        private final TechRepository techRepository;
        private final TechEntityMapper techEntityMapper;
        private final CapacityTechRepository capacityTechRepository;
        private final CapacityTechEntityMapper capacityTechEntityMapper;

        @Bean
        public TechPersistencePort techPersistencePort() {
                return new TechPersistenceAdapter(techRepository,techEntityMapper);
        }

        @Bean
        public TechServicePort usersServicePort(TechPersistencePort usersPersistencePort){
                return new TechUseCase(techPersistencePort());
        }

        @Bean
        public CapacityTechPersistencePort capacityTechPersistencePort() {
                return new CapacityTechPersistenceAdapter(capacityTechRepository, capacityTechEntityMapper, techPersistencePort());
        }

        @Bean
        public CapacityTechServicePort capacityTechServicePort() {
                return new CapacityTechUseCase(capacityTechPersistencePort());
        }
        
        @Bean
        public TechSagaUseCase techSagaUseCase() {
                return new TechSagaUseCase(capacityTechPersistencePort(), techPersistencePort());
        }
}

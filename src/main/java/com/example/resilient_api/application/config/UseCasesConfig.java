package com.example.resilient_api.application.config;

import com.example.resilient_api.domain.api.CapacityTechServicePort;
import com.example.resilient_api.domain.spi.CapacityTechPersistencePort;
import com.example.resilient_api.domain.spi.TechPersistencePort;
import com.example.resilient_api.domain.usecase.CapacityTechUseCase;
import com.example.resilient_api.infrastructure.adapters.persistenceadapter.CapacityTechPersistenceAdapter;
import com.example.resilient_api.infrastructure.adapters.persistenceadapter.TechPersistenceAdapter;
import com.example.resilient_api.infrastructure.adapters.persistenceadapter.mapper.CapacityTechEntityMapper;
import com.example.resilient_api.infrastructure.adapters.persistenceadapter.repository.CapacityTechRepository;
import com.example.resilient_api.infrastructure.adapters.persistenceadapter.repository.TechRepository;
import com.example.resilient_api.infrastructure.adapters.persistenceadapter.mapper.TechEntityMapper;
import lombok.RequiredArgsConstructor;
import com.example.resilient_api.domain.usecase.TechUseCase;
import com.example.resilient_api.domain.api.TechServicePort;
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
}

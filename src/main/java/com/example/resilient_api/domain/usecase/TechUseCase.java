package com.example.resilient_api.domain.usecase;

import com.example.resilient_api.domain.enums.TechnicalMessage;
import com.example.resilient_api.domain.exceptions.BusinessException;
import com.example.resilient_api.domain.model.Tech;
import com.example.resilient_api.domain.spi.TechPersistencePort;
import com.example.resilient_api.domain.api.TechServicePort;
import reactor.core.publisher.Mono;
import lombok.RequiredArgsConstructor;
import com.example.resilient_api.domain.constants.Constants;
import java.util.UUID;

@RequiredArgsConstructor
public class TechUseCase implements TechServicePort {

    private final TechPersistencePort techPersistencePort;

    @Override
    public Mono<Tech> registerTech(Tech tech) {
        return validateNameLength(tech.name())
                .then(validateDescriptionLength(tech.description()))
                .then(techPersistencePort.existByName(tech.name()))
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new BusinessException(TechnicalMessage.TECH_ALREADY_EXISTS));
                    }
                    return techPersistencePort.save(new Tech(UUID.randomUUID(), tech.name(), tech.description()));
                });
    }

    private Mono<Void> validateNameLength(String name) {
        if (name == null || name.isEmpty()) {
            return Mono.error(new BusinessException(TechnicalMessage.NAME_REQUIRED));
        }
        if (name.length() > Constants.NAME_MAX_LENGTH) {
            return Mono.error(new BusinessException(TechnicalMessage.INVALID_NAME));
        }
        return Mono.empty();
    }

    private Mono<Void> validateDescriptionLength(String description) {
        if (description == null || description.isEmpty()) {
            return Mono.error(new BusinessException(TechnicalMessage.DESCRIPTION_REQUIRED));
        }
        if (description.length() > Constants.DESCRIPTION_MAX_LENGTH) {
            return Mono.error(new BusinessException(TechnicalMessage.INVALID_DESCRIPTION));
        }
        return Mono.empty();
    }

}

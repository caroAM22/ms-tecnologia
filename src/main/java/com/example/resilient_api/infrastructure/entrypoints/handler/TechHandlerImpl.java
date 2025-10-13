package com.example.resilient_api.infrastructure.entrypoints.handler;

import com.example.resilient_api.domain.enums.TechnicalMessage;
import com.example.resilient_api.domain.exceptions.BusinessException;
import com.example.resilient_api.domain.exceptions.TechnicalException;
import com.example.resilient_api.infrastructure.entrypoints.dto.TechDTO;
import com.example.resilient_api.infrastructure.entrypoints.util.APIResponse;
import com.example.resilient_api.infrastructure.entrypoints.util.ErrorDTO;
import com.example.resilient_api.domain.api.TechServicePort;
import com.example.resilient_api.infrastructure.entrypoints.mapper.TechMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

import static com.example.resilient_api.infrastructure.entrypoints.util.Constants.X_MESSAGE_ID;
import static com.example.resilient_api.infrastructure.entrypoints.util.Constants.USER_ERROR;

@Component
@RequiredArgsConstructor
@Slf4j
public class TechHandlerImpl {

    private final TechServicePort techServicePort;
    private final TechMapper techMapper;

    public Mono<ServerResponse> createTech(ServerRequest request) {
        String messageId = getMessageId(request);
        return request.bodyToMono(TechDTO.class)
                .doOnNext(techDTO -> log.info("Received TechDTO: name={}, description={}", techDTO.getName(), techDTO.getDescription()))
                .flatMap(techDTO -> {
                    var tech = techMapper.techDTOToTech(techDTO);
                    log.info("Mapped Tech: name={}, description={}", tech.name(), tech.description());
                    return techServicePort.registerTech(tech)
                            .doOnSuccess(savedTech -> log.info("Tech created successfully with messageId: {}", messageId));
                })
                .flatMap(savedTech -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .bodyValue(TechnicalMessage.TECH_CREATED.getMessage()))
                .doOnError(ex -> log.error(USER_ERROR, ex))
                .onErrorResume(BusinessException.class, ex -> buildErrorResponse(
                        HttpStatus.BAD_REQUEST,
                        messageId,
                        TechnicalMessage.INVALID_PARAMETERS,
                        List.of(ErrorDTO.builder()
                                .code(ex.getTechnicalMessage().getCode())
                                .message(ex.getTechnicalMessage().getMessage())
                                .param(ex.getTechnicalMessage().getParam())
                                .build())))
                .onErrorResume(TechnicalException.class, ex -> buildErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        messageId,
                        TechnicalMessage.INTERNAL_ERROR,
                        List.of(ErrorDTO.builder()
                                .code(ex.getTechnicalMessage().getCode())
                                .message(ex.getTechnicalMessage().getMessage())
                                .param(ex.getTechnicalMessage().getParam())
                                .build())))
                .onErrorResume(ex -> {
                    log.error("Unexpected error occurred for messageId: {}", messageId, ex);
                    return buildErrorResponse(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            messageId,
                            TechnicalMessage.INTERNAL_ERROR,
                            List.of(ErrorDTO.builder()
                                    .code(TechnicalMessage.INTERNAL_ERROR.getCode())
                                    .message(TechnicalMessage.INTERNAL_ERROR.getMessage())
                                    .build()));
                });
    }

    public Mono<ServerResponse> getTech(ServerRequest request) {
        String id = request.pathVariable("id");
        return techServicePort.getTechById(id)
                .map(techMapper::techToTechDTO)
                .flatMap(techDTO -> ServerResponse.ok().bodyValue(techDTO))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    private Mono<ServerResponse> buildErrorResponse(HttpStatus httpStatus, String identifier, TechnicalMessage error,
                                                    List<ErrorDTO> errors) {
        return Mono.defer(() -> {
            APIResponse apiErrorResponse = APIResponse
                    .builder()
                    .code(error.getCode())
                    .message(error.getMessage())
                    .identifier(identifier)
                    .date(Instant.now().toString())
                    .errors(errors)
                    .build();
            return ServerResponse.status(httpStatus)
                    .bodyValue(apiErrorResponse);
        });
    }

    private String getMessageId(ServerRequest serverRequest) {
        return serverRequest.headers().firstHeader(X_MESSAGE_ID);
    }
}

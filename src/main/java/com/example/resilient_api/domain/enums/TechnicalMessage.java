package com.example.resilient_api.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TechnicalMessage {


    INTERNAL_ERROR("500","Something went wrong, please try again", ""),
    INTERNAL_ERROR_IN_ADAPTERS("PRC501","Something went wrong in adapters, please try again", ""),
    INVALID_REQUEST("400", "Bad Request, please verify data", ""),
    INVALID_PARAMETERS(INVALID_REQUEST.getCode(), "Bad Parameters, please verify data", ""),
    INVALID_NAME("404", "Invalid name, please verify", "name"),
    NAME_REQUIRED("404", "Name is required, please verify", "name"),
    DESCRIPTION_REQUIRED("404", "Description is required, please verify", "description"),
    INVALID_DESCRIPTION("404", "Invalid description, please verify", "description"),
    UNSUPPORTED_OPERATION("501", "Method not supported, please try again", ""),
    TECH_CREATED("201", "Tech created successfully", ""),
    ADAPTER_RESPONSE_NOT_FOUND("404-0", "invalid email, please verify", ""),
    TECH_ALREADY_EXISTS("409","Tech already exists." ,"" );

    private final String code;
    private final String message;
    private final String param;
}
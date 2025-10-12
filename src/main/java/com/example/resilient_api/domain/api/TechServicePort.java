package com.example.resilient_api.domain.api;

import com.example.resilient_api.domain.model.Tech;
import reactor.core.publisher.Mono;

public interface TechServicePort {
    Mono<Tech> registerTech(Tech tech);
}

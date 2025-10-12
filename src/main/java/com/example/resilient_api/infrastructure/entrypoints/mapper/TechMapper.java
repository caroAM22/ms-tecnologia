package com.example.resilient_api.infrastructure.entrypoints.mapper;

import com.example.resilient_api.domain.model.Tech;
import com.example.resilient_api.infrastructure.entrypoints.dto.TechDTO;
import org.springframework.stereotype.Component;

@Component
public class TechMapper {
    
    public Tech techDTOToTech(TechDTO techDTO) {
        if (techDTO == null) {
            return null;
        }
        return new Tech(null, techDTO.getName(), techDTO.getDescription());
    }
    
    public TechDTO techToTechDTO(Tech tech) {
        if (tech == null) {
            return null;
        }
        return TechDTO.builder()
                .id(tech.id() != null ? tech.id().toString() : null)
                .name(tech.name())
                .description(tech.description())
                .build();
    }
}
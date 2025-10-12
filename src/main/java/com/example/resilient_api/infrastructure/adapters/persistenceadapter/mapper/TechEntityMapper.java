package com.example.resilient_api.infrastructure.adapters.persistenceadapter.mapper;

import com.example.resilient_api.domain.model.Tech;
import com.example.resilient_api.infrastructure.adapters.persistenceadapter.entity.TechEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TechEntityMapper {
    
    public Tech toModel(TechEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Tech(
            entity.getId() != null ? UUID.fromString(entity.getId()) : null,
            entity.getName(),
            entity.getDescription()
        );
    }
    
    public TechEntity toEntity(Tech tech) {
        if (tech == null) {
            return null;
        }
        TechEntity entity = new TechEntity();
        entity.setId(tech.id() != null ? tech.id().toString() : null);
        entity.setName(tech.name());
        entity.setDescription(tech.description());
        return entity;
    }
}
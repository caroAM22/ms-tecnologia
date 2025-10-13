package com.example.resilient_api.infrastructure.adapters.persistenceadapter.mapper;

import com.example.resilient_api.domain.model.CapacityTech;
import com.example.resilient_api.infrastructure.adapters.persistenceadapter.entity.CapacityTechEntity;
import org.springframework.stereotype.Component;

@Component
public class CapacityTechEntityMapper {
    
    public CapacityTech toModel(CapacityTechEntity entity) {
        if (entity == null) {
            return null;
        }
        return new CapacityTech(entity.getCapacityId(), entity.getTechId());
    }
    
    public CapacityTechEntity toEntity(CapacityTech capacityTech) {
        if (capacityTech == null) {
            return null;
        }
        return new CapacityTechEntity(capacityTech.capacityId(), capacityTech.techId());
    }
}
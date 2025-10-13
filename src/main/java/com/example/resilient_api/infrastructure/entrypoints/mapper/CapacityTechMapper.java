package com.example.resilient_api.infrastructure.entrypoints.mapper;

import com.example.resilient_api.domain.model.CapacityTech;
import com.example.resilient_api.infrastructure.entrypoints.dto.CapacityTechDTO;
import org.springframework.stereotype.Component;

@Component
public class CapacityTechMapper {
    
    public CapacityTech capacityTechDTOToCapacityTech(CapacityTechDTO dto) {
        if (dto == null) {
            return null;
        }
        return new CapacityTech(dto.getCapacityId(), dto.getTechId());
    }
    
    public CapacityTechDTO capacityTechToCapacityTechDTO(CapacityTech capacityTech) {
        if (capacityTech == null) {
            return null;
        }
        return CapacityTechDTO.builder()
                .capacityId(capacityTech.capacityId())
                .techId(capacityTech.techId())
                .build();
    }
}
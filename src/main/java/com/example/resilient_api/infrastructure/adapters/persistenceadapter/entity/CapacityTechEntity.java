package com.example.resilient_api.infrastructure.adapters.persistenceadapter.entity;

import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "capacity_tech")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CapacityTechEntity {
    private String capacityId;
    private String techId;
}
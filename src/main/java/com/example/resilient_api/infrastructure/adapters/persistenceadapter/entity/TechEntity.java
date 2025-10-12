package com.example.resilient_api.infrastructure.adapters.persistenceadapter.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "techs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechEntity implements Persistable<String> {
    @Id
    private String id;
    private String name;
    private String description;
    
    @Transient
    private boolean isNew = true;
    
    @Override
    public boolean isNew() {
        return isNew;
    }
    
    public void setNotNew() {
        this.isNew = false;
    }
}

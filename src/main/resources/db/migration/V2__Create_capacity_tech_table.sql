CREATE TABLE capacity_tech (
    capacity_id VARCHAR(36) NOT NULL,
    tech_id VARCHAR(36) NOT NULL,
    PRIMARY KEY (capacity_id, tech_id)
);

DROP TABLE IF EXISTS advocate_clerks;

CREATE TABLE advocate_clerks (
    id UUID PRIMARY KEY,                          -- Unique identifier (UUID)
    tenant_id VARCHAR(128) NOT NULL,               -- Tenant ID
    application_number VARCHAR(64),               -- Application number
    state_regn_number VARCHAR(64),                 -- State registration number
    individual_id VARCHAR(64),                    -- Individual ID
    is_active BOOLEAN DEFAULT TRUE,               -- Active status
    created_by character varying(64),
    last_modified_by character varying(64),
    created_time bigint,
    last_modified_time bigint,
    additional_details JSONB                       -- Additional details (JSONB type)
);

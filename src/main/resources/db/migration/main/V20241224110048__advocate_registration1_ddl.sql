-- Drop the table if it exists
DROP TABLE IF EXISTS advocates;

-- Create the table
CREATE TABLE advocates (
           id UUID PRIMARY KEY,                       -- UUID type for unique identifier
           tenant_id VARCHAR(128) NOT NULL,           -- Tenant ID, length up to 128 characters
           application_number VARCHAR(64) NOT NULL,  -- Application number, length up to 64 characters
           bar_registration_number VARCHAR(64),      -- Bar registration number, length up to 64 characters
           advocate_type VARCHAR(64),                 -- Advocate type, length up to 64 characters
           organisation_id UUID,                      -- Organization ID (Foreign key to organisation table if exists)
           individual_id VARCHAR(64),                 -- Individual ID, length up to 64 characters
           is_active BOOLEAN NOT NULL DEFAULT true,   -- Active status (default true)
           created_by character varying(64),
           last_modified_by character varying(64),
           created_time bigint,
           last_modified_time bigint,
           additional_details JSONB
);



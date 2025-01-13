-- Drop the common documents table if it exists
DROP TABLE IF EXISTS adv_documents;

-- Create the common documents table
CREATE TABLE adv_documents (
    id                        character varying(64),
    file_store              character varying(64),
    document_type             character varying(64),
    document_uid              character varying(64),
    advocate_id               UUID,
    advocate_clerk_id         UUID,
    additional_details        JSONB,
    CONSTRAINT pk_documents PRIMARY KEY (id),
    CONSTRAINT fk_documents_advocate FOREIGN KEY (advocate_id) REFERENCES advocates (id),
    CONSTRAINT fk_documents_advocate_clerk FOREIGN KEY (advocate_clerk_id) REFERENCES advocate_clerks (id)
);

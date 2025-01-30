CREATE TABLE document_signer (
    id UUID PRIMARY KEY,
    signer_id UUID NOT NULL,
    document_id UUID NOT NULL,
    assigned_at TIMESTAMP DEFAULT NOW(),
    signed_at TIMESTAMP,
    document_hash VARCHAR(255),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (signer_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE
);

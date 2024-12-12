CREATE TABLE document_signatures (
    id UUID PRIMARY KEY,
    document_id UUID NOT NULL,
    signer_id UUID NOT NULL,
    signed_at TIMESTAMP NOT NULL,
    FOREIGN KEY (document_id) REFERENCES documents (id),
    FOREIGN KEY (signer_id) REFERENCES users (id),
    UNIQUE (document_id, signer_id)
);

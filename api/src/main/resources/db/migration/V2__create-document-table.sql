CREATE TABLE documents (
    id UUID PRIMARY KEY,
    owner_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    status SMALLINT NOT NULL,
    url VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES users (id)
);

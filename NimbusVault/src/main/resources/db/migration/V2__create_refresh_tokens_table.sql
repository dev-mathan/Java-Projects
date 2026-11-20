
CREATE TABLE refresh_tokens (
    id UUID PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    user_id UUID NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    revoked BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP,

    CONSTRAINT fk_refresh_token_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);

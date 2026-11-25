CREATE TABLE user_auth_providers (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    provider VARCHAR(20) NOT NULL,
    provider_id VARCHAR(255) NOT NULL,
    created_at TIMESTAMP,

    CONSTRAINT fk_user_auth_providers_user
        FOREIGN KEY (user_id)
        REFERENCES users(id),

    CONSTRAINT uk_user_auth_provider
        UNIQUE (provider, provider_id)
);
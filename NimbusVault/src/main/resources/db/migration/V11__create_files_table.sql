
CREATE TABLE files (
    id UUID PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    object_name VARCHAR(255) NOT NULL UNIQUE,
    content_type VARCHAR(255) NOT NULL,
    size BIGINT NOT NULL,
    user_id UUID NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_files_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);

CREATE INDEX idx_files_user_id ON files(user_id);

CREATE TABLE google_link_otps (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    google_id VARCHAR(255) NOT NULL,
    otp VARCHAR(10) NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    used BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP,

    CONSTRAINT fk_google_link_otp_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);
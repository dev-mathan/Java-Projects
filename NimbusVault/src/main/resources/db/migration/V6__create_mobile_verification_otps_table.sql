CREATE TABLE mobile_verification_otps (
    id UUID PRIMARY KEY,
    otp VARCHAR(6) NOT NULL,
    user_id UUID NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    used BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP,

    CONSTRAINT fk_mobile_verification_otp_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);
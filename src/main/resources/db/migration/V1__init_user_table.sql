CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    created_at BIGINT NOT NULL,
    updated_at BIGINT NOT NULL,

    user_name VARCHAR(50) NOT NULL UNIQUE,
    full_name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    email_verified BOOLEAN NOT NULL,

    phone VARCHAR(20) UNIQUE,
    phone_verified BOOLEAN NOT NULL,

    locked BOOLEAN NOT NULL,
    locked_until BIGINT
);

CREATE TABLE user_credentials (
    created_at BIGINT NOT NULL,
    updated_at BIGINT NOT NULL,

    user_id BIGINT NOT NULL REFERENCES users(id),
    type VARCHAR(50) NOT NULL,

    secret_data TEXT,
    salt VARCHAR(100),
    credential_data TEXT,

    PRIMARY KEY (user_id, type)
);




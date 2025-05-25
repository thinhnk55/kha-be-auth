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
    locked_until BIGINT,

    metadata JSONB
);

-- GIN index hỗ trợ tìm kiếm ILIKE cho full_name
CREATE INDEX idx_trgm__users__full_name
ON users
USING gin (full_name gin_trgm_ops);
-- GIN index hỗ trợ tìm kiếm ILIKE cho email
CREATE INDEX IF NOT EXISTS idx_trgm__users__email
ON users
USING gin (email gin_trgm_ops);

-- GIN index hỗ trợ tìm kiếm ILIKE cho phone
CREATE INDEX IF NOT EXISTS idx_trgm__users__phone
ON users
USING gin (phone gin_trgm_ops);



CREATE TABLE user_credentials (
    created_at BIGINT NOT NULL,
    updated_at BIGINT NOT NULL,

    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    type VARCHAR(50) NOT NULL, -- Ví dụ: PASSWORD | OTP | GOOGLE .v.v.

    secret_data TEXT,
    salt VARCHAR(100),
    credential_data TEXT,

    PRIMARY KEY (user_id, type)
);




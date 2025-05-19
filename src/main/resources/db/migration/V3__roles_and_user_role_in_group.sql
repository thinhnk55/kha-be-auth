CREATE TABLE IF NOT EXISTS roles (
    id BIGSERIAL PRIMARY KEY,
    created_at BIGINT NOT NULL,
    updated_at BIGINT NOT NULL,

    code VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    metadata JSONB
);

CREATE TABLE IF NOT EXISTS user_role_in_group (
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    group_id BIGINT NOT NULL REFERENCES groups(id) ON DELETE CASCADE,
    assigned_at BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id, group_id)
);

CREATE INDEX IF NOT EXISTS idx_user_role_in_group_group_id ON user_role_in_group(group_id);
CREATE INDEX IF NOT EXISTS idx_user_role_in_group_user_id ON user_role_in_group(user_id);
CREATE INDEX IF NOT EXISTS idx_user_role_in_group_role_id ON user_role_in_group(role_id);
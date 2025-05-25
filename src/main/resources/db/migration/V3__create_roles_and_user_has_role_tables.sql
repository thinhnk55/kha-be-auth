CREATE TABLE IF NOT EXISTS roles (
    id BIGSERIAL PRIMARY KEY,
    created_at BIGINT NOT NULL,
    updated_at BIGINT NOT NULL,

    group_id BIGINT NOT NULL REFERENCES groups(id) ON DELETE RESTRICT,
    code VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    metadata JSONB,

    CONSTRAINT uq__roles_group__group_id__code
        UNIQUE (group_id, code)
);

CREATE TABLE IF NOT EXISTS user_has_role (
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE RESTRICT,
    assigned_at BIGINT NOT NULL,
    PRIMARY KEY(user_id, role_id)
);

CREATE INDEX IF NOT EXISTS idx__user_has_role__role_id ON user_has_role(role_id);
CREATE INDEX IF NOT EXISTS idx__user_has_role__user_id ON user_has_role(user_id);
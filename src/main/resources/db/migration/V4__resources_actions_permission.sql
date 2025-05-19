CREATE TABLE IF NOT EXISTS actions (
    id BIGSERIAL PRIMARY KEY,
    created_at BIGINT NOT NULL,
    updated_at BIGINT NOT NULL,

    code VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    metadata JSONB
);

CREATE TABLE IF NOT EXISTS resources (
    id BIGSERIAL PRIMARY KEY,
    created_at BIGINT NOT NULL,
    updated_at BIGINT NOT NULL,
    code VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    metadata JSONB
);

CREATE TABLE IF NOT EXISTS resource_has_action (
    resource_id  BIGINT NOT NULL REFERENCES resources(id) ON DELETE CASCADE,
    action_id    BIGINT NOT NULL REFERENCES actions(id) ON DELETE RESTRICT,
    PRIMARY KEY(resource_id, action_id)
);

CREATE TABLE IF NOT EXISTS permissions (
    id BIGSERIAL PRIMARY KEY,
    created_at BIGINT NOT NULL,
    updated_at BIGINT NOT NULL,

    role_id      BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    group_id     BIGINT NOT NULL REFERENCES groups(id) ON DELETE CASCADE,
    resource_id  BIGINT NOT NULL REFERENCES resources(id) ON DELETE CASCADE,
    action_id    BIGINT NOT NULL REFERENCES actions(id) ON DELETE RESTRICT,

    CONSTRAINT uq_permissions UNIQUE (role_id, group_id, resource_id, action_id)
);

CREATE INDEX IF NOT EXISTS idx_permissions_resource_action
    ON permissions (resource_id, action_id);
CREATE INDEX IF NOT EXISTS idx_permissions_role_group
    ON permissions (role_id, group_id);


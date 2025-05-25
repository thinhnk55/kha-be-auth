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

    role_id      BIGINT NOT NULL REFERENCES roles(id) ON DELETE RESTRICT,
    resource_id  BIGINT NOT NULL REFERENCES resources(id) ON DELETE RESTRICT,
    action_id    BIGINT NOT NULL REFERENCES actions(id) ON DELETE RESTRICT,

    CONSTRAINT uq__permissions__role_id__resource_id__action_id UNIQUE (role_id, resource_id, action_id)
);

CREATE INDEX IF NOT EXISTS idx__permissions__role_id
    ON permissions (role_id);


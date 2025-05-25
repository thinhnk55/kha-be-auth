CREATE TABLE IF NOT EXISTS groups (
    id BIGSERIAL PRIMARY KEY,
    created_at BIGINT NOT NULL,
    updated_at BIGINT NOT NULL,

    parent_id BIGINT,
    code VARCHAR(255) NOT NULL,
    name TEXT,
    metadata JSONB,
    CONSTRAINT fk_groups_parent
        FOREIGN KEY (parent_id)
        REFERENCES groups(id)
        ON DELETE RESTRICT,
    CONSTRAINT uq_groups_parent_code
        UNIQUE (parent_id, code)
);
-- Hỗ trợ tìm kiếm tất cả các nhóm con
CREATE INDEX IF NOT EXISTS idx__groups__parent_id ON groups(parent_id);

CREATE TABLE IF NOT EXISTS user_in_group (
    user_id BIGINT NOT NULL,
    group_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, group_id),
    CONSTRAINT fk_user_in_group_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_user_in_group_group
        FOREIGN KEY (group_id)
        REFERENCES groups(id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx__user_in_group__group_id ON user_in_group(group_id);
CREATE INDEX IF NOT EXISTS idx__user_in_group__user_id ON user_in_group(user_id);

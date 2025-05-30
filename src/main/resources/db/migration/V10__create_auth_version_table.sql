-- Create auth_version table for tracking version changes across different components
CREATE TABLE auth_version (
    code VARCHAR(50) PRIMARY KEY,
    version BIGINT NOT NULL DEFAULT 1
);

-- Add comment for documentation
COMMENT ON TABLE auth_version IS 'Stores version numbers for different auth components to enable efficient polling and caching';
COMMENT ON COLUMN auth_version.code IS 'Component identifier (e.g., policy_version, user_version)';
COMMENT ON COLUMN auth_version.version IS 'Current version number, incremented on each change'; 
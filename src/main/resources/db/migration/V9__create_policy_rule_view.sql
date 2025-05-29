-- Create view for policy rules to simplify complex queries
-- This view joins permissions, resources, and actions tables for easy access

CREATE OR REPLACE VIEW policy_rules AS
SELECT 
    p.id,
    p.role_id,
    r.code as resource_code,
    a.code as action_code
FROM permissions p
JOIN resources r ON p.resource_id = r.id
JOIN actions a ON p.action_id = a.id;

-- Create index on the underlying table for better performance
CREATE INDEX IF NOT EXISTS idx_policy_rules_resource_id 
    ON permissions (resource_id);
    
CREATE INDEX IF NOT EXISTS idx_policy_rules_role_id 
    ON permissions (role_id);

-- Comment the view
COMMENT ON VIEW policy_rules IS 'Simplified view for Casbin policy loading - contains only essential fields'; 
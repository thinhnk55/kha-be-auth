CREATE OR REPLACE VIEW user_effective_roles AS
SELECT uhr.user_id, uhr.role_id
FROM user_has_role uhr

UNION

SELECT uig.user_id, ghr.role_id
FROM user_in_group uig
JOIN group_has_role ghr ON uig.group_id = ghr.group_id;

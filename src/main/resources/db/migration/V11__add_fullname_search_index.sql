CREATE INDEX IF NOT EXISTS idx_trgm__users__full_name
ON users
USING gin (full_name public.gin_trgm_ops); 
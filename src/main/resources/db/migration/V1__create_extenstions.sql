-- Bắt buộc cài đặt pg_trgm nếu muốn tìm kiếm ILIKE
CREATE EXTENSION IF NOT EXISTS pg_trgm;
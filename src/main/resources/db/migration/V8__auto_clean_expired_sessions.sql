-- Hàm xóa session đã hết hạn
CREATE OR REPLACE FUNCTION delete_expired_sessions() RETURNS void AS $$
BEGIN
    DELETE FROM sessions WHERE expired_time < EXTRACT(EPOCH FROM now());
END;
$$ LANGUAGE plpgsql;
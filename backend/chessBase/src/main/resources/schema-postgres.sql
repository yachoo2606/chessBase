DO $$
BEGIN
    -- Replace 'your_db' with your database name
    IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'chessbase') THEN
        CREATE DATABASE 'chessbase';
    END IF;
END$$;
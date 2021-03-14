CREATE DATABASE service_level_provider;
CREATE USER service_level_provider WITH ENCRYPTED PASSWORD 'a12345';
GRANT ALL PRIVILEGES ON DATABASE service_level_provider TO service_level_provider;
CREATE DATABASE client_activity_service;
CREATE USER client_activity_service WITH ENCRYPTED PASSWORD 'a12345';
GRANT ALL PRIVILEGES ON DATABASE client_activity_service TO client_activity_service;
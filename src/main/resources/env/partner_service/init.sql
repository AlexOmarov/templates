CREATE DATABASE partner_service;
CREATE USER partner_service WITH ENCRYPTED PASSWORD 'a12345';
GRANT ALL PRIVILEGES ON DATABASE partner_service TO partner_service;
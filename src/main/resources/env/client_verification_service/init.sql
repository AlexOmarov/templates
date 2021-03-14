create extension if not exists "uuid-ossp";
CREATE DATABASE client_verification_service;
CREATE USER client_verification_service WITH ENCRYPTED PASSWORD 'client_verification_service';
GRANT ALL PRIVILEGES ON DATABASE client_verification_service TO client_verification_service;
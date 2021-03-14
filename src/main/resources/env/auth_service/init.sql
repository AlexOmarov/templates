create extension if not exists "uuid-ossp";
CREATE USER auth_service WITH ENCRYPTED PASSWORD 'auth_service';
GRANT ALL PRIVILEGES ON DATABASE auth_service TO auth_service;
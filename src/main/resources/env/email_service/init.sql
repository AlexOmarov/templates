create extension if not exists "uuid-ossp";
CREATE USER email_service WITH ENCRYPTED PASSWORD 'email_service';
GRANT ALL PRIVILEGES ON DATABASE email_service TO email_service;
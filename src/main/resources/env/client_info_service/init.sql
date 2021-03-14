create extension if not exists "uuid-ossp";
CREATE USER client_info_service WITH ENCRYPTED PASSWORD 'client_info_service';
GRANT ALL PRIVILEGES ON DATABASE client_info_service TO client_info_service;
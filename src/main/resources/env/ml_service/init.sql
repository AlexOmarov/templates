CREATE USER IF NOT EXISTS ml_service IDENTIFIED BY 'ml_service';
GRANT ALL ON ml_service.* TO ml_service;

CREATE TABLE if not exists ml_service.history
(
    service LowCardinality(String),
    client LowCardinality(String),
    startDate DateTime,
    id UUID
)
ENGINE = MergeTree
ORDER BY service
SETTINGS index_granularity = 8192

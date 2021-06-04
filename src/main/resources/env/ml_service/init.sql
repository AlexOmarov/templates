CREATE USER IF NOT EXISTS ml_service IDENTIFIED BY 'ml_service';
GRANT ALL ON ml_service.* TO ml_service;

CREATE TABLE if not exists ml_service.history
(
    service UUID,
    client UUID,
    updateTime DateTime,
    updateType LowCardinality(String),
    id UUID
)
ENGINE = MergeTree
ORDER BY service
SETTINGS index_granularity = 8192;

/* Model vectors */
CREATE TABLE if not exists service_vector (
    id UUID,
    service UUID,
    updateDate DateTime,
    version UInt64,
    clusters Map(String, UInt64)
)
ENGINE = MergeTree
ORDER BY id
SETTINGS index_granularity = 8192;

CREATE TABLE if not exists client_vector (
    id UUID,
    client UUID,
    updateDate DateTime,
    version UInt64,
    clusters Map(String, UInt64)
)
ENGINE = MergeTree
ORDER BY id
SETTINGS index_granularity = 8192;

CREATE TABLE if not exists markov (
    id UUID,
    updateDate DateTime,
    version UInt64,
    cluster UInt64,
    value Map(UInt64, String)
)
ENGINE = MergeTree
ORDER BY id
SETTINGS index_granularity = 8192;

/* Actual clients and services */
CREATE TABLE if not exists client (
    id UUID,
    code String(512)
)
ENGINE = MergeTree
ORDER BY id
SETTINGS index_granularity = 8192;

CREATE TABLE if not exists service (
    id UUID,
    code String(512)
)
ENGINE = MergeTree
ORDER BY id
SETTINGS index_granularity = 8192;


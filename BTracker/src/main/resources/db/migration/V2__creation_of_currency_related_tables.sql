CREATE TABLE IF NOT EXISTS currency
(
    id                   SERIAL PRIMARY KEY,
    name                 VARCHAR(50) UNIQUE NOT NULL,
    symbol               VARCHAR(7) UNIQUE NOT NULL,
    current_price        NUMERIC(8, 2) NOT NULL,
    created_time         TIMESTAMP NOT NULL DEFAULT clock_timestamp(),
    updated_time         TIMESTAMP NOT NULL DEFAULT clock_timestamp()
    );

CREATE TABLE IF NOT EXISTS unsupported_currency
(
    id                   SERIAL PRIMARY KEY,
    name                 VARCHAR(50) UNIQUE NOT NULL,
    symbol               VARCHAR(7) UNIQUE NOT NULL
    );
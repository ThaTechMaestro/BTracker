CREATE TABLE IF NOT EXISTS alert
(
    id                   SERIAL PRIMARY KEY,
    currency_symbol      VARCHAR(7) NOT NULL,
    status               VARCHAR(20) NOT NULL,
    targeted_price       NUMERIC(8, 2) NOT NULL,
    user_id              INT NOT NULL,
    created_time         TIMESTAMP NOT NULL DEFAULT clock_timestamp(),
    updated_time         TIMESTAMP NOT NULL DEFAULT clock_timestamp(),
    CONSTRAINT user_id_fkey FOREIGN KEY (user_id) REFERENCES users(user_id)
);
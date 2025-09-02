CREATE TABLE subscription (
    subscription_id BIGSERIAL PRIMARY KEY,
    icao_code VARCHAR(4) NOT NULL UNIQUE
);
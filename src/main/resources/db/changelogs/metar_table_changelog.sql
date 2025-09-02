CREATE TABLE metar (
    metar_id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    subscription_id BIGINT NOT NULL,
    data TEXT NOT NULL,
    CONSTRAINT fk_metar_subscription FOREIGN KEY (subscription_id)
        REFERENCES subscription(subscription_id) ON DELETE CASCADE
);
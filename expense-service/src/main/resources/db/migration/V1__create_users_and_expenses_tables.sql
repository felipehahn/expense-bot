CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    telegram_id BIGINT NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE expenses (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    description VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_expenses_user FOREIGN KEY (user_id) REFERENCES users (id)
);

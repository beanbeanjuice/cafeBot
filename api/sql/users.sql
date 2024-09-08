CREATE TABLE IF NOT EXISTS users (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_snowflake_id BIGINT NOT NULL,
    balance DOUBLE NOT NULL DEFAULT 0,
    UNIQUE (user_snowflake_id),
    PRIMARY KEY (id)
);

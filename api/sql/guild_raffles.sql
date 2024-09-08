CREATE TABLE IF NOT EXISTS guild_raffles (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    guild_id INT UNSIGNED NOT NULL,
    channel_snowflake_id BIGINT NOT NULL,
    message_snowflake_id BIGINT NOT NULL,
    winner_amount INT UNSIGNED NOT NULL,
    starting_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    duration INT UNSIGNED NOT NULL,
    ending_timestamp TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (guild_id) REFERENCES guilds(id) ON DELETE CASCADE
);

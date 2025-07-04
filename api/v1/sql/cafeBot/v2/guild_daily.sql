CREATE TABLE IF NOT EXISTS guild_daily (
    guild_id INT UNSIGNED NOT NULL,
    channel_snowflake_id BIGINT NOT NULL,
    PRIMARY KEY (guild_id),
    UNIQUE (guild_id, channel_snowflake_id),
    FOREIGN KEY (guild_id) REFERENCES guilds(id) ON DELETE CASCADE
);

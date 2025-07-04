CREATE TABLE IF NOT EXISTS guild_updates (
    guild_id INT UNSIGNED NOT NULL,
    channel_snowflake_id BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (guild_id),
    FOREIGN KEY (guild_id) REFERENCES guilds(id) ON DELETE CASCADE
);

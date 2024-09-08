CREATE TABLE IF NOT EXISTS guild_twitch_settings (
    guild_id INT UNSIGNED NOT NULL,
    channel_snowflake_id BIGINT NOT NULL,
    role_snowflake_id BIGINT NOT NULL,
    PRIMARY KEY (guild_id),
    UNIQUE (guild_id),
    FOREIGN KEY (guild_id) REFERENCES guilds(id) ON DELETE CASCADE
);

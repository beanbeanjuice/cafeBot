CREATE TABLE IF NOT EXISTS guild_voice_binds (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    guild_id INT UNSIGNED NOT NULL,
    role_snowflake_id BIGINT NOT NULL,
    channel_snowflake_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (guild_id, role_snowflake_id, channel_snowflake_id),
    FOREIGN KEY (guild_id) REFERENCES guilds(id) ON DELETE CASCADE
);

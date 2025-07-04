CREATE TABLE IF NOT EXISTS guild_counting (
    guild_id INT UNSIGNED NOT NULL,
    channel_snowflake_id BIGINT,
    failure_role_snowflake_id BIGINT,
    highest_count INT UNSIGNED NOT NULL DEFAULT 0,
    current_count INT UNSIGNED NOT NULL DEFAULT 0,
    last_user_id INT UNSIGNED NOT NULL,
    PRIMARY KEY (guild_id),
    UNIQUE (guild_id),
    FOREIGN KEY (guild_id) REFERENCES guilds(id) ON DELETE CASCADE,
    FOREIGN KEY (last_user_id) REFERENCES users(id)
);

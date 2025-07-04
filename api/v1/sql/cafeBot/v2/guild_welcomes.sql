CREATE TABLE IF NOT EXISTS guild_welcomes (
    guild_id INT UNSIGNED NOT NULL,
    channel_snowflake_id BIGINT NOT NULL,
    description TEXT,
    thumbnail_url TEXT,
    image_url TEXT,
    message TEXT,
    PRIMARY KEY (guild_id),
    UNIQUE (guild_id),
    FOREIGN KEY (guild_id) REFERENCES guilds(id) ON DELETE CASCADE
);

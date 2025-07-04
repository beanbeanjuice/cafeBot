CREATE TABLE IF NOT EXISTS guild_twitch_channels (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    guild_id INT UNSIGNED NOT NULL,
    channel_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (guild_id, channel_name),
    FOREIGN KEY (guild_id) REFERENCES guilds(id) ON DELETE CASCADE
);

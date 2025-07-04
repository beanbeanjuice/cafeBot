CREATE TABLE IF NOT EXISTS user_mutual_guilds (
    user_id INT UNSIGNED NOT NULL,
    guild_id INT UNSIGNED NOT NULL,
    PRIMARY KEY (user_id),
    UNIQUE (user_id, guild_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (guild_id) REFERENCES guilds(id) ON DELETE CASCADE
);

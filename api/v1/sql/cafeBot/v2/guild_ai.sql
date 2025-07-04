CREATE TABLE IF NOT EXISTS guild_ai (
    guild_id INT UNSIGNED NOT NULL,
    use_dumb_ai TINYINT DEFAULT 1,
    open_ai_thread_id TEXT,
    PRIMARY KEY (guild_id),
    UNIQUE (guild_id),
    FOREIGN KEY (guild_id) REFERENCES guilds(id) ON DELETE CASCADE
);

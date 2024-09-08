CREATE TABLE guild_ai_threads(
    guild_id BIGINT NOT NULL,
    thread_id BIGINT NOT NULL,
    UNIQUE(guild_id),
    UNIQUE(thread_id),
    PRIMARY KEY (guild_id)
);

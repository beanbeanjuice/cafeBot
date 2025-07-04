CREATE TABLE mutual_guilds (
    user_id BIGINT NOT NULL,
    guild_id BIGINT NOT NULL,
    unique(user_id, guild_id)
);

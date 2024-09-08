CREATE TABLE IF NOT EXISTS guilds (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    guild_snowflake_id BIGINT NOT NULL,
    unique (guild_snowflake_id),
    PRIMARY KEY (id)
);

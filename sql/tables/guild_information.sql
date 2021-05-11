CREATE TABLE guild_information
(guild_id BIGINT,
prefix TEXT,
moderator_role_id BIGINT DEFAULT 0,
twitch_channel_id BIGINT DEFAULT 0,
muted_role_id BIGINT DEFAULT 0,
live_notifications_role_id BIGINT DEFAULT 0,
notify_when_updated TINYINT DEFAULT 1);
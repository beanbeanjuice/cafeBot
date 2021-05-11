CREATE TABLE guild_information
(guild_id BIGINT,
prefix TEXT,
moderator_role_id BIGINT DEFAULT 0,
twitch_channel_id BIGINT DEFAULT 0,
muted_role_id BIGINT DEFAULT 0,
live_notifications_role_id BIGINT DEFAULT 0,
notify_on_update TINYINT DEFAULT 1,
bot_update_channel_id BIGINT DEFAULT 0);
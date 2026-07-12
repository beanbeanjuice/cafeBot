package com.beanbeanjuice.cafebot.utility.commands;

import net.dv8tion.jda.api.Permission;

/**
 * Metadata for Discord "message context" ("Apps" menu) commands.
 *
 * <p>This intentionally only carries the fields needed to build the {@code CommandData}
 * for Discord command registration. Event dispatch is handled by each implementation's
 * own {@link net.dv8tion.jda.api.hooks.ListenerAdapter} callback
 * ({@code onMessageContextInteraction}), so this interface has no {@code handle()} method.
 */
public interface IMessageCommand {

    String getName();

    default Permission[] getPermissions() { return new Permission[0]; }

    default boolean isNSFW() { return false; }

    /** If true, the command is available in DMs with the bot. */
    default boolean allowDM() { return true; }

    /** If true, the command is available in guilds. */
    default boolean allowGuild() { return false; }

}

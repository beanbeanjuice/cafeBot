package com.beanbeanjuice.utility.listener;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.utility.logging.LogLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.BaseGuildMessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * A {@link ListenerAdapter} used for listening for server joins/leaves.
 *
 * @author beanbeanjuice
 */
public class ServerListener extends ListenerAdapter {

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        super.onGuildJoin(event);
        BaseGuildMessageChannel channel = event.getGuild().getDefaultChannel();

        if (channel != null) {
            try {
                channel.sendMessageEmbeds(guildJoinEmbed()).queue();
            } catch (InsufficientPermissionException ignored) {}
        }

        GuildHandler.addGuild(event.getGuild());
        Bot.updateGuildPresence(); // Updates the amount of servers in the status.
        Bot.getLogger().log(this.getClass(), LogLevel.INFO, "`" + event.getGuild().getName() + "` has added me! :blush:", false, true);
    }

    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {
        super.onGuildLeave(event);
        GuildHandler.removeGuild(event.getGuild());
        Bot.updateGuildPresence(); // Updates the amount of servers in the status.
        Bot.getLogger().log(ServerListener.class, LogLevel.INFO, "`" + event.getGuild().getName() + "` has removed me... :pleading_face:", false, true);

    }

    @NotNull
    private MessageEmbed guildJoinEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("The Barista Has Arrived");
        embedBuilder.setColor(Color.pink);
        embedBuilder.setThumbnail(Bot.getBot().getSelfUser().getAvatarUrl());
        String description = """
                Thank you for inviting me! I hope I'm not too much trouble. Please make sure I have the appropriate permissions!


                For a list of command sections, type `/help`.

                For a list of commands in a section, type `/help (command section name)`!

                For help with a specific command, type `/help (command name/alias)`!


                Anyway... be sure to report any bug reports or features you want to see!""";
        embedBuilder.setDescription(description);
        return embedBuilder.build();
    }

}

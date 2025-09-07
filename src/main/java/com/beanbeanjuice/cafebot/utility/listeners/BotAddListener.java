package com.beanbeanjuice.cafebot.utility.listeners;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.unions.DefaultGuildChannelUnion;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Optional;

public class BotAddListener extends ListenerAdapter {

    private final CafeBot cafeBot;

    public BotAddListener(final CafeBot cafeBot) {
        this.cafeBot = cafeBot;
    }

    @Override
    public void onGuildJoin(final GuildJoinEvent event) {
        super.onGuildJoin(event);

        Optional<DefaultGuildChannelUnion> optionalChannel = Optional.ofNullable(event.getGuild().getDefaultChannel());

        optionalChannel.ifPresent(channel -> {
            try {

                if (channel.getType() == ChannelType.TEXT)
                    channel.asTextChannel().sendMessageEmbeds(guildJoinEmbed()).queue();

                else if (channel.getType() == ChannelType.NEWS)
                    channel.asNewsChannel().sendMessageEmbeds(guildJoinEmbed()).queue();

                cafeBot.increaseCommandsRun();
            } catch (InsufficientPermissionException ignored) {}
        });

        this.cafeBot.getLogger().log(BotAddListener.class, LogLevel.INFO, "**" + event.getGuild().getName() + "** has added me! :blush:", false, true);
    }

    private MessageEmbed guildJoinEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("The Barista Has Arrived <a:cafeBot:1119635469727191190>");
        embedBuilder.setColor(Color.pink);
        embedBuilder.setThumbnail(cafeBot.getDiscordAvatarUrl());
        String description = """
                <a:cafeBot:1119635469727191190> Thank you for inviting me! I hope I'm not too much trouble. Please make sure I have the appropriate permissions!

                For a list of commands, type **/help**!

                Also... be sure to report any bug reports or features you want to see!""";
        embedBuilder.setDescription(description);
        return embedBuilder.build();
    }

}

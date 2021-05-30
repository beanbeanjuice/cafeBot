package com.beanbeanjuice.utility.listener;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.sections.moderation.welcome.GuildWelcome;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * A listener for when someone joins the server.
 */
public class WelcomeListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        TextChannel welcomeChannel = CafeBot.getGuildHandler().getCustomGuild(event.getGuild()).getWelcomeChannel();
        if (welcomeChannel != null) {
            GuildWelcome guildWelcome = CafeBot.getWelcomeHandler().getGuildWelcome(event.getGuild().getId());
            welcomeChannel.sendMessage(getWelcomeEmbed(guildWelcome, event.getMember().getUser())).queue();
        }
    }

    @NotNull
    private String parseDescription(@NotNull String description, @NotNull User joiner) {
        description = description.replace("{user}", joiner.getAsMention());
        description = description.replace("\\n", "\n");
        return description;
    }

    @NotNull
    public MessageEmbed getWelcomeEmbed(@NotNull GuildWelcome guildWelcome, @NotNull User joiner) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription(parseDescription(guildWelcome.getDescription(), joiner));
        embedBuilder.setThumbnail(guildWelcome.getThumbnailURL());
        embedBuilder.setImage(guildWelcome.getImageURL());
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        embedBuilder.setAuthor(joiner.getAsTag(), joiner.getAvatarUrl(), joiner.getAvatarUrl());
        return embedBuilder.build();
    }

}

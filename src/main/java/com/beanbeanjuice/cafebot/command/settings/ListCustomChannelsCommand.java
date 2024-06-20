package com.beanbeanjuice.cafebot.command.settings;

import com.beanbeanjuice.cafebot.utility.command.CommandCategory;
import com.beanbeanjuice.cafebot.utility.command.ICommand;
import com.beanbeanjuice.cafebot.utility.handler.guild.CustomChannel;
import com.beanbeanjuice.cafebot.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * A command used to list all of the {@link CustomChannel CustomChannel}
 * that the {@link net.dv8tion.jda.api.entities.Guild Guild} has for the bot.
 *
 * @author beanbeanjuice
 */
public class ListCustomChannelsCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Custom Channels");
        StringBuilder descriptionBuilder = new StringBuilder();
        GuildHandler.getCustomGuild(event.getGuild()).getCustomChannelIDs().forEach((customChannel, channelID) -> {
            descriptionBuilder.append("**").append(customChannel.getName()).append("**: ");
            try {
                TextChannel channel = event.getGuild().getTextChannelById(channelID);
                descriptionBuilder.append(channel.getAsMention()).append("\n");
            } catch (NullPointerException e) {
                descriptionBuilder.append("`Not Set`\n");
            }
        });
        embedBuilder.setColor(Helper.getRandomColor());
        embedBuilder.setDescription(descriptionBuilder.toString());
        event.getHook().sendMessageEmbeds(embedBuilder.build()).queue();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "List all of the custom channels for this server that you have set on this bot.";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/list-custom-channels`";
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.SETTINGS;
    }

    @NotNull
    @Override
    public Boolean isHidden() {
        return true;
    }

}

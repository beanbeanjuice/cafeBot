package com.beanbeanjuice.command.moderation;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * A command used to list all of the {@link com.beanbeanjuice.utility.handler.guild.CustomChannel CustomChannel}
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
        Bot.getGuildHandler().getCustomGuild(event.getGuild()).getCustomChannelIDs().forEach((customChannel, channelID) -> {
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
        return CommandCategory.MODERATION;
    }

    @NotNull
    @Override
    public Boolean allowDM() {
        return false;
    }

    @NotNull
    @Override
    public Boolean isHidden() {
        return true;
    }

}

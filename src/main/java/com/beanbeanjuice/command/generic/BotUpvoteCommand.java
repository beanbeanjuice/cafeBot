package com.beanbeanjuice.command.generic;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.helper.Helper;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

public class BotUpvoteCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        event.getHook().sendMessageEmbeds(Helper.successEmbed(
                "Bot Upvote",
                """
                        If you want to show your support for the bot, please click the links below! Please click the links below

                        **Link 1**: [top.gg](https://top.gg/bot/787162619504492554)."""
        )).queue();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Upvote the bot!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/bot-upvote`";
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.GENERIC;
    }

    @NotNull
    @Override
    public Boolean allowDM() {
        return true;
    }

    @NotNull
    @Override
    public Boolean isHidden() {
        return false;
    }
}

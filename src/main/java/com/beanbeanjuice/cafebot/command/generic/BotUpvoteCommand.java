package com.beanbeanjuice.cafebot.command.generic;

import com.beanbeanjuice.cafebot.utility.command.CommandCategory;
import com.beanbeanjuice.cafebot.utility.command.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to upvote the bot.
 *
 * @author beanbeanjuice
 * @since v3.1.0
 */
public class BotUpvoteCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                "Voting List",
                "If you want to show support for the bot, click the buttons below! " +
                        "Soon, there will be a feature where you can get perks for up-voting the bot. " +
                        "Any and all support is welcome!"
        )).addActionRow(getButtons()).queue();
    }

    @NotNull
    private ArrayList<Button> getButtons() {
        ArrayList<Button> buttons = new ArrayList<>();
        buttons.add(Button.link("https://top.gg/bot/787162619504492554/vote", "Top.GG")
                .withEmoji(Emoji.fromFormatted("<a:wiggle:886217792578269236>")));
        buttons.add(Button.link("https://discordbotlist.com/bots/cafebot-8422/upvote", "Discord Bot List")
                .withEmoji(Emoji.fromFormatted("<a:wowowow:886217210010431508>")));
        return buttons;
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

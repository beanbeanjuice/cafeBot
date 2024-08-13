package com.beanbeanjuice.cafebot.commands.generic;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.ArrayList;

public class BotUpvoteCommand extends Command implements ICommand {

    public BotUpvoteCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        event.getHook().sendMessageEmbeds(Helper.successEmbed(
                "Voting List",
                """
                        Upvoting helps me serve coffee to more people! \
                        If you're enjoying my service, please consider doing so! \
                        Any and all support is welcome!
                        """
        )).addActionRow(getButtons()).queue();
    }

    private ArrayList<Button> getButtons() {
        ArrayList<Button> buttons = new ArrayList<>();

        buttons.add(Button.link("https://top.gg/bot/787162619504492554/vote", "Top.GG")
                .withEmoji(Emoji.fromFormatted("<a:wiggle:886217792578269236>")));

        buttons.add(Button.link("https://discordbotlist.com/bots/cafebot-8422/upvote", "Discord Bot List")
                .withEmoji(Emoji.fromFormatted("<a:wowowow:886217210010431508>")));

        return buttons;
    }

    @Override
    public String getName() {
        return "bot-upvote";
    }

    @Override
    public String getDescription() {
        return "Upvote the bot!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.GENERIC;
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[0];
    }

    @Override
    public boolean isEphemeral() {
        return true;
    }

    @Override
    public boolean isNSFW() {
        return false;
    }

    @Override
    public boolean allowDM() {
        return true;
    }

}

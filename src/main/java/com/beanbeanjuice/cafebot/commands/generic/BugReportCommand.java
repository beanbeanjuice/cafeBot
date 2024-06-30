package com.beanbeanjuice.cafebot.commands.generic;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class BugReportCommand extends Command implements ICommand {

    private final String BUG_REPORT_URL = "https://github.com/beanbeanjuice/cafeBot/issues/new/choose";

    public BugReportCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        event.getHook().sendMessageComponents(
                ActionRow.of(Button.link(BUG_REPORT_URL, "Bug Report").withEmoji(Emoji.fromFormatted("<:bean_moment:841922879166742529>")))
        ).queue();
    }

    @Override
    public String getName() {
        return "bug";
    }

    @Override
    public String getDescription() {
        return "Discovered a bug with me?";
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

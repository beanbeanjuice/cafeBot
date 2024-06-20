package com.beanbeanjuice.cafebot.command.generic;

import com.beanbeanjuice.cafebot.utility.command.CommandCategory;
import com.beanbeanjuice.cafebot.utility.command.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link ICommand} used for getting the link to the bug reports.
 *
 * @author beanbeanjuice
 */
public class BugReportCommand implements ICommand {

    private final String BUG_REPORT_URL = "https://github.com/beanbeanjuice/cafeBot/issues/new/choose";

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        event.getHook().sendMessageEmbeds(bugReportEmbed()).queue();
    }

    private MessageEmbed bugReportEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Bug Report", BUG_REPORT_URL);
        embedBuilder.setDescription("You can submit a [bug report](" + BUG_REPORT_URL + ") on github!");
        embedBuilder.setColor(Helper.getRandomColor());
        return embedBuilder.build();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Submit a bug report for the bot!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/bug-report`";
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
        return true;
    }

}

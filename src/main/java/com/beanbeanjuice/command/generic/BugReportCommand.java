package com.beanbeanjuice.command.generic;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

/**
 * A command used for getting the link to the bug reports.
 *
 * @author beanbeanjuice
 */
public class BugReportCommand implements ICommand {

    private final String BUG_REPORT_URL = "https://github.com/beanbeanjuice/cafeBot/issues/new?assignees=beanbeanjuice&labels=bug&template=bug-report.md&title=%5BBUG%5D+%2A%2ADESCRIBE+YOUR+ISSUE+AS+SHORT+AS+POSSIBLE+IN+THIS+BOX%2A%2A";

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(bugReportEmbed()).queue();
    }

    private MessageEmbed bugReportEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Bug Report", BUG_REPORT_URL);
        embedBuilder.setDescription("You can submit a [bug report](" + BUG_REPORT_URL + ") on github!");
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "bug-report";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("bugreport");
        arrayList.add("report-bug");
        arrayList.add("reportbug");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Submit a bug report!";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "bug-report`";
    }

    @Override
    public Usage getUsage() {
        return new Usage();
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.GENERIC;
    }
}

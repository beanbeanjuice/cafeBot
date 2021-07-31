package com.beanbeanjuice.command.generic;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import com.beanbeanjuice.utility.helper.api.GitHubReleaseHelper;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.concurrent.CompletionException;

/**
 * An {@link ICommand} used to get a specific release version from the bot.
 *
 * @author beanbeanjuice
 */
public class GetBotReleaseVersionCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        try {
            if (args.isEmpty()) {
                event.getChannel().sendMessageEmbeds(new GitHubReleaseHelper().getVersion(CafeBot.getBotVersion())).queue();
            } else {
                event.getChannel().sendMessageEmbeds(new GitHubReleaseHelper().getVersion(args.get(0))).queue();
            }
        } catch (CompletionException e) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                    "Update Not Found",
                    "There is no update corresponding to that version number..."
            )).queue();
        }
    }

    @Override
    public String getName() {
        return "get-bot-release-version";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("getbotreleaseversion");
        arrayList.add("get-version");
        arrayList.add("getversion");
        arrayList.add("get-update");
        arrayList.add("getupdate");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Gets the latest/specific update notes from the bot! Version numbers are `vX.X.X`.";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "get-update` or `" + prefix + "get-update v2.8.1`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.TEXT, "Version Number", false);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.GENERIC;
    }
}

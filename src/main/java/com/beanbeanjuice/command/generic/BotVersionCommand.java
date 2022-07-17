package com.beanbeanjuice.command.generic;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.api.GitHubReleaseHelper;
import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.helper.Helper;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.concurrent.CompletionException;

/**
 * An {@link ICommand} used to get a specific release version from the bot.
 *
 * @author beanbeanjuice
 */
public class BotVersionCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {

        String version = Bot.BOT_VERSION;
        try {
            version = event.getOption("bot_version").getAsString();
        } catch (NullPointerException ignored) {}

        try {
            event.getHook().sendMessageEmbeds(new GitHubReleaseHelper().getVersion(version)).queue();
        } catch (CompletionException e) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Update Not Found",
                    "There is no update corresponding to that version number..."
            )).queue();
        }
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Show release information for the bot!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/bot-version` or `/bot-version v1.1.1`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "bot_version", "Specific version number for the bot.", false));
        return options;
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

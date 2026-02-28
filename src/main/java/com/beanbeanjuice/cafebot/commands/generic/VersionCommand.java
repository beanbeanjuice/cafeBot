package com.beanbeanjuice.cafebot.commands.generic;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.api.GitHubVersionEndpointWrapper;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Optional;

public class VersionCommand extends Command implements ICommand {

    public VersionCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        Optional<OptionMapping> versionMapping = Optional.ofNullable(event.getOption("version"));
        String version = versionMapping.map(OptionMapping::getAsString).orElse(bot.getBotVersion());

        GitHubVersionEndpointWrapper githubVersionAPI = new GitHubVersionEndpointWrapper(bot);

        githubVersionAPI.getVersion(version)
                .thenAcceptAsync((embed) -> event.getHook().sendMessageEmbeds(embed).queue())
                .exceptionallyAsync((ignored) -> {
                    event.getHook().sendMessageEmbeds(error(version)).queue();
                    return null;
                });
    }

    private MessageEmbed error(final String version) {
        return Helper.errorEmbed(
                "Version Error",
                String.format("""
                        Error getting bot version: %s
                        """, version)
        );
    }

    @Override
    public String getName() {
        return "version";
    }

    @Override
    public String getDescriptionPath() {
        return "Get release notes for the any of the previous versions!";
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
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.STRING, "version", "The bot version you want to get release notes for.", false)
        };
    }

    @Override
    public boolean allowDM() {
        return true;
    }

}

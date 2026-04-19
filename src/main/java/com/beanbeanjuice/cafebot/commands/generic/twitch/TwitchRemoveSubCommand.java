package com.beanbeanjuice.cafebot.commands.generic.twitch;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class TwitchRemoveSubCommand extends Command implements ISubCommand {

    public TwitchRemoveSubCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        String username = event.getOption("username").getAsString();  // Should not be null.

        bot.getCafeAPI().getTwitchChannelApi().deleteChannel(event.getGuild().getId(), username)
                .thenRun(() -> {
                    String title = ctx.getUserI18n().getString("command.twitch.subcommands.remove.success.title");
                    String description = ctx.getUserI18n().getString("command.twitch.subcommands.remove.success.description");

                    event.getHook().sendMessageEmbeds(Helper.successEmbed(title, description)).queue();
                })
                .exceptionally((ex) -> {
                    String title = ctx.getUserI18n().getString("command.twitch.subcommands.remove.error.title");
                    String description = ctx.getUserI18n().getString("command.twitch.subcommands.remove.error.description");

                    event.getHook().sendMessageEmbeds(Helper.errorEmbed(title, description)).queue();

                    bot.getLogger().log(this.getClass(), LogLevel.WARN, "Error Deleting Twitch Channel: " + ex.getMessage());
                    return null;
                });
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescriptionPath() {
        return "command.twitch.subcommands.remove.description";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.STRING, "username", "command.twitch.subcommands.remove.arguments.username.description", true)
        };
    }

}

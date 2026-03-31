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

public class TwitchAddSubCommand extends Command implements ISubCommand {

    public TwitchAddSubCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        String username = event.getOption("username").getAsString();  // Should not be null.

        bot.getCafeAPI().getTwitchChannelApi().addChannel(event.getGuild().getId(), username)
                .thenRun(() -> {
                    bot.getTwitchHandler().addStream(username);

                    String title = ctx.getUserI18n().getString("command.twitch.subcommands.add.success.title");
                    String description = ctx.getUserI18n().getString("command.twitch.subcommands.add.success.description")
                            .replace("{channel}", username);

                    event.getHook().sendMessageEmbeds(Helper.successEmbed(title, description)).queue();
                })
                .exceptionally((ex) -> {
                    String title = ctx.getUserI18n().getString("command.twitch.subcommands.add.error.title");
                    String description = ctx.getUserI18n().getString("command.twitch.subcommands.add.error.description")
                            .replace("{channel}", username);

                    event.getHook().sendMessageEmbeds(Helper.errorEmbed(title, description)).queue();

                    bot.getLogger().log(this.getClass(), LogLevel.WARN, "Error Adding Twitch Channel: " + ex.getMessage());
                    return null;
                });
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getDescriptionPath() {
        return "command.twitch.subcommands.add.description";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.STRING, "username", "command.twitch.subcommands.add.arguments.username.description", true)
        };
    }

}

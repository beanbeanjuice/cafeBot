package com.beanbeanjuice.cafebot.commands.generic.twitch;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.i18n.I18N;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.List;

public class TwitchListSubCommand extends Command implements ISubCommand {

    public TwitchListSubCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        bot.getCafeAPI().getTwitchChannelApi().getChannels(event.getGuild().getId())
                .thenAccept((channels) -> event.getHook().sendMessageEmbeds(twitchChannelsEmbed(channels, ctx.getUserI18n())).queue())
                .exceptionally((ex) -> {
                    event.getHook().sendMessageEmbeds(Helper.defaultErrorEmbed(ctx.getUserI18n())).queue();

                    bot.getLogger().log(this.getClass(), LogLevel.WARN, "Error Getting Twitch Channels: " + ex.getMessage());
                    return null;
                });
    }

    private MessageEmbed twitchChannelsEmbed(List<String> channels, I18N userBundle) {
        String title = userBundle.getString("command.twitch.subcommands.list.success.title");

        String channelsString = String.join("\n", channels);
        String description = userBundle.getString("command.twitch.subcommands.list.success.description")
                .replace("{channels}", channelsString);

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(description);
        embedBuilder.setColor(Helper.getRandomColor());
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescriptionPath() {
        return "command.twitch.subcommands.list.description";
    }

}

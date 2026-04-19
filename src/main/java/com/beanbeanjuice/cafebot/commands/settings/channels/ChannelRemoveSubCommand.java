package com.beanbeanjuice.cafebot.commands.settings.channels;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomChannelType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.i18n.I18N;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Arrays;

public class ChannelRemoveSubCommand extends Command implements ISubCommand {

    public ChannelRemoveSubCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        I18N bundle = ctx.getUserI18n();
        String guildId = event.getGuild().getId();
        CustomChannelType type = CustomChannelType.valueOf(event.getOption("type").getAsString());

        this.bot.getCafeAPI().getCustomChannelApi().deleteCustomChannel(guildId, type)
                .thenAccept((ignored) -> {
                    String title = bundle.getString("command.channel.subcommand.remove.embed.success.title")
                            .replace("{type}", type.getFriendlyName());
                    String description = bundle.getString("command.channel.subcommand.remove.embed.success.description")
                            .replace("{type}", type.getFriendlyName());
                    event.getHook().sendMessageEmbeds(Helper.successEmbed(title, description)).queue();
                })
                .exceptionally((ex) -> {
                    String title = bundle.getString("command.channel.subcommand.remove.embed.error.title")
                            .replace("{type}", type.getFriendlyName());
                    event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                            title,
                            bundle.getString("command.channel.subcommand.remove.embed.error.description")
                    )).queue();

                    this.bot.getLogger().log(this.getClass(), LogLevel.WARN, String.format("Error Removing %s Channel: %s", type.getFriendlyName(), ex.getMessage()));
                    return null;
                });
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescriptionPath() {
        return "command.channel.subcommand.remove.description";
    }

    @Override
    public OptionData[] getOptions() {
        OptionData channelTypeData = new OptionData(OptionType.STRING, "type", "command.channel.subcommand.remove.arguments.type.description", true);

        Arrays.stream(CustomChannelType.values()).forEach((type) -> channelTypeData.addChoice(type.getFriendlyName(), type.name()));

        return new OptionData[] {
                channelTypeData
        };
    }

}

package com.beanbeanjuice.cafebot.commands.settings.channels;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomChannelType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.i18n.I18N;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Arrays;
import java.util.Optional;

public class ChannelSetSubCommand extends Command implements ISubCommand {

    public ChannelSetSubCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        I18N bundle = ctx.getUserI18n();
        CustomChannelType type = CustomChannelType.valueOf(event.getOption("type").getAsString());
        Optional<OptionMapping> channelMapping = Optional.ofNullable(event.getOption("channel"));
        GuildChannelUnion channel = channelMapping.map(OptionMapping::getAsChannel).orElse((GuildChannelUnion) event.getChannel());
        String guildId = event.getGuild().getId();
        String channelId = channel.getId();

        if (channel.getType() != ChannelType.TEXT) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    bundle.getString("command.channel.subcommand.set.embed.error.invalid-channel.title"),
                    bundle.getString("command.channel.subcommand.set.embed.error.invalid-channel.description")
            )).queue();
            return;
        }

        this.bot.getCafeAPI().getCustomChannelApi().setCustomChannel(guildId, type, channelId)
                .thenAccept((customChannel) -> {
                    String title = bundle.getString("command.channel.subcommand.set.embed.success.title")
                            .replace("{type}", type.getFriendlyName());
                    String description = bundle.getString("command.channel.subcommand.set.embed.success.description")
                            .replace("{channel}", channel.getAsMention())
                            .replace("{channelDescription}", type.getDescription());
                    event.getHook().sendMessageEmbeds(Helper.successEmbed(title, description)).queue();
                })
                .exceptionally((ex) -> {
                    String title = bundle.getString("command.channel.subcommand.set.embed.error.title")
                            .replace("{type}", type.getFriendlyName());
                    event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                            title,
                            bundle.getString("command.channel.subcommand.set.embed.error.description")
                    )).queue();

                    this.bot.getLogger().log(this.getClass(), LogLevel.WARN, String.format("Error Setting %s Channel: %s", type.getFriendlyName(), ex.getMessage()), true, true);
                    return null;
                });
    }

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String getDescriptionPath() {
        return "command.channel.subcommand.set.description";
    }

    @Override
    public OptionData[] getOptions() {
        OptionData channelTypeData = new OptionData(OptionType.STRING, "type", "command.channel.subcommand.set.arguments.type.description", true);

        Arrays.stream(CustomChannelType.values()).forEach((type) -> channelTypeData.addChoice(type.getFriendlyName(), type.name()));

        return new OptionData[] {
                channelTypeData,
                new OptionData(OptionType.CHANNEL, "channel", "command.channel.subcommand.set.arguments.channel.description", false).setChannelTypes(ChannelType.TEXT)
        };
    }

}

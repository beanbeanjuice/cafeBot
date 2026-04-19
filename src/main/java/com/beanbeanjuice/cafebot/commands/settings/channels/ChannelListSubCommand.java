package com.beanbeanjuice.cafebot.commands.settings.channels;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomChannelType;
import com.beanbeanjuice.cafebot.api.wrapper.type.CustomChannel;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.i18n.I18N;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletionException;

public class ChannelListSubCommand extends Command implements ISubCommand {

    public ChannelListSubCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        I18N bundle = ctx.getUserI18n();
        Guild guild = event.getGuild();

        this.bot.getCafeAPI().getCustomChannelApi().getCustomChannels(guild.getId())
                .thenAccept((customChannels) -> {
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setTitle(bundle.getString("command.channel.subcommand.list.embed.title"));
                    embedBuilder.setColor(Helper.getRandomColor());
                    embedBuilder.setFooter(bundle.getString("command.channel.subcommand.list.embed.footer"));

                    String unsetText = bundle.getString("command.channel.subcommand.list.embed.unset");

                    Arrays.stream(CustomChannelType.values()).forEach((type) -> {
                        TextChannel channel = Optional.ofNullable(customChannels.get(type))
                                .map(CustomChannel::getChannelId)
                                .map(guild::getTextChannelById)
                                .orElse(null);

                        String mention = (channel == null) ? unsetText : channel.getAsMention();

                        embedBuilder.addField(String.format("**%s** - %s", type.getFriendlyName(), mention), type.getDescription(), true);
                    });

                    event.getHook().sendMessageEmbeds(embedBuilder.build()).queue();
                }).exceptionally((ex) -> {
                    event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                            bundle.getString("command.channel.subcommand.list.embed.error.title"),
                            bundle.getString("command.channel.subcommand.list.embed.error.description")
                    )).queue();
                    bot.getLogger().log(this.getClass(), LogLevel.WARN, "Error Listing Channels: " + ex.getMessage());

                    throw new CompletionException(ex);
                });
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescriptionPath() {
        return "command.channel.subcommand.list.description";
    }

}

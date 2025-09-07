package com.beanbeanjuice.cafebot.commands.settings.channels;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomChannelType;
import com.beanbeanjuice.cafebot.api.wrapper.type.CustomChannel;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

public class ChannelListSubCommand extends Command implements ISubCommand {

    public ChannelListSubCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();

        this.bot.getCafeAPI().getCustomChannelApi().getCustomChannels(guild.getId())
                .thenAccept((customChannels) -> {
                    String description = Arrays.stream(CustomChannelType.values()).map((type) -> {
                        TextChannel channel = Optional.ofNullable(customChannels.get(type))
                                .map(CustomChannel::getChannelId)
                                .map(guild::getTextChannelById)
                                .orElse(null);

                        String mention = (channel == null) ? "*Unset*" : channel.getAsMention();

                        return String.format("**%s** - %s", type.getFriendlyName(), mention);
                    }).collect(Collectors.joining("\n"));

                    event.getHook().sendMessageEmbeds(Helper.successEmbed(
                            "Custom Channels",
                            description
                    )).queue();
                }).exceptionally((ex) -> {

                    bot.getLogger().log(this.getClass(), LogLevel.WARN, "Error Listing Channels: " + ex.getMessage());

                    throw new CompletionException(ex);
                });
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "List all custom channels for the server!";
    }

}

package com.beanbeanjuice.cafebot.commands.settings.channels;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomChannelType;
import com.beanbeanjuice.cafebot.api.wrapper.type.CustomChannel;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
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
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setTitle("Custom Channels");
                    embedBuilder.setColor(Helper.getRandomColor());
                    embedBuilder.setFooter("You can set channels with \"/channel set!\"");

                    Arrays.stream(CustomChannelType.values()).forEach((type) -> {
                        TextChannel channel = Optional.ofNullable(customChannels.get(type))
                                .map(CustomChannel::getChannelId)
                                .map(guild::getTextChannelById)
                                .orElse(null);

                        String mention = (channel == null) ? "*Unset*" : channel.getAsMention();

                        embedBuilder.addField(String.format("**%s** - %s", type.getFriendlyName(), mention), type.getDescription(), true);
                    });

                    event.getHook().sendMessageEmbeds(embedBuilder.build()).queue();
                }).exceptionally((ex) -> {
                    event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                            "Error Listing Custom Channels",
                            "For some reason I had a problem checking the list... I've notified my boss!"
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
    public String getDescription() {
        return "List all custom channels for the server!";
    }

}

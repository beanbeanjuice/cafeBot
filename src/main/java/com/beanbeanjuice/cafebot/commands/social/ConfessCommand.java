package com.beanbeanjuice.cafebot.commands.social;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomChannelType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.*;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.time.Instant;
import java.util.Optional;

public class ConfessCommand extends Command implements ICommand {

    public ConfessCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        String guildID = event.getGuild().getId();
        String message = event.getOption("message").getAsString();

        bot.getCafeAPI().getCustomChannelApi().getCustomChannel(guildID, CustomChannelType.CONFESSIONS)
                .thenAccept((customChannel) -> {
                    String channelId = customChannel.getChannelId();
                    Optional<TextChannel> channelOptional = Optional.ofNullable(event.getGuild().getChannelById(TextChannel.class, channelId));

                    channelOptional.ifPresentOrElse(
                            (channel) -> sendVent(message, channel, event),
                            () -> {
                                sendFailure(event);

                                this.bot.getLogger().logToGuild(event.getGuild(), Helper.errorEmbed(
                                        "Confession Error",
                                        "A user tried to confess, but your confessions channel is not set!"
                                ));
                            }
                    );
                })
                .exceptionally((ex) -> {
                    sendFailure(event);
                    return null;
                });
    }

    private void sendVent(final String message, final TextChannel channel, final SlashCommandInteractionEvent event) {
        channel.sendMessageEmbeds(getVentEmbed(message)).queue((confessionMessage) -> {
            bot.getConfessionHandler().addConfession(confessionMessage.getId(), event.getUser().getId());
        });

        event.getHook().sendMessageEmbeds(
                Helper.successEmbed(
                        "Confession Sent",
                        String.format("Your anonymous confession has been successfully sent to %s. Admins can still choose to ban you if your message violates their rules!", channel.getAsMention())
                )
        ).queue();
    }

    private MessageEmbed getVentEmbed(final String message) {
        return new EmbedBuilder()
                .setTitle("Anonymous Confession")
                .setColor(Helper.getRandomColor())
                .setDescription(message)
                .setTimestamp(Instant.now())
                .setFooter("Get an admin to react with 🔨 to ban the user for rule violations! (Only works for new confessions)")
                .build();
    }

    private void sendFailure(final SlashCommandInteractionEvent event) {
        event.getHook().sendMessageEmbeds(
                Helper.errorEmbed(
                        "Confession Error",
                        "This server may not have a confession channel set... You can talk to me if you'd like! <a:cafeBot:1119635469727191190>"
                )
        ).queue();
    }

    @Override
    public String getName() {
        return "confess";
    }

    @Override
    public String getDescription() {
        return "Confess something anonymously!~";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.SOCIAL;
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[] {
                Permission.MESSAGE_SEND
        };
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
    public boolean allowDM() {
        return false;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.STRING, "message", "The message you want to anonymously confess.", true)
        };
    }

}

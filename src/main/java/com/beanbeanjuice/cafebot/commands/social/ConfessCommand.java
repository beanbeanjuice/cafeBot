package com.beanbeanjuice.cafebot.commands.social;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomChannelType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.i18n.I18N;
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
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        I18N bundle = ctx.getUserI18n();
        String guildID = event.getGuild().getId();
        String message = event.getOption("message").getAsString();

        bot.getCafeAPI().getCustomChannelApi().getCustomChannel(guildID, CustomChannelType.CONFESSIONS)
                .thenAccept((customChannel) -> {
                    String channelId = customChannel.getChannelId();
                    Optional<TextChannel> channelOptional = Optional.ofNullable(event.getGuild().getChannelById(TextChannel.class, channelId));

                    channelOptional.ifPresentOrElse(
                            (channel) -> sendVent(message, channel, event, bundle),
                            () -> {
                                sendFailure(event, bundle);

                                this.bot.getLogger().logToGuild(event.getGuild(), Helper.errorEmbed(
                                        bundle.getString("command.confess.embed.guild-error.title"),
                                        bundle.getString("command.confess.embed.guild-error.description")
                                ));
                            }
                    );
                })
                .exceptionally((ex) -> {
                    sendFailure(event, bundle);
                    return null;
                });
    }

    private void sendVent(final String message, final TextChannel channel, final SlashCommandInteractionEvent event, final I18N bundle) {
        channel.sendMessageEmbeds(getVentEmbed(message, bundle)).queue((confessionMessage) -> {
            bot.getConfessionHandler().addConfession(confessionMessage.getId(), event.getUser().getId());
        });

        String sentDescription = bundle.getString("command.confess.embed.sent.description")
                .replace("{channel}", channel.getAsMention());
        event.getHook().sendMessageEmbeds(
                Helper.successEmbed(
                        bundle.getString("command.confess.embed.sent.title"),
                        sentDescription
                )
        ).queue();
    }

    private MessageEmbed getVentEmbed(final String message, final I18N bundle) {
        return new EmbedBuilder()
                .setTitle(bundle.getString("command.confess.embed.title"))
                .setColor(Helper.getRandomColor())
                .setDescription(message)
                .setTimestamp(Instant.now())
                .setFooter(bundle.getString("command.confess.embed.footer"))
                .build();
    }

    private void sendFailure(final SlashCommandInteractionEvent event, final I18N bundle) {
        event.getHook().sendMessageEmbeds(
                Helper.errorEmbed(
                        bundle.getString("command.confess.embed.error.title"),
                        bundle.getString("command.confess.embed.error.description")
                )
        ).queue();
    }

    @Override
    public String getName() {
        return "confess";
    }

    @Override
    public String getDescriptionPath() {
        return "command.confess.description";
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
                new OptionData(OptionType.STRING, "message", "command.confess.arguments.message.description", true)
        };
    }

}

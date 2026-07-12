package com.beanbeanjuice.cafebot.commands.social;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomChannelType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.i18n.I18N;
import com.beanbeanjuice.cafebot.utility.commands.*;
import com.beanbeanjuice.cafebot.utility.handlers.ConfessionEmbedBuilder;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.FileUpload;

import java.util.Optional;

public class ConfessCommand extends Command implements ICommand {

    // Discord's file-upload cap for non-boosted servers.
    private static final long MAX_ATTACHMENT_SIZE_BYTES = 25L * 1024 * 1024;

    public ConfessCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        I18N bundle = ctx.getUserI18n();
        String guildID = event.getGuild().getId();

        Optional<String> messageOptional = Optional.ofNullable(event.getOption("message")).map(OptionMapping::getAsString);
        Optional<Message.Attachment> attachmentOptional = Optional.ofNullable(event.getOption("attachment")).map(OptionMapping::getAsAttachment);

        // At least one of {message, attachment} is required.
        if (messageOptional.isEmpty() && attachmentOptional.isEmpty()) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    bundle.getString("command.confess.embed.empty-error.title"),
                    bundle.getString("command.confess.embed.empty-error.description")
            )).queue();
            return;
        }

        if (attachmentOptional.isPresent() && attachmentOptional.get().getSize() > MAX_ATTACHMENT_SIZE_BYTES) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    bundle.getString("command.confess.embed.attachment-too-large.title"),
                    bundle.getString("command.confess.embed.attachment-too-large.description")
            )).queue();
            return;
        }

        bot.getCafeAPI().getCustomChannelApi().getCustomChannel(guildID, CustomChannelType.CONFESSIONS)
                .thenAccept((customChannel) -> {
                    String channelId = customChannel.getChannelId();
                    Optional<TextChannel> channelOptional = Optional.ofNullable(event.getGuild().getChannelById(TextChannel.class, channelId));

                    channelOptional.ifPresentOrElse(
                            (channel) -> sendVent(messageOptional.orElse(null), attachmentOptional.orElse(null), channel, event, bundle),
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

    private void sendVent(final String message,
                          final Message.Attachment attachment,
                          final TextChannel channel,
                          final SlashCommandInteractionEvent event,
                          final I18N bundle) {
        MessageEmbed embed = ConfessionEmbedBuilder.build(message, attachment != null, bundle);
        String userId = event.getUser().getId();

        if (attachment == null) {
            channel.sendMessageEmbeds(embed).queue(
                    (confessionMessage) -> bot.getConfessionHandler().addConfession(confessionMessage.getId(), userId)
            );
            sendSuccess(channel, event, bundle);
            return;
        }

        // Must be re-uploaded as a real attachment
        attachment.getProxy().download().thenAccept((inputStream) -> {
            FileUpload upload = FileUpload.fromData(inputStream, attachment.getFileName());
            channel.sendMessageEmbeds(embed).addFiles(upload).queue(
                    (confessionMessage) -> bot.getConfessionHandler().addConfession(confessionMessage.getId(), userId)
            );
            sendSuccess(channel, event, bundle);
        }).exceptionally((ex) -> {
            sendFailure(event, bundle);
            return null;
        });
    }

    private void sendSuccess(final TextChannel channel, final SlashCommandInteractionEvent event, final I18N bundle) {
        String sentDescription = bundle.getString("command.confess.embed.sent.description")
                .replace("{channel}", channel.getAsMention());

        event.getHook().sendMessageEmbeds(
                Helper.successEmbed(
                        bundle.getString("command.confess.embed.sent.title"),
                        sentDescription
                )
        ).queue();
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
                new OptionData(OptionType.STRING, "message", "command.confess.arguments.message.description", false),
                new OptionData(OptionType.ATTACHMENT, "attachment", "command.confess.arguments.attachment.description", false)
        };
    }

}

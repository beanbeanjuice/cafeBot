package com.beanbeanjuice.cafebot.utility.listeners;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomChannelType;
import com.beanbeanjuice.cafebot.i18n.I18N;
import com.beanbeanjuice.cafebot.utility.commands.IMessageCommand;
import com.beanbeanjuice.cafebot.utility.handlers.ConfessionEmbedBuilder;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.FileUpload;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Adds the "Confess" message-context ("Apps" menu) command. When the user long-presses
 * a message they sent to the bot in a DM and picks "Confess", the bot shows a picker
 * of mutual guilds that have a CONFESSIONS custom channel configured. Selecting one
 * relays the DM message (text + attachments) to that guild's confession channel as a
 * normal anonymous confession. This is essentially indistinguishable from {@code /confess}.
 *
 * <p>The DM is only relayed after the explicit context-menu click and guild selection.
 */
public class ConfessionRelayListener extends ListenerAdapter implements IMessageCommand {

    private final CafeBot bot;

    private static final String COMMAND_NAME = "Confess";
    private static final String SELECT_MENU_ID_PREFIX = "cafeBot:confess:relay:";
    private static final int MAX_GUILD_OPTIONS = 25;
    private static final long MAX_ATTACHMENT_SIZE_BYTES = 25L * 1024 * 1024;

    public ConfessionRelayListener(final CafeBot bot) {
        this.bot = bot;
    }

    @Override
    public String getName() { return COMMAND_NAME; }

    @Override
    public boolean allowDM() { return true; }

    @Override
    public boolean allowGuild() { return false; }

    @Override
    public void onMessageContextInteraction(MessageContextInteractionEvent event) {
        if (!event.getName().equals(COMMAND_NAME)) return;

        I18N bundle = new I18N(event.getUserLocale().toLocale());
        event.deferReply(true).queue();

        // Only allow in DMs.
        if (event.getChannelType() != ChannelType.PRIVATE) {
            sendEphemeralError(event, bundle, "not-dm");
            return;
        }

        Message target = event.getTarget();

        // Only for YOUR DMs.
        if (target.getAuthor().getIdLong() != event.getUser().getIdLong()) {
            sendEphemeralError(event, bundle, "not-your-message");
            return;
        }

        if (target.getContentRaw().isBlank() && target.getAttachments().isEmpty()) {
            sendEphemeralError(event, bundle, "empty");
            return;
        }

        if (hasOversizedAttachment(target.getAttachments())) {
            sendEphemeralError(event, bundle, "too-large");
            return;
        }

        bot.getCafeAPI().getCustomChannelApi().getCustomChannels(CustomChannelType.CONFESSIONS)
                .thenCompose((confessionChannels) -> {
                    // Only guilds with a confessions channel configured are worth checking.
                    // this also bounds how many live membership lookups we do below.
                    List<Guild> candidates = confessionChannels.keySet().stream()
                            .map((guildId) -> bot.getShardManager().getGuildById(guildId))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());

                    return retrieveEligibleGuilds(candidates, event.getUser());
                })
                .thenAccept((eligible) -> {
                    if (eligible.isEmpty()) {
                        sendEphemeralError(event, bundle, "no-guilds");
                        return;
                    }

                    List<Guild> limited = eligible.stream().limit(MAX_GUILD_OPTIONS).collect(Collectors.toList());

                    StringSelectMenu.Builder menuBuilder = StringSelectMenu.create(SELECT_MENU_ID_PREFIX + target.getId())
                            .setPlaceholder(bundle.getString("command.confess.relay.picker.placeholder"))
                            .setMinValues(1)
                            .setMaxValues(1);

                    limited.forEach((g) -> menuBuilder.addOption(
                            Helper.shortenToLimit(g.getName(), 100), // 100 = Discord's option-label max
                            g.getId()
                    ));

                    MessageEmbed pickerEmbed = new EmbedBuilder()
                            .setTitle(bundle.getString("command.confess.relay.picker.title"))
                            .setDescription(bundle.getString("command.confess.relay.picker.description"))
                            .setColor(Helper.getRandomColor())
                            .build();

                    event.getHook().sendMessageEmbeds(pickerEmbed)
                            .addComponents(ActionRow.of(menuBuilder.build()))
                            .queue();
                })
                .exceptionally((ex) -> {
                    sendEphemeralError(event, bundle, "lookup");
                    return null;
                });
    }

    private CompletableFuture<List<Guild>> retrieveEligibleGuilds(final List<Guild> candidates, final User user) {
        List<CompletableFuture<Guild>> checks = candidates.stream()
                .map((guild) -> guild.retrieveMember(user).submit()
                        .thenApply((member) -> guild)
                        .exceptionally((ex) -> null))
                .toList();

        return CompletableFuture.allOf(checks.toArray(new CompletableFuture[0]))
                .thenApply((v) -> checks.stream()
                        .map(CompletableFuture::join)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        if (!event.getComponentId().startsWith(SELECT_MENU_ID_PREFIX)) return;

        I18N bundle = new I18N(event.getUserLocale().toLocale());
        event.deferEdit().queue();

        if (event.getChannelType() != ChannelType.PRIVATE) {
            editEphemeralError(event, bundle, "not-dm");
            return;
        }

        String messageId = event.getComponentId().substring(SELECT_MENU_ID_PREFIX.length());
        Optional<String> guildIdOptional = event.getValues().stream().findFirst();

        if (guildIdOptional.isEmpty() || messageId.isEmpty()) {
            editEphemeralError(event, bundle, "generic");
            return;
        }
        String guildId = guildIdOptional.get();

        Guild guild = bot.getShardManager().getGuildById(guildId);
        if (guild == null) {
            editEphemeralError(event, bundle, "no-access");
            return;
        }

        // membership may have changed between showing the picker and this click
        guild.retrieveMember(event.getUser()).queue(
                (member) -> handleGuildSelected(event, bundle, guild, guildId, messageId),
                (throwable) -> editEphemeralError(event, bundle, "no-access")
        );
    }

    private void handleGuildSelected(final StringSelectInteractionEvent event, final I18N bundle,
                                     final Guild guild, final String guildId, final String messageId) {
        event.getChannel().retrieveMessageById(messageId).queue((targetMessage) -> {
            if (targetMessage.getAuthor().getIdLong() != event.getUser().getIdLong()) {
                editEphemeralError(event, bundle, "not-your-message");
                return;
            }

            if (targetMessage.getContentRaw().isBlank() && targetMessage.getAttachments().isEmpty()) {
                editEphemeralError(event, bundle, "empty");
                return;
            }

            if (hasOversizedAttachment(targetMessage.getAttachments())) {
                editEphemeralError(event, bundle, "too-large");
                return;
            }

            bot.getCafeAPI().getCustomChannelApi().getCustomChannel(guildId, CustomChannelType.CONFESSIONS)
                    .thenAccept((customChannel) -> {
                        TextChannel destination = guild.getChannelById(TextChannel.class, customChannel.getChannelId());
                        if (destination == null) {
                            editEphemeralError(event, bundle, "no-access");
                            return;
                        }

                        relay(event, targetMessage, destination, bundle);
                    })
                    .exceptionally((ex) -> {
                        editEphemeralError(event, bundle, "no-access");
                        return null;
                    });
        }, (throwable) -> editEphemeralError(event, bundle, "generic"));
    }

    private void relay(final StringSelectInteractionEvent event,
                       final Message targetMessage,
                       final TextChannel destination,
                       final I18N bundle) {
        String content = targetMessage.getContentRaw();
        List<Message.Attachment> attachments = targetMessage.getAttachments();
        MessageEmbed confession = ConfessionEmbedBuilder.build(
                content.isBlank() ? null : content,
                !attachments.isEmpty(),
                bundle
        );
        String userId = event.getUser().getId();

        if (attachments.isEmpty()) {
            destination.sendMessageEmbeds(confession).queue(
                    (posted) -> {
                        bot.getConfessionHandler().addConfession(posted.getId(), userId);
                        editWithSuccess(event, destination, bundle);
                    },
                    (throwable) -> editEphemeralError(event, bundle, "generic")
            );
            return;
        }

        // Download every attachment first then re-upload as real FileUploads.
        List<CompletableFuture<FileUpload>> uploadFutures = attachments.stream()
                .map((att) -> att.getProxy().download().thenApply((is) -> FileUpload.fromData(is, att.getFileName())))
                .toList();

        CompletableFuture.allOf(uploadFutures.toArray(new CompletableFuture[0]))
                .thenAccept((v) -> {
                    List<FileUpload> uploads = new ArrayList<>(uploadFutures.size());
                    for (CompletableFuture<FileUpload> f : uploadFutures) uploads.add(f.join());

                    destination.sendMessageEmbeds(confession).addFiles(uploads).queue(
                            (posted) -> {
                                bot.getConfessionHandler().addConfession(posted.getId(), userId);
                                editWithSuccess(event, destination, bundle);
                            },
                            (throwable) -> editEphemeralError(event, bundle, "generic")
                    );
                })
                .exceptionally((ex) -> {
                    editEphemeralError(event, bundle, "generic");
                    return null;
                });
    }

    private boolean hasOversizedAttachment(final List<Message.Attachment> attachments) {
        return attachments.stream().anyMatch((att) -> att.getSize() > MAX_ATTACHMENT_SIZE_BYTES);
    }

    private void sendEphemeralError(final MessageContextInteractionEvent event, final I18N bundle, final String key) {
        event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                bundle.getString("command.confess.relay.error." + key + ".title"),
                bundle.getString("command.confess.relay.error." + key + ".description")
        )).queue();
    }

    private void editEphemeralError(final StringSelectInteractionEvent event, final I18N bundle, final String key) {
        event.getHook().editOriginalEmbeds(Helper.errorEmbed(
                bundle.getString("command.confess.relay.error." + key + ".title"),
                bundle.getString("command.confess.relay.error." + key + ".description")
        )).setComponents().queue();
    }

    private void editWithSuccess(final StringSelectInteractionEvent event, final TextChannel destination, final I18N bundle) {
        String description = bundle.getString("command.confess.embed.sent.description")
                .replace("{channel}", destination.getAsMention());
        event.getHook().editOriginalEmbeds(Helper.successEmbed(
                bundle.getString("command.confess.embed.sent.title"),
                description
        )).setComponents().queue();
    }

}

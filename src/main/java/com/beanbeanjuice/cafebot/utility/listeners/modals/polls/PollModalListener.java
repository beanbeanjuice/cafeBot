package com.beanbeanjuice.cafebot.utility.listeners.modals.polls;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomChannelType;
import com.beanbeanjuice.cafebot.api.wrapper.api.exception.ApiRequestException;
import com.beanbeanjuice.cafebot.api.wrapper.type.poll.PartialPoll;
import com.beanbeanjuice.cafebot.api.wrapper.type.poll.PollOption;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.handlers.polls.PollSessionHandler;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.components.label.Label;
import net.dv8tion.jda.api.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;
import net.dv8tion.jda.api.components.textinput.TextInput;
import net.dv8tion.jda.api.components.textinput.TextInputStyle;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import net.dv8tion.jda.api.modals.Modal;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletionException;

public class PollModalListener extends ListenerAdapter {

    // Map of userId -> PollModalSessionData
    private final CafeBot bot;

    // In the form where MODAL_ID:%s, where %s is the user id.
    public static final String MODAL_ID = "cafeBot:modal:poll:create";

    public PollModalListener(CafeBot bot) {
        this.bot = bot;
    }

    @Override
    public void onButtonInteraction(@NonNull ButtonInteractionEvent event) {
        if (!event.isFromGuild()) return;
        if (event.getUser().isBot()) return;

        String id = event.getComponentId();
        if (!id.startsWith(MODAL_ID)) return;
        id = id.replace(String.format("%s:", MODAL_ID), "");

        if (id.endsWith("submit")) {
            handleSubmit(event);
            return;
        }

        if (id.endsWith("add")) {
            event.replyModal(getNextOptionModal()).queue();
            return;
        }

        if (id.endsWith("cancel")) {
            event.replyEmbeds(Helper.errorEmbed(
                    "Poll Creation Cancelled",
                    "<:cafeBot_sad:1171726165040447518> Oh... I've gone ahead and cancelled the poll for you..."
            )).setEphemeral(true).queue();
            bot.getPollSessionHandler().closeSession(event.getGuild().getId(), event.getUser().getId());
            return;
        }
    }

    private void handleSubmit(ButtonInteractionEvent event) {
        Guild guild = event.getGuild();
        String guildId = guild.getId();
        String userId = event.getUser().getId();

        bot.getCafeAPI().getCustomChannelApi().getCustomChannel(guildId, CustomChannelType.POLL).thenAccept((customChannel) -> {
            TextChannel channel = guild.getTextChannelById(customChannel.getChannelId());
            if (channel == null) throw new IllegalStateException("Missing Poll Channel");

            channel.sendMessageEmbeds(bot.getPollSessionHandler().getPartialPollEmbed(guildId, userId)).queue((message) -> {
                String messageId = message.getId();

                PartialPoll pollToAdd = bot.getPollSessionHandler().getPollAndCloseSession(guildId, userId);

                bot.getCafeAPI().getPollApi().createPoll(guildId, messageId, pollToAdd).thenAccept((poll) -> {
                    message.editMessageEmbeds(PollSessionHandler.getPollEmbed(poll)).queue();

                    Arrays.stream(poll.getOptions()).map(PollOption::getEmoji).forEach((emoji) -> message.addReaction(Emoji.fromUnicode(emoji.get())).queue());

                    event.replyEmbeds(Helper.successEmbed(
                            "Poll Created!",
                            String.format("Hai!~ I've successfully created your poll here: %s", message.getJumpUrl())
                    )).setEphemeral(true).queue();
                }).exceptionally((ex) -> {
                    message.delete().queue();

                    if (ex.getCause() instanceof ApiRequestException apiRequestException) {
                        event.replyEmbeds(Helper.errorEmbed(
                                "Error Creating Poll",
                                String.format("I got this error when trying to create your poll: `%s`", apiRequestException.getBody().get("error"))
                        )).queue();

                        throw new CompletionException(ex);
                    }

                    event.replyEmbeds(Helper.errorEmbed(
                            "Error Creating Poll",
                            "Something went wrong... I'm really sorry.. I couldn't create your poll!"
                    )).queue();

                    bot.getLogger().log(this.getClass(), LogLevel.WARN, "Error Creating Poll: " + ex.getMessage(), true, false);
                    throw new CompletionException(ex);
                });
            });
        }).exceptionally((ex) -> {
            event.replyEmbeds(Helper.errorEmbed(
                    "No Poll Channel",
                    "I- I'm so sorry to say this but... I think you don't have a poll channel set... Try using `/channel set`!"
            )).setEphemeral(true).queue();
            throw new CompletionException(ex);
        });

    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (!event.isFromGuild()) return;
        if (event.getUser().isBot()) return;

        String id = event.getModalId();

        if (!id.startsWith(MODAL_ID)) return;
        id = id.replace(String.format("%s:", MODAL_ID), "");

        if (id.startsWith("initial")) {
            handleInitialModal(event);
            return;
        }

        if (id.startsWith("option")) {
            handleOptionModal(event);
            return;
        }
    }

    private void handleInitialModal(ModalInteractionEvent event) {
        String guildId = event.getGuild().getId();
        String userId = event.getUser().getId();

        String title = event.getValue("title").getAsString();
        Optional<String> description = Optional.ofNullable(event.getValue("description")).map(ModalMapping::getAsString);
        boolean allowMultiple = Boolean.valueOf(event.getValue("allow-multiple").getAsStringList().getFirst());

        bot.getPollSessionHandler().addInitialData(guildId, userId, title, description.orElse(null), allowMultiple);

        event.reply("Poll details saved. Add poll option:")
                .addEmbeds(bot.getPollSessionHandler().getPartialPollEmbed(guildId, userId))
                .setEphemeral(true)
                .addComponents(
                        ActionRow.of(
                                Button.secondary(String.format("%s:button:add", MODAL_ID), "Add Option"),
                                Button.danger(String.format("%s:button:cancel", MODAL_ID), "Cancel")
                        )
                )
                .queue();
    }

    private void handleOptionModal(ModalInteractionEvent event) {
        String guildId = event.getGuild().getId();
        String userId = event.getUser().getId();

        String title = event.getValue("title").getAsString();
        Optional<String> description = Optional.ofNullable(event.getValue("description"))
                .map(ModalMapping::getAsString)
                .map((str) -> {
                    if (str.isBlank()) return null;
                    return str;
                });
        Optional<String> emoji = Optional.ofNullable(event.getValue("emoji"))
                .map(ModalMapping::getAsString)
                .map((str) -> {
                    if (str.isBlank()) return null;
                    return str;
                });

        PartialPoll existingPoll = bot.getPollSessionHandler().getPoll(guildId, userId);

        if (emoji.isPresent() && (existingPoll.getExistingEmojis().contains(emoji.get()) || !Helper.isSingleEmoji(emoji.get()))) {
            event.reply("**Invalid emoji**. Please use a **unique** and **valid** *unicode* emoji.")
                    .addEmbeds(bot.getPollSessionHandler().getPartialPollEmbed(guildId, userId))
                    .setEphemeral(true)
                    .addComponents(
                            ActionRow.of(
                                    Button.secondary(String.format("%s:button:add", MODAL_ID), "Add Option"),
                                    Button.primary(String.format("%s:button:submit", MODAL_ID), "Submit Poll"),
                                    Button.danger(String.format("%s:button:cancel", MODAL_ID), "Cancel")
                            )
                    )
                    .queue();
            return;
        }

        bot.getPollSessionHandler().addOption(guildId, userId, emoji.orElse(null), title, description.orElse(null));

        event.reply("Poll details saved. Add poll option:")
                .addEmbeds(bot.getPollSessionHandler().getPartialPollEmbed(guildId, userId))
                .setEphemeral(true)
                .addComponents(
                        ActionRow.of(
                                Button.primary(String.format("%s:button:submit", MODAL_ID), "Submit Poll"),
                                Button.secondary(String.format("%s:button:add", MODAL_ID), "Add Option"),
                                Button.danger(String.format("%s:button:cancel", MODAL_ID), "Cancel")
                        )
                )
                .queue();
    }

    public static Modal getInitialModal(int duration) {
        TextDisplay instructions = TextDisplay.of(String.format("This poll will end %d minute(s) after it is created.", duration));
        Label title = Label.of("Title", TextInput.of("title", TextInputStyle.SHORT));
        Label description = Label.of("Description", TextInput.create("description", TextInputStyle.PARAGRAPH).setRequired(false).build());
        Label allowMultiple = Label.of("Allow Multiple Choices?", StringSelectMenu.create("allow-multiple").addOption("Yes", "true").addOption("No", "false").build());

        return Modal.create(String.format("%s:initial", PollModalListener.MODAL_ID), "Create a Poll")
                .addComponents(
                        instructions,
                        title,
                        description,
                        allowMultiple
                ).build();
    }

    public static Modal getNextOptionModal() {
        TextDisplay instructions = TextDisplay.of("Use the fields below to setup poll options.");
        Label title = Label.of("Title", TextInput.of("title", TextInputStyle.SHORT));
        Label description = Label.of("Description", TextInput.create("description", TextInputStyle.PARAGRAPH).setRequired(false).build());
        Label emoji = Label.of("Emoji", TextInput.create("emoji", TextInputStyle.SHORT).setRequired(false).build());

        return Modal.create(String.format("%s:option", MODAL_ID), "Add Poll Option")
                .addComponents(
                        instructions,
                        title,
                        description,
                        emoji
                )
                .build();
    }
}

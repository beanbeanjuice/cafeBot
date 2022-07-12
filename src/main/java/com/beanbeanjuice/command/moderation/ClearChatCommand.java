package com.beanbeanjuice.command.moderation;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.handler.guild.CustomGuild;
import com.beanbeanjuice.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

/**
 * An {@link ICommand} used to clear a {@link net.dv8tion.jda.api.entities.TextChannel TextChannel}.
 *
 * @author beanbeanjuice
 */
public class ClearChatCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        if (GuildHandler.getCustomGuild(event.getGuild()).containsTextChannelDeletingMessages(event.getTextChannel())) {
            event.getHook().sendMessageEmbeds(alreadyDeletingMessagesEmbed()).queue();
            return;
        }

        int amount = event.getOption("amount").getAsInt();

        // Send message that it is deleting, save the message and exclude it from being deleted.
        event.getHook().sendMessageEmbeds(beginDeletionEmbed(amount)).queue(e -> {

            // +1 is needed because it removes the one just sent.
            event.getChannel().getHistory().retrievePast(amount+1).queue(messages -> {
                messages.remove(e);
                startMessagesDeletions(new ArrayList<>(messages), e);
            });
        });
    }

    private MessageEmbed beginDeletionEmbed(@NotNull Integer amount) {
        return new EmbedBuilder()
                .setTitle("Starting Deletion")
                .setColor(Helper.getRandomColor())
                .setDescription("Deleting `" + amount + "` messages. This might take a while.")
                .build();
    }

    private MessageEmbed alreadyDeletingMessagesEmbed() {
        return new EmbedBuilder()
                .setTitle("Already Deleting Messages")
                .setDescription("You are already deleting messages in this channel. " +
                        "Please use this command in another channel or wait for this action to stop.")
                .setColor(Color.red)
                .build();
    }

    private void startMessagesDeletions(@NotNull ArrayList<Message> messages, @NotNull Message deletionMessage) {

        Collections.reverse(messages);

        TextChannel textChannel = deletionMessage.getTextChannel();
        int messageCount = messages.size();
        CustomGuild guild = GuildHandler.getCustomGuild(deletionMessage.getGuild());

        guild.addTextChannelToDeletingMessages(deletionMessage.getTextChannel());

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {

            int count = 0;

            @Override
            public void run() {

                Message message = messages.get(count++);

                // Go through each message and delete it individually. Ignores the error response it might get.
                message.delete().queue(null, new ErrorHandler().ignore(ErrorResponseException.class));

                // If the messages is empty, cancel it.
                if (count == messageCount) {
                    timer.cancel(); // Cancel the timer.
                    textChannel.sendMessageEmbeds(completedDeletionEmbed(messageCount)).queue(e -> {
                        try {
                            // Makes the thread sleep for 10 seconds.
                            Thread.sleep(10000);
                        } catch (InterruptedException ignored) {}

                        // Removes the current channel from channels having current deletions in it.
                        guild.removeTextChannelFromDeletingMessages(e.getTextChannel());
                        e.delete().queue();
                    });
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 50);
    }

    private MessageEmbed completedDeletionEmbed(@NotNull Integer count) {
        return new EmbedBuilder()
                .setTitle("Completed Deletion")
                .setDescription("Successfully deleted `" + count + "` messages. " +
                        "There might be a time lag for deleting messages. This message will disappear once " +
                        "all of the messages have been successfully deleted from discord.")
                .setColor(Helper.getRandomColor())
                .build();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Clear the chat!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/clearchat 99`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.INTEGER, "amount", "The number of messages to clear.", true, false)
                .setRequiredRange(1, 99));
        return options;
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.MODERATION;
    }

    @NotNull
    @Override
    public Boolean allowDM() {
        return false;
    }

    @NotNull
    @Override
    public Boolean isHidden() {
        return true;
    }

    @Nullable
    @Override
    public ArrayList<Permission> getPermissions() {
        ArrayList<Permission> permissions = new ArrayList<>();
        permissions.add(Permission.MESSAGE_MANAGE);
        return permissions;
    }

}

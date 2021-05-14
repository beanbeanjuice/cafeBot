package com.beanbeanjuice.command.moderation;

import com.beanbeanjuice.main.BeanBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import com.beanbeanjuice.utility.guild.CustomGuild;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A command used to clear chat.
 *
 * @author beanbeanjuice
 */
public class ClearChatCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        // Checking if they are a moderator.
        if (!BeanBot.getGeneralHelper().checkPermission(event.getMember(), event.getChannel(), Permission.MESSAGE_MANAGE)) {
            return;
        }

        int amount = Integer.parseInt(args.get(0));

        // Checking if they are only deleting 1 message.
        if (amount == 1) {
            event.getChannel().sendMessage(moreThanOneEmbed()).queue();
            return;
        }

        // Sees if the amount is too many.
        if (amount > 99) {
            event.getChannel().sendMessage(tooManyMessagesEmbed()).queue();
            return;
        }

        if (BeanBot.getGuildHandler().getCustomGuild(event.getGuild()).containsTextChannelDeletingMessages(event.getChannel())) {
            event.getChannel().sendMessage(alreadyDeletingMessagesEmbed()).queue();
            return;
        }

        // Send message that it is deleting, save the message and exclude it from being deleted.
        event.getChannel().sendMessage(beginDeletionEmbed(Integer.parseInt(args.get(0)))).queue(e -> {

            // +1 is needed because it removes the one just sent.
            event.getChannel().getHistory().retrievePast(amount+1).queue(messages -> {
                messages.remove(e);
                startMessagesDeletionsEmbed(amount);
                startMessagesDeletions(new ArrayList<>(messages), e);
            });
        });

    }

    private MessageEmbed alreadyDeletingMessagesEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Already Deleting Messages");
        embedBuilder.setDescription("You are already deleting messages in this channel. " +
                "Please use this command in another channel or wait for this action to stop.");
        embedBuilder.setColor(Color.red);
        return embedBuilder.build();
    }

    private MessageEmbed tooManyMessagesEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Too Many Messages");
        embedBuilder.setDescription("You must select a number between `2` and `99`.");
        embedBuilder.setColor(Color.red);
        return embedBuilder.build();
    }

    private MessageEmbed startMessagesDeletionsEmbed(@NotNull Integer messageCount) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Starting Deletion");
        embedBuilder.setColor(BeanBot.getGeneralHelper().getRandomColor());
        embedBuilder.setDescription("Starting deletion of `" + messageCount + "` messages. This may take a while...");
        return embedBuilder.build();
    }

    private void startMessagesDeletions(@NotNull ArrayList<Message> messages, @NotNull Message deletionMessage) {

        Collections.reverse(messages);

        TextChannel textChannel = deletionMessage.getTextChannel();
        int messageCount = messages.size();
        CustomGuild guild = BeanBot.getGuildHandler().getCustomGuild(deletionMessage.getGuild());

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
                    deletionMessage.delete().queue(); // Delete the deletion message.
                    textChannel.sendMessage(completedDeletionEmbed(messageCount)).queue(e -> {
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
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Completed Deletion");
        embedBuilder.setDescription("Successfully deleted `" + count + "` messages. " +
                "There might be a time lag for deleting messages. This message will disappear once " +
                "all of the messages have been successfully deleted from discord.");
        embedBuilder.setColor(BeanBot.getGeneralHelper().getRandomColor());
        return embedBuilder.build();
    }

    private MessageEmbed moreThanOneEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Error Deleting Messages");
        embedBuilder.setColor(Color.red);
        embedBuilder.setDescription("You must specify a number greater than 1.");
        return embedBuilder.build();
    }

    private MessageEmbed beginDeletionEmbed(@NotNull Integer amount) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Starting Deletion");
        embedBuilder.setColor(BeanBot.getGeneralHelper().getRandomColor());
        embedBuilder.setDescription("Deleting `" + amount + "` messages. This might take a while.");
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "clear-chat";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("clearchat");
        arrayList.add("clear");
        arrayList.add("cc");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Delete messages in a channel! This can take a while, so be wary.";
    }

    @Override
    public String exampleUsage() {
        return "`!!clearchat 20`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.NUMBER, "Amount of messages to clear.", true);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.MODERATION;
    }
}

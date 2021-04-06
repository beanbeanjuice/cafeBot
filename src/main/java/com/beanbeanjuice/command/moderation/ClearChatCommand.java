package com.beanbeanjuice.command.moderation;

import com.beanbeanjuice.main.BeanBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A command used to clear chat.
 *
 * @author beanbeanjuice
 */
public class ClearChatCommand implements ICommand {

    private Timer timer;
    private TimerTask timerTask;

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {

        if (!BeanBot.getGeneralHelper().isModerator(event.getMember(), event.getGuild(), event)) {
            return;
        }

        if (Integer.parseInt(args.get(0)) == 1) {
            event.getMessage().delete().queue();
            return;
        }

        event.getChannel().sendMessage(messageEmbed(args.get(0))).queue(e -> {
            timer = new Timer();
            timerTask = new TimerTask() {

                int messageAmount = Integer.parseInt(args.get(0));

                @Override
                public void run() {

                    MessageHistory history = new MessageHistory(event.getChannel());
                    List<Message> msgs;

                    while (messageAmount > 100) {
                        msgs = history.retrievePast(100).complete();
                        msgs.remove(e);

                        if (msgs.isEmpty()) {
                            timer.cancel();
                            messageAmount = 0;
                            break;
                        }

                        event.getChannel().deleteMessages(msgs).queue();
                        messageAmount -= 100;
                        try {
                            Thread.sleep(30000);
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                    }

                    if (messageAmount > 1) {
                        msgs = history.retrievePast(messageAmount).complete();
                        msgs.remove(e);
                        event.getChannel().deleteMessages(msgs).queue();
                    }

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    e.delete().queue();
                    timer.cancel();
                }
            };

            timer.schedule(timerTask, 0);
        });

    }

    private MessageEmbed messageEmbed(String amount) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Deleting Messages");
        embedBuilder.setDescription("Deleting `" + amount + "` messages... this may take a while...");
        embedBuilder.setColor(BeanBot.getGeneralHelper().getRandomColor());
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
        return "Delete a chats in a channel!";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.NUMBER, "Amount of chats to clear.", true);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.MODERATION;
    }
}

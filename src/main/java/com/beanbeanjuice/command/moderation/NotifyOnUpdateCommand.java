package com.beanbeanjuice.command.moderation;

import com.beanbeanjuice.main.BeanBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * A command used to update the notify_on_update parameter in the MySQL database for the {@link net.dv8tion.jda.api.entities.Guild Guild}.
 *
 * @author beanbeanjuice
 */
public class NotifyOnUpdateCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {

        if (!BeanBot.getGeneralHelper().isAdministrator(event.getMember(), event)) {
            return;
        }

        // For when there are no arguments.
        if (args.isEmpty()) {
            boolean currentState = BeanBot.getGuildHandler().getCustomGuild(event.getGuild()).getNotifyOnUpdate();
            event.getChannel().sendMessage(currentNotificationStateEmbed(currentState)).queue();
            return;
        }

        // For when they are enabling it.
        if (args.get(0).equalsIgnoreCase("enable")) {
            if (BeanBot.getGuildHandler().getCustomGuild(event.getGuild()).setNotifyOnUpdate(true)) {
                event.getChannel().sendMessage(enableEmbed()).queue();
            } else {
                errorMessage(event);
            }
            return;
        }

        // For when they are disabling it.
        if (args.get(0).equalsIgnoreCase("disable")) {
            if (BeanBot.getGuildHandler().getCustomGuild(event.getGuild()).setNotifyOnUpdate(false)) {
                event.getChannel().sendMessage(disableEmbed()).queue();
            } else {
                errorMessage(event);
            }
            return;
        }

        // If the argument provided is invalid.
        event.getChannel().sendMessage(BeanBot.getGeneralHelper().errorEmbed(
                "Invalid Arguments.",
                "For this command, you don't have to add arguments. However, if you do, " +
                        "please make sure to specify the arguments `enable` or `disable` otherwise it will not work."
        )).queue();

    }

    private MessageEmbed disableEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Bot Update Notification Status");
        embedBuilder.setDescription("Successfully disabled the bot update notification status! " +
                "You will no longer be receiving notifications when the bot is updated :( but you can re-enable them at any time!");
        embedBuilder.setColor(BeanBot.getGeneralHelper().getRandomColor());
        return embedBuilder.build();
    }

    private MessageEmbed enableEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Bot Update Notification Status");
        embedBuilder.setDescription("Successfully enabled the bot update notification status! " +
                "You will now receive notifications when the bot is updated!");
        embedBuilder.setColor(BeanBot.getGeneralHelper().getRandomColor());
        return embedBuilder.build();
    }

    private void errorMessage(GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(BeanBot.getGeneralHelper().errorEmbed(
                "Unable to Update Bot Notifications",
                "There was an error updating the bot notification state. Please " +
                        "try again later."
        )).queue();
    }

    private MessageEmbed currentNotificationStateEmbed(@NotNull Boolean currentState) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Bot Update Notification Status");
        embedBuilder.setDescription("The current notification status for bot updates is " +
                "currently set to `" + currentState.toString() + "`.");
        embedBuilder.setColor(BeanBot.getGeneralHelper().getRandomColor());
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "notify-on-update";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("notifyonupdate");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Check to see if you are being notified for updates to me! " +
                "You can set also use the additional parameters `enable` and `disable`!";
    }

    @Override
    public String exampleUsage() {
        return "`!!notify-on-update disable`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.TEXT, "enable/disable", false);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.MODERATION;
    }
}

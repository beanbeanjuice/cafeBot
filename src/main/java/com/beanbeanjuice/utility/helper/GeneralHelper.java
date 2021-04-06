package com.beanbeanjuice.utility.helper;

import com.beanbeanjuice.main.BeanBot;
import com.beanbeanjuice.utility.guild.CustomGuild;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Random;

/**
 * A general class used for everything.
 *
 * @author beanbeanjuice
 */
public class GeneralHelper {

    /**
     * @return A random {@link Color}.
     */
    @NotNull
    public Color getRandomColor() {
        Random random = new Random();

        float r = random.nextFloat();
        float g = random.nextFloat();
        float b = random.nextFloat();

        return new Color(r, g, b);
    }

    /**
     * Gets a {@link User} from the ID.
     * @param userID The ID of the {@link User}.
     * @return The {@link User}.
     */
    @Nullable
    public User getUser(@NotNull String userID) {
        userID = userID.replace("<@!", "");
        userID = userID.replace(">", "");
        System.out.println(userID);

        return BeanBot.getJDA().getUserById(userID);
    }

    /**
     * Private message's a specified {@link User}.
     * @param user The {@link User} to be messaged.
     * @param message The contents of the message.
     */
    public void pmUser(User user, String message) {
        user.openPrivateChannel().flatMap(channel -> channel.sendMessage(message)).queue();
    }

    /**
     * Checks if a specified {@link Member} is an administrator.
     * @param member The {@link Member} to be checked.
     * @param event The {@link GuildMessageReceivedEvent} that was sent.
     * @return Whether or not the {@link Member} is an administrator.
     */
    @NotNull
    public Boolean isAdministrator(@NotNull Member member, @NotNull GuildMessageReceivedEvent event) {
        if (member.hasPermission(Permission.ADMINISTRATOR)) {
            return true;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("No Permission");
        embedBuilder.setColor(Color.red);
        embedBuilder.setDescription("You don't have permission to run this command. You must be an administrator");
        event.getChannel().sendMessage(embedBuilder.build()).queue();
        return false;
    }

    /**
     * Checks if the specified {@link Member} is an administrator or has the moderator role.
     * @param member The {@link Member} to be checked.
     * @param guild The {@link Guild} the {@link Member} is in.
     * @param event The {@link GuildMessageReceivedEvent} that was sent.
     * @return Whether or not the {@link Member} can run the command.
     */
    @NotNull
    public Boolean isModerator(@NotNull Member member, @NotNull Guild guild, @NotNull GuildMessageReceivedEvent event) {

        if (member.hasPermission(Permission.ADMINISTRATOR)) {
            return true;
        }

        CustomGuild customGuild = BeanBot.getGuildHandler().getCustomGuild(guild);

        if (customGuild.getModeratorRole() == null) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setAuthor("No Moderator Role Set");
            embedBuilder.setColor(Color.red);
            embedBuilder.setDescription("No moderator role has been set. Please check the help command for more information.");
            event.getChannel().sendMessage(embedBuilder.build()).queue();
            return false;
        }

        if (!member.getRoles().contains(customGuild.getModeratorRole())) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setAuthor("No Permission");
            embedBuilder.setColor(Color.red);
            embedBuilder.setDescription("You don't have permission to run this command.");
            event.getChannel().sendMessage(embedBuilder.build()).queue();
            return false;
        }
        return true;
    }

    /**
     * Compares a {@link Permission} for a {@link Member}.
     * @param member The {@link Member} to be checked.
     * @param channel The {@link TextChannel} the message was sent in.
     * @param permission The {@link Permission} to check for.
     * @return Whether or not the {@link Member} has the {@link Permission}.
     */
    @NotNull
    public Boolean checkPermission(@NotNull Member member, @NotNull TextChannel channel, @NotNull Permission permission) {

        if (member.hasPermission(Permission.ADMINISTRATOR)) {
            return true;
        }

        if (!member.hasPermission(permission)) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setAuthor("No Permission");
            embedBuilder.setColor(Color.red);
            embedBuilder.setDescription("You don't have the permission `" + permission.getName() + "` to run this command.");
            channel.sendMessage(embedBuilder.build()).queue();
            return false;
        }
        return true;
    }

    /**
     * @return The SQL Server Error {@link MessageEmbed}.
     */
    @NotNull
    public MessageEmbed sqlServerError() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.red);
        embedBuilder.setAuthor("Connection Error");
        embedBuilder.setDescription("The bot is unable to connect to the SQL database. Please try again later.");
        return embedBuilder.build();
    }

}
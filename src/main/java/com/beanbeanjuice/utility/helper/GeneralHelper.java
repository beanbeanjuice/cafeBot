package com.beanbeanjuice.utility.helper;

import com.beanbeanjuice.main.BeanBot;
import com.beanbeanjuice.utility.guild.CustomGuild;
import com.beanbeanjuice.utility.helper.timestamp.TimestampDifference;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.sql.Timestamp;
import java.util.Random;

/**
 * A general class used for everything.
 *
 * @author beanbeanjuice
 */
public class GeneralHelper {

    /**
     * Compare the difference in minutes between two {@link Timestamp} objects.
     * @param oldTime The old {@link Timestamp}.
     * @param currentTime The new {@link Timestamp}.
     * @param timestampDifference The {@link TimestampDifference} to choose.
     * @return The difference in minutes as a {@link Long}.
     */
    @NotNull
    public Long compareTwoTimeStamps(Timestamp oldTime, Timestamp currentTime, TimestampDifference timestampDifference) {
        long milliseconds1 = oldTime.getTime();
        long milliseconds2 = currentTime.getTime();
        long diff = milliseconds2 - milliseconds1;

        switch (timestampDifference) {
            
            case SECONDS -> {
                return diff / 1000;
            }

            case MINUTES -> {
                return diff / (60 * 1000);
            }

            case HOURS -> {
                return diff / (60 * 60 * 1000);
            }

            case DAYS -> {
                return diff / (24 * 60 * 60 * 1000);
            }

            default -> {
                return diff;
            }
        }
    }

    /**
     * Remove underscores from a {@link String}.
     * @param string The {@link String} to remove underscores from.
     * @return The new {@link String}.
     */
    @NotNull
    public String removeUnderscores(@NotNull String string) {
        return string.replaceAll("_", " ");
    }

    /**
     * Check whether or not a {@link String} is a number.
     * @param check The {@link String} to check.
     * @return Whether or not the {@link String} is a number.
     */
    @NotNull
    public Boolean isNumber(@NotNull String check) {
        try {
            Integer.parseInt(check);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

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

        return BeanBot.getJDA().getUserById(userID);
    }

    /**
     * Gets a {@link Role} from the ID.
     * @param guild The {@link Guild} that contains the {@link Role}.
     * @param roleID The ID of the {@link Role}.
     * @return The {@link Role}.
     */
    @Nullable
    public Role getRole(@NotNull Guild guild, @NotNull String roleID) {
        roleID = roleID.replace("<@&", "");
        roleID = roleID.replace(">", "");

        return guild.getRoleById(roleID);
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
        embedBuilder.setDescription("You don't have permission to run this command. You must be an administrator.");
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

    /**
     * Creates an error {@link MessageEmbed}.
     * @param title The title for the {@link MessageEmbed}.
     * @param description The description for the {@link MessageEmbed}.
     * @return The created {@link MessageEmbed}.
     */
    @NotNull
    public MessageEmbed errorEmbed(@NotNull String title, @NotNull String description) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(title);
        embedBuilder.setDescription(description);
        embedBuilder.setColor(Color.red);
        return embedBuilder.build();
    }

    /**
     * Creates a success {@link MessageEmbed}.
     * @param title The title for the {@link MessageEmbed}.
     * @param description The description for the {@link MessageEmbed}.
     * @return The creates {@link MessageEmbed}.
     */
    @NotNull
    public MessageEmbed successEmbed(@NotNull String title, @NotNull String description) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(title);
        embedBuilder.setDescription(description);
        embedBuilder.setColor(getRandomColor());
        return embedBuilder.build();
    }

}
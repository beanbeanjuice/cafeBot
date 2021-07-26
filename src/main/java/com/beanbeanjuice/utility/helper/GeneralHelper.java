package com.beanbeanjuice.utility.helper;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.guild.CustomGuild;
import com.beanbeanjuice.utility.helper.timestamp.TimestampDifference;
import com.beanbeanjuice.utility.logger.LogLevel;
import com.beanbeanjuice.utility.sql.SQLServer;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * A general class used for everything.
 *
 * @author beanbeanjuice
 */
public class GeneralHelper {

    private Timer spotifyRefreshTimer;
    private TimerTask spotifyRefreshTimerTask;

    private Timer mysqlRefreshTimer;
    private TimerTask mysqlRefreshTimerTask;

    public void startMySQLRefreshTimer() {
        mysqlRefreshTimer = new Timer();
        mysqlRefreshTimerTask = new TimerTask() {

            @SneakyThrows
            @Override
            public void run() {
                try {
                    CafeBot.getLogManager().log(this.getClass(), LogLevel.INFO, "Refreshing MySQL Connection...", true, false);
                    CafeBot.getSQLServer().getConnection().close(); // Closes the SQL Connection
                    CafeBot.getSQLServer().startConnection(); // Reopens the SQL Connection

                    // If the SQL Connection is still closed, then it must throw an sql exception.
                    if (!CafeBot.getSQLServer().checkConnection()) {
                        throw new SQLException("The connection is still closed.");
                    }

                    CafeBot.getLogManager().log(this.getClass(), LogLevel.OKAY, "Successfully refreshed the MySQL Connection!", true, false);
                } catch (SQLException e) {
                    CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Unable to Connect to the SQL Server: " + e.getMessage(), true, false);

                    CafeBot.setSQLServer(new SQLServer(CafeBot.getSQLURL(), CafeBot.getSQLPort(), CafeBot.getSQLEncrypt(), CafeBot.getSQLUsername(), CafeBot.getSQLPassword()));
                    CafeBot.getSQLServer().startConnection();

                    Thread.sleep(3000);
                    while (!CafeBot.getSQLServer().checkConnection()) {
                        CafeBot.setSQLServer(new SQLServer(CafeBot.getSQLURL(), CafeBot.getSQLPort(), CafeBot.getSQLEncrypt(), CafeBot.getSQLUsername(), CafeBot.getSQLPassword()));
                        CafeBot.getSQLServer().startConnection();
                    }
                }
            }
        };
        mysqlRefreshTimer.scheduleAtFixedRate(mysqlRefreshTimerTask, 1800000, 1800000);
    }

    /**
     * Starts the re-establishing of a Spotify Key Timer.
     */
    public void startSpotifyRefreshTimer() {
        spotifyRefreshTimer = new Timer();
        spotifyRefreshTimerTask = new TimerTask() {

            @Override
            public void run() {
                SpotifyApi spotifyApi = new SpotifyApi.Builder()
                        .setClientId(CafeBot.getSpotifyApiClientID())
                        .setClientSecret(CafeBot.getSpotifyApiClientSecret())
                        .build();

                ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();

                try {
                    final ClientCredentials clientCredentials = clientCredentialsRequest.execute();
                    spotifyApi.setAccessToken(clientCredentials.getAccessToken());
                    CafeBot.getLogManager().log(this.getClass(), LogLevel.INFO, "Spotify Access Token Expires In: " + clientCredentials.getExpiresIn() + " Seconds", true, false);
                    CafeBot.getLogManager().log(this.getClass(), LogLevel.OKAY, "Successfully connected to the Spotify API!", true, false);
                    CafeBot.setSpotifyApi(spotifyApi);
                } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
                    CafeBot.getLogManager().log(this.getClass(), LogLevel.ERROR, e.getMessage());
                }
                CafeBot.getLogManager().log(this.getClass(), LogLevel.OKAY, "Successfully established the Spotify connection!", true, false);
            }
        };
        spotifyRefreshTimer.scheduleAtFixedRate(spotifyRefreshTimerTask, 0, 1800000);
    }

    /**
     * Generates a Random Alpha-Numeric {@link String}.
     * @param n The length of the random alpha-numeric {@link String}.
     * @return The randomly generated alpha-numeric {@link String}.
     */
    @NotNull
    public String getRandomAlphaNumericString(@NotNull Integer n) {

        // Chooses a Random Character from This String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // Creates a String Builder with the specified size
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    /**
     * Creates a term {@link HashMap} with the command term as
     * a {@link String} and the argument as a {@link String}.
     * @param commandTerms The {@link ArrayList<String>} of command terms.
     * @param arguments The {@link ArrayList<String>} of arguments.
     * @return The new {@link HashMap}.
     */
    public HashMap<String, String> createCommandTermMap(@NotNull ArrayList<String> commandTerms, @NotNull ArrayList<String> arguments) {
        HashMap<String, String> mappedUnderscores = new HashMap<>();

        StringBuilder currentWord = new StringBuilder();
        String currentCommandTerm = "";
        int currentArgumentIndex = -1;

        // Goes through each argument once.
        for (String argument : arguments) {

            // Finds the index of the command term
            int termIndex = getTermIndex(commandTerms, argument);

            // If the current argument does not contain a command term,
            // then do not continue to the next command term.
            if (termIndex != -1) {
                mappedUnderscores.put(currentCommandTerm, currentWord.toString());
                currentWord = new StringBuilder();
                currentCommandTerm = commandTerms.get(termIndex);
                currentWord.append(argument.replace(currentCommandTerm + ":", ""));
            } else {
                currentWord.append(" ").append(argument);
            }
            currentArgumentIndex++;

            // Base case for when the arguments is full.
            if (currentArgumentIndex == arguments.size() - 1) {
                mappedUnderscores.put(currentCommandTerm, currentWord.toString());
            }
        }
        return mappedUnderscores;
    }

    /**
     * Checks the index of the command term.
     * @param terms The {@link ArrayList<String>} containing the command terms.
     * @param string The current {@link String} to check if they have the command term.
     * @return The {@link Integer} index of the command term. Returns -1 if it does not contain the command term.
     */
    private Integer getTermIndex(@NotNull ArrayList<String> terms, @NotNull String string) {
        for (int i = 0; i < terms.size(); i++) {
            if (string.startsWith(terms.get(i) + ":")) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Convert a {@link String} to a {@link Date}.
     * @param dateString The {@link String} to convert.
     * @return The converted {@link Date}.
     */
    @Nullable
    public Date parseDate(@NotNull String dateString) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = simpleDateFormat.parse(dateString);
            return new Date(date.getTime());
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Get a random number.
     * @param minimum The minimum {@link Integer}.
     * @param maximum The maximum {@link Integer}.
     * @return The random {@link Integer}.
     */
    @NotNull
    public Integer getRandomNumber(@NotNull Integer minimum, @NotNull Integer maximum) {
        return (int) ((Math.random() * (maximum - minimum)) + minimum);
    }

    /**
     * Formats the specified time.
     * @param timeInMillis The time as a {@link Long} value.
     * @return The formatted time {@link String}.
     */
    @NotNull
    public String formatTime(@NotNull Long timeInMillis) {
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * Formats the specified time, including days.
     * @param timeInMillis The time as a {@link Long} value.
     * @return The formatted time {@link String}.
     */
    @NotNull
    public String formatTimeDays(@NotNull Long timeInMillis) {
        final long days = timeInMillis / TimeUnit.DAYS.toMillis(1);
        final long hours = timeInMillis % TimeUnit.DAYS.toMillis(1) / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis % TimeUnit.DAYS.toMillis(1) % TimeUnit.HOURS.toMillis(1) / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.DAYS.toMillis(1) % TimeUnit.HOURS.toMillis(1) % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d:%02d", days, hours, minutes, seconds);
    }

    /**
     * Checks if a specified {@link Integer} is a double digit.
     * @param number The {@link Integer} specified.
     * @return Whether or not the {@link Integer} is a double digit.
     */
    @NotNull
    public Boolean isDoubleDigit(@NotNull Integer number) {
        return (number > 9 && number < 100) || (number < -9 && number > -100);
    }

    /**
     * Round a time to minutes or seconds.
     * @param time The time in milliseconds.
     * @param timestampDifference The timestamp difference specified.
     * @return The rounded time.
     */
    @NotNull
    public Integer roundTime(@NotNull Long time, @NotNull TimestampDifference timestampDifference) {

        if (timestampDifference.equals(TimestampDifference.MINUTES)) {
            return Math.round(((time/1000)/60));
        }
        return Math.round((time/1000)%60);
    }

    /**
     * Compare the difference in time between two {@link Timestamp} objects.
     * @param oldTime The old {@link Timestamp}.
     * @param currentTime The new {@link Timestamp}.
     * @param timestampDifference The {@link TimestampDifference} to choose.
     * @return The difference in time as a {@link Long}.
     */
    @NotNull
    public Long compareTwoTimeStamps(@NotNull Timestamp oldTime, @NotNull Timestamp currentTime, @NotNull TimestampDifference timestampDifference) {
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
        userID = userID.replace("<@", ""); // Edge Case for Mobile
        userID = userID.replace(">", "");

        try {
            return CafeBot.getJDA().getUserById(userID);
        } catch (NullPointerException | NumberFormatException e) {
            return null;
        }
    }

    /**
     * Gets a {@link TextChannel} from the ID.
     * @param guild The {@link Guild} that contains the {@link TextChannel}.
     * @param textChannelID The ID of the {@link TextChannel}.
     * @return The {@link TextChannel}.
     */
    @Nullable
    public TextChannel getTextChannel(@NotNull Guild guild, @NotNull String textChannelID) {
        textChannelID = textChannelID.replace("<#", "");
        textChannelID = textChannelID.replace(">", "");

        try {
            return guild.getTextChannelById(textChannelID);
        } catch (NullPointerException | NumberFormatException e) {
            return null;
        }
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
        roleID = roleID.replace("<@", "");
        roleID = roleID.replace(">", "");

        try {
            return guild.getRoleById(roleID);
        } catch (NullPointerException | NumberFormatException e) {
            return null;
        }
    }

    /**
     * Shorten the message to a certain limit.
     * @param message The contents of the message.
     * @param limit The limit of the new message.
     * @return The new, limited message.
     */
    @NotNull
    public String shortenToLimit(@NotNull String message, @NotNull Integer limit) {
        message = message.replace("\"", "\\\"");
        if (message.length() >= limit) {
            return message.substring(0, limit - 3) + "...";
        }

        return message;
    }

    /**
     * Private message's a specified {@link User}.
     * @param user The {@link User} to be messaged.
     * @param message The contents of the message.
     */
    public void pmUser(@NotNull User user, @NotNull String message) {
        user.openPrivateChannel().flatMap(channel -> channel.sendMessage(message)).queue();
    }

    /**
     * Private messages a specified {@link User}.
     * @param user The {@link User} to be messaged.
     * @param embed The {@link MessageEmbed} to be sent.
     */
    public void pmUser(@NotNull User user, @NotNull MessageEmbed embed) {
        user.openPrivateChannel().flatMap(channel -> channel.sendMessage(embed)).queue();
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
        embedBuilder.setTitle("No Permission");
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

        CustomGuild customGuild = CafeBot.getGuildHandler().getCustomGuild(guild);

        if (customGuild.getModeratorRole() == null) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("No Moderator Role Set");
            embedBuilder.setColor(Color.red);
            embedBuilder.setDescription("No moderator role has been set. Please check the help command for more information.");
            event.getChannel().sendMessage(embedBuilder.build()).queue();
            return false;
        }

        if (!member.getRoles().contains(customGuild.getModeratorRole())) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("No Permission");
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
            embedBuilder.setTitle("No Permission");
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
        embedBuilder.setTitle("Connection Error");
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
        embedBuilder.setTitle(title);
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
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(description);
        embedBuilder.setColor(getRandomColor());
        return embedBuilder.build();
    }

}
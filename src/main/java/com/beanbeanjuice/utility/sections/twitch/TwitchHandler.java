package com.beanbeanjuice.utility.sections.twitch;

import com.beanbeanjuice.main.CafeBot;
import com.beanbeanjuice.utility.listener.TwitchListener;
import com.beanbeanjuice.utility.logger.LogLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * A class used for handling Twitch instances.
 *
 * @author beanbeanjuice
 */
public class TwitchHandler {

    private ArrayList<String> alreadyAddedTwitchNames;
    private TwitchListener twitchListener;

    /**
     * Creates a new {@link TwitchHandler} instance.
     */
    public TwitchHandler() {
        alreadyAddedTwitchNames = new ArrayList<>();
        twitchListener = new TwitchListener();
        twitchListener.addEventHandler(new TwitchMessageEventHandler());
    }

    /**
     * Add a twitch channel name.
     * @param twitchUsername The twitch channel name specified.
     */
    public void addTwitchChannel(@NotNull String twitchUsername) {
        twitchUsername = twitchUsername.toLowerCase();
        if (!alreadyAddedTwitchNames.contains(twitchUsername)) {
            twitchListener.addStream(twitchUsername);
            alreadyAddedTwitchNames.add(twitchUsername);
        }
    }

    /**
     * Add an {@link ArrayList<String>} of twitch channel names.
     * @param twitchUsernames The {@link ArrayList<String>} specified.
     */
    public void addTwitchChannels(@NotNull ArrayList<String> twitchUsernames) {
        for (String string : twitchUsernames) {
            addTwitchChannel(string);
        }
    }

    @Nullable
    public ArrayList<String> getGuildsForChannel(@NotNull String channelName) {
        ArrayList<String> guilds = new ArrayList<>();
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "SELECT * FROM cafeBot.guild_twitch WHERE twitch_channel = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setString(1, channelName.toLowerCase());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                guilds.add(String.valueOf(resultSet.getLong(1)));
            }

            return guilds;
        } catch (SQLException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Getting Twitch Guilds: " + e.getMessage());
            return null;
        }
    }

}

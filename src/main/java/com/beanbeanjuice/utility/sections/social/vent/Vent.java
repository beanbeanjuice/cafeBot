package com.beanbeanjuice.utility.sections.social.vent;

import com.beanbeanjuice.CafeBot;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A custom {@link Vent} class for the {@link VentHandler}.
 *
 * @author beanbeanjuice
 */
public class Vent {

    private final int TIME_TO_END = 1800;
    private final String userID;
    private final String guildID;
    private Timer timer;
    private TimerTask timerTask;
    private ListenerAdapter listenerAdapter;

    /**
     * Creates a new {@link Vent} object.
     * @param userID The ID of the {@link net.dv8tion.jda.api.entities.User User} who sent the {@link Vent}.
     * @param guildID The ID of the {@link net.dv8tion.jda.api.entities.Guild Guild} to send the {@link Vent} in.
     */
    public Vent(@NotNull String userID, @NotNull String guildID) {
        this.userID = userID;
        this.guildID = guildID;
    }

    /**
     * Starts the {@link Vent} timer.
     */
    public void startVentTimer() {

        CafeBot.getGeneralHelper().pmUser(CafeBot.getJDA().getUserById(userID), CafeBot.getGeneralHelper().successEmbed(
                "Anonymous Venting",
                "To anonymously vent, simply type your message here. You have 30 minutes to type it before the timer is cancelled. " +
                        "To cancel the timer manually, do `!!cancel`. This will post in the last server where you used the `vent` command in."
        ));

        listenerAdapter = new ListenerAdapter() {
            @Override
            public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {

                // Only listen to vents from the user.
                if (!event.getAuthor().getId().equals(userID)) {
                    return;
                }

                // Stop the timer if !!cancel is used.
                if (event.getMessage().getContentRaw().startsWith("!!cancel")) {
                    CafeBot.getGeneralHelper().pmUser(event.getAuthor(), CafeBot.getGeneralHelper().successEmbed(
                            "Timer Stopped",
                            "The timer has been successfully stopped."
                    ));
                    stopVentTimer();
                    return;
                }

                TextChannel ventChannel = CafeBot.getGuildHandler().getCustomGuild(guildID).getVentingChannel();

                // Checking if the text channel is null.
                if (ventChannel == null) {
                    CafeBot.getGeneralHelper().pmUser(event.getAuthor(), CafeBot.getGeneralHelper().errorEmbed(
                            "Something Went Wrong",
                            "There was an error sending your vent. The timer has been cancelled. Please try again... " +
                                    "The venting channel may no longer exist."
                    ));
                    stopVentTimer();
                    return;
                }

                CafeBot.getVentHandler().sendVent(CafeBot.getGeneralHelper().shortenToLimit(event.getMessage().getContentRaw(), 2048), ventChannel);
                CafeBot.getGeneralHelper().pmUser(event.getAuthor(), CafeBot.getGeneralHelper().successEmbed(
                        "Vent Successfully Sent",
                        "Your vent should now show up in the server! I hope that got off of your chest!"
                ));
                stopVentTimer();
            }
        };

        CafeBot.getJDA().addEventListener(listenerAdapter);

        timer = new Timer();
        timerTask = new TimerTask() {

            int count = 0;

            @Override
            public void run() {
                count++;

                // Stop the timer after specified time.
                if (count >= TIME_TO_END) {
                    CafeBot.getGeneralHelper().pmUser(CafeBot.getJDA().getUserById(userID), CafeBot.getGeneralHelper().errorEmbed(
                            "Stopping Vent Timer",
                            "You didn't respond in time so the timer has been stopped."
                    ));
                    stopVentTimer();
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    public void stopVentTimer() {
        CafeBot.getJDA().removeEventListener(listenerAdapter);
        CafeBot.getVentHandler().removeVent(userID);
        timer.cancel();
    }

}

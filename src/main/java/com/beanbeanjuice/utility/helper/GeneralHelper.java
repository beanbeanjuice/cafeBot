package com.beanbeanjuice.utility.helper;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
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

    private final JDA jda;

    /**
     * Creates a new {@link GeneralHelper} object.
     * @param jda The {@link JDA} that has been created in the {@link com.beanbeanjuice.main.BeanBot BeanBot} class.
     */
    public GeneralHelper(JDA jda) {
        this.jda = jda;
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
        System.out.println(userID);

        return jda.getUserById(userID);
    }

    /**
     * Private message's a specified {@link User}.
     * @param user The {@link User} to be messaged.
     * @param message The contents of the message.
     */
    public void pmUser(User user, String message) {
        user.openPrivateChannel().flatMap(channel -> channel.sendMessage(message)).queue();
    }

}
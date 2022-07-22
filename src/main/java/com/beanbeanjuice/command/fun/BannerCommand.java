package com.beanbeanjuice.command.fun;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * An {@link ICommand} used to get the banner of a {@link net.dv8tion.jda.api.entities.User User}.
 *
 * @author beanbeanjuice
 * @since v3.1.0
 */
public class BannerCommand implements ICommand {

    private final String filename = "banner_image.jpg";

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        User user = event.getUser();
        if (event.getOption("user") != null)
            user = event.getOption("user").getAsUser();

        String username = user.getName();
        String avatarURL = user.getAvatarUrl();

        user.retrieveProfile().queue(
                (profile) -> {
                    if (profile.getBannerUrl() != null) {
                        event.getHook().sendMessageEmbeds(bannerEmbed(username, avatarURL, profile)).queue();
                    } else {
                        File file = new File(filename);
                        event.getHook().sendMessageEmbeds(bannerEmbed(username, avatarURL, profile))
                                .addFile(file, filename).queue((message) -> {
                                    file.delete();  // Finally delete the file once the message is sent.
                                });
                    }
        },
                (failure) -> {
                    event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                            "Error Getting Banner",
                            "There was an error getting " + username + "'s banner. " +
                                    "Please try again later."
                    )).queue();
                });
    }

    @NotNull
    private MessageEmbed bannerEmbed(@NotNull String username, @NotNull String avatarURL, @NotNull User.Profile profile) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(profile.getAccentColor())
                .setAuthor(username + "'s Banner", null, avatarURL);

        if (profile.getBannerUrl() != null) {
            embedBuilder.setImage(profile.getBannerUrl() + "?size=600");
        } else {
            int width = 600, height = 240;

            // Creating the image in code.
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = bufferedImage.createGraphics();

            // Setting the colour and filling it.
            graphics2D.setColor(profile.getAccentColor());
            graphics2D.fillRect(0, 0, width, height);

            // Clearing up resources.
            graphics2D.dispose();

            // Creating the file, and writing the file.
            File file = new File(filename);
            try {
                ImageIO.write(bufferedImage, "jpg", file);
                embedBuilder.setImage("attachment://" + filename);
            } catch (IOException ignored) { }
        }

        return embedBuilder.build();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Get your or another user's banner!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/banner` or `/banner @beanbeanjuice`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "user", "The user you want to get the banner of.", false));
        return options;
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.FUN;
    }

    @NotNull
    @Override
    public Boolean allowDM() {
        return true;
    }

}

package com.beanbeanjuice.cafebot.command.settings.welcome;

import com.beanbeanjuice.cafebot.Bot;
import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafebot.utility.command.CommandCategory;
import com.beanbeanjuice.cafebot.utility.command.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.listener.WelcomeListener;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.welcomes.GuildWelcome;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.CafeException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.ConflictException;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ISubCommand} used to edit the welcome message for the welcome
 * {@link net.dv8tion.jda.api.entities.channel.concrete.TextChannel TextChannel} for a specified {@link net.dv8tion.jda.api.entities.Guild Guild}.
 *
 * @author beanbeanjuice
 */
public class EditWelcomeMessageSubCommand implements ISubCommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        User user = event.getUser();
        String description = event.getOption("inner_message").getAsString();
        String message = null;
        String imageURL = null;
        String thumbnailURL = null;

        if (event.getOption("outer_message") != null)
            message = event.getOption("outer_message").getAsString();

        if (event.getOption("image") != null)
            imageURL = event.getOption("image").getAsAttachment().getUrl();

        if (event.getOption("thumbnail") != null)
            thumbnailURL = event.getOption("thumbnail").getAsAttachment().getUrl();

        GuildWelcome guildWelcome = new GuildWelcome(event.getGuild().getId(), description, thumbnailURL, imageURL, message);

        // Sets it in the API
        if (setGuildWelcome(guildWelcome)) {
            guildWelcome.getMessage().ifPresentOrElse(
                    (guildWelcomeMessage) -> event.getHook().sendMessage(guildWelcomeMessage).addEmbeds(WelcomeListener.getWelcomeEmbed(guildWelcome, user)).queue(),
                    () -> event.getHook().sendMessageEmbeds(WelcomeListener.getWelcomeEmbed(guildWelcome, user)).queue()
            );
            return;
        }

        event.getHook().sendMessageEmbeds(Helper.sqlServerError()).queue();
    }

    /**
     * Sets the {@link GuildWelcome} in the {@link CafeAPI CafeAPI}.
     * @param guildWelcome The {@link GuildWelcome} to set.
     * @return True, if the {@link GuildWelcome} was set successfully.
     */
    @NotNull
    public Boolean setGuildWelcome(@NotNull GuildWelcome guildWelcome) {
        try {
            return Bot.getCafeAPI().WELCOME.createGuildWelcome(guildWelcome);
        } catch (ConflictException e) {
            return Bot.getCafeAPI().WELCOME.updateGuildWelcome(guildWelcome);
        } catch (CafeException e) {
            Bot.getLogger().log(this.getClass(), LogLevel.ERROR, "Error Setting Guild Welcome: " + e.getMessage(), e);
            return false;
        }
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Edit the welcome channel message!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/welcome-channel edit-message message:@awesomerole, someone joined the server! thumbnail:https://www.fakeImageurl.png image:https://www.fakeImageUrl.png2 description:Welcome, {user} to the server!\\nYou're cool!` or `/welcome-channel edit-message description:Welcome to the server!`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "inner_message", "The inner message to send when someone joins. " +
                "You can use `\\n` and use {user}.", true));
        options.add(new OptionData(OptionType.STRING, "outer_message", "The outer message to send when someone joins. " +
                "You can use `\\n` and use {user}.", false));
        options.add(new OptionData(OptionType.ATTACHMENT, "image", "The image to show when someone joins.", false));
        options.add(new OptionData(OptionType.ATTACHMENT, "thumbnail", "The thumbnail image to show when someone joins.", false));
        return options;
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.SETTINGS;
    }

    @NotNull
    @Override
    public String getName() {
        return "edit-message";
    }

}

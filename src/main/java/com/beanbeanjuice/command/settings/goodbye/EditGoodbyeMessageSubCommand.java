package com.beanbeanjuice.command.settings.goodbye;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.cafeapi.cafebot.goodbyes.GuildGoodbye;
import com.beanbeanjuice.cafeapi.exception.api.CafeException;
import com.beanbeanjuice.cafeapi.exception.api.ConflictException;
import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ISubCommand;
import com.beanbeanjuice.utility.helper.Helper;
import com.beanbeanjuice.utility.listener.GoodbyeListener;
import com.beanbeanjuice.utility.logging.LogLevel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EditGoodbyeMessageSubCommand implements ISubCommand {

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

        GuildGoodbye guildGoodbye = new GuildGoodbye(event.getGuild().getId(), description, thumbnailURL, imageURL, message);

        // Sets it in the API
        if (setGuildGoodbye(guildGoodbye)) {
            guildGoodbye.getMessage().ifPresentOrElse(
                    (guildGoodbyeMessage) -> event.getHook().sendMessage(guildGoodbyeMessage).addEmbeds(GoodbyeListener.getGoodbyeEmbed(guildGoodbye, user)).queue(),
                    () -> event.getHook().sendMessageEmbeds(GoodbyeListener.getGoodbyeEmbed(guildGoodbye, user)).queue()
            );
            return;
        }

        event.getHook().sendMessageEmbeds(Helper.sqlServerError()).queue();
    }

    /**
     * Sets the {@link GuildGoodbye} in the {@link com.beanbeanjuice.cafeapi.CafeAPI CafeAPI}.
     * @param guildGoodbye The {@link GuildGoodbye} to set.
     * @return True, if the {@link GuildGoodbye} was set successfully.
     */
    @NotNull
    public Boolean setGuildGoodbye(@NotNull GuildGoodbye guildGoodbye) {
        try {
            return Bot.getCafeAPI().GOODBYE.createGuildGoodbye(guildGoodbye);
        } catch (ConflictException e) {
            return Bot.getCafeAPI().GOODBYE.updateGuildGoodbye(guildGoodbye);
        } catch (CafeException e) {
            Bot.getLogger().log(this.getClass(), LogLevel.ERROR, "Error Setting Guild Goodbye: " + e.getMessage(), e);
            return false;
        }
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Edit the goodbye channel message!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/goodbye-channel edit-message message:@awesomerole, someone left the server... description:Sadly, {user} left the server` or `/goodbye-channel edit-message description:Goodbye!!`";
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

package com.beanbeanjuice.cafebot.command.generic;

import com.beanbeanjuice.cafebot.utility.command.CommandCategory;
import com.beanbeanjuice.cafebot.utility.command.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link ICommand} used for sending the user a feature request link.
 *
 * @author beanbeanjuice
 */
public class FeatureRequestCommand implements ICommand {

    private final String FEATURE_REQUEST_URL = "https://github.com/beanbeanjuice/cafeBot/issues/new/choose";

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        event.getHook().sendMessageEmbeds(featureRequestEmbed()).queue();
    }

    private MessageEmbed featureRequestEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Feature Request", FEATURE_REQUEST_URL);
        embedBuilder.setDescription("You can submit a [feature request](" + FEATURE_REQUEST_URL + ") on github!");
        embedBuilder.setColor(Helper.getRandomColor());
        return embedBuilder.build();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Submit a feature request for the bot!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/feature-report`";
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.GENERIC;
    }

    @NotNull
    @Override
    public Boolean allowDM() {
        return true;
    }

    @NotNull
    @Override
    public Boolean isHidden() {
        return true;
    }

}

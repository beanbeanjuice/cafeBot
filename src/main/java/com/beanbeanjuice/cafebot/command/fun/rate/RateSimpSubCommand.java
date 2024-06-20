package com.beanbeanjuice.cafebot.command.fun.rate;

import com.beanbeanjuice.cafebot.utility.command.CommandCategory;
import com.beanbeanjuice.cafebot.utility.command.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ISubCommand} used to rate someone's simp level.
 *
 * @author beanbeanjuice
 * @since v3.1.0
 */
public class RateSimpSubCommand implements ISubCommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        User user = event.getUser();
        if (event.getOption("user") != null)
            user = event.getOption("user").getAsUser();

        event.getHook().sendMessageEmbeds(Helper.smallAuthorEmbed(
                "Simp Rating", null, user.getAvatarUrl(),
                "The simp rating is `" + Helper.getRandomNumber(0, 101) + "`%!"
        )).queue();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Rate someone's simp level!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/rate simp` or `/rate simp @beanbeanjuice`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "user", "THe person to rate!", false));
        return options;
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.FUN;
    }

    @NotNull
    @Override
    public String getName() {
        return "simp";
    }

}

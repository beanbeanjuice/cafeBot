package com.beanbeanjuice.command.fun;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.helper.Helper;
import io.github.beanbeanjuice.cafeapi.cafebot.birthdays.Birthday;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to get someone's birthday.
 *
 * @author beanbeanjuice
 */
public class GetBirthdayCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        User user = event.getUser();

        if (event.getOption("user") != null)
            user = event.getOption("user").getAsUser();

        Birthday birthday = Bot.getBirthdayHandler().getBirthday(user.getId());

        // Checks if the birthday exists.
        if (birthday == null) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Error Getting Birthday",
                    "There has either been an error getting the birthday, or the birthday has not been set."
            )).queue();
            return;
        }

        event.getHook().sendMessageEmbeds(birthdayEmbed(user, birthday)).queue();
    }

    /**
     * Creates a {@link MessageEmbed} for the {@link Birthday} for a {@link User}.
     * @param user The specified {@link User}.
     * @param birthday The {@link Birthday} for the {@link User}.
     * @return The created {@link MessageEmbed} for the {@link Birthday}.
     */
    @NotNull
    private MessageEmbed birthdayEmbed(@NotNull User user, @NotNull Birthday birthday) {
        return new EmbedBuilder()
                .setColor(Helper.getRandomColor())
                .setTitle(user.getName() + "'s Birthday")
                .setDescription("Birthday is on `" + birthday.getMonth() + ", " + birthday.getDay() + "`.")
                .build();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Get your birthday or the birthday of another user!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/get-birthday` or `/get-birthday @beanbeanjuice`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "user", "The person who's birthday you want to check.", false));
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

    @NotNull
    @Override
    public Boolean isHidden() {
        return true;
    }

}

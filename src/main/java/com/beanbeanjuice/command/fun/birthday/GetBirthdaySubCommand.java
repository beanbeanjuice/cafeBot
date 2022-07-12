package com.beanbeanjuice.command.fun.birthday;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ISubCommand;
import com.beanbeanjuice.utility.helper.Helper;
import com.beanbeanjuice.utility.section.fun.BirthdayHandler;
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
 * An {@link ISubCommand} used to get someone's birthday.
 *
 * @author beanbeanjuice
 */
public class GetBirthdaySubCommand implements ISubCommand {

    /**
     * 
     * @param event The {@link SlashCommandInteractionEvent}.
     */
    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        User user = event.getUser();
        boolean self = true;

        if (event.getOption("user") != null) {
            user = event.getOption("user").getAsUser();
            self = false;
        }

        Birthday birthday = BirthdayHandler.getBirthday(user.getId());

        // Checks if the birthday exists.
        if (birthday == null) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Error Getting Birthday",
                    "There has either been an error getting the birthday, or the birthday has not been set."
            )).queue();
            return;
        }

        if (self) {
            event.getHook().sendMessageEmbeds(selfBirthdayEmbed(birthday)).queue();
            return;
        }
        event.getHook().sendMessageEmbeds(birthdayEmbed(user, birthday)).queue();
    }

    @NotNull
    private MessageEmbed selfBirthdayEmbed(@NotNull Birthday birthday) {
        return new EmbedBuilder()
                .setColor(Helper.getRandomColor())
                .setTitle("Your Birthday")
                .setDescription("Your birthday is on `" + birthday.getMonth() + ", " + birthday.getDay() + "`. " +
                        "The timezone specified is `" + birthday.getTimeZone().getID() + "`.")
                .build();
    }

    @NotNull
    private MessageEmbed birthdayEmbed(@NotNull User user, @NotNull Birthday birthday) {
        return new EmbedBuilder()
                .setColor(Helper.getRandomColor())
                .setTitle(user.getName() + "'s Birthday")
                .setDescription("Their birthday is on `" + birthday.getMonth() + ", " + birthday.getDay() + "`. " +
                        "The timezone specified is `" + birthday.getTimeZone().getID() + "`.")
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

    @NotNull
    @Override
    public String getName() {
        return "get";
    }
}

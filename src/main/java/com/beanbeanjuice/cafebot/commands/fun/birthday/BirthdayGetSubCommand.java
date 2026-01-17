package com.beanbeanjuice.cafebot.commands.fun.birthday;

import com.beanbeanjuice.cafebot.api.wrapper.api.exception.ApiRequestException;
import com.beanbeanjuice.cafebot.api.wrapper.type.Birthday;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.apache.hc.core5.http.HttpStatus;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Optional;

public class BirthdayGetSubCommand extends Command implements ISubCommand {

    public BirthdayGetSubCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        Optional<OptionMapping> userMapping = Optional.ofNullable(event.getOption("user"));

        User user = userMapping.map(OptionMapping::getAsUser).orElse(event.getUser());
        boolean isSelf = userMapping.isEmpty();

        bot.getCafeAPI().getBirthdayApi().getBirthday(user.getId())
                        .thenAccept((birthday) -> {
                            sendBirthday(user, isSelf, birthday, event);
                        })
                        .exceptionally((ex) -> {
                            if (ex.getCause() instanceof ApiRequestException apiRequestException) {

                                if (apiRequestException.getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                                    sendError(event);
                                }

                                return null;
                            }

                            event.getHook().sendMessageEmbeds(Helper.defaultErrorEmbed()).queue();
                            bot.getLogger().log(this.getClass(), LogLevel.WARN, "Error Getting Birthday: " + ex.getMessage());

                            return null;
                        });
    }

    private void sendError(SlashCommandInteractionEvent event) {
        event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                "🎂 Error Getting Birthday",
                "<:cafeBot_sad:1171726165040447518> Sorry... there was an error getting their birthday. It might not be set, you should tell them to set it with `/birthday`!."
        )).queue();
    }

    private void sendBirthday(User user, boolean isSelf, Birthday birthday, SlashCommandInteractionEvent event) {
        MessageEmbed embed = (isSelf) ? selfBirthdayEmbed(birthday) : birthdayEmbed(user, birthday);
        event.getHook().sendMessageEmbeds(embed).queue();
    }

    private MessageEmbed selfBirthdayEmbed(Birthday birthday) {
        return new EmbedBuilder()
                .setColor(Helper.getRandomColor())
                .setTitle("🎂 Your Birthday")
                .setDescription(
                        String.format(
                                "Your birthday is on **%s %d** (%s).",
                                Month.of(birthday.getMonth()).getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                                birthday.getDay(),
                                birthday.getTimeZone().getId()
                        )
                )
                .build();
    }

    private MessageEmbed birthdayEmbed(User user, Birthday birthday) {
        return new EmbedBuilder()
                .setColor(Helper.getRandomColor())
                .setTitle("🎂 " + user.getName() + "'s Birthday")
                .setDescription(
                        String.format("Their birthday is on **%s %d** (%s).",
                                Month.of(birthday.getMonth()).getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                                birthday.getDay(),
                                birthday.getTimeZone().getId()
                        )
                )
                .build();
    }

    @Override
    public String getName() {
        return "get";
    }

    @Override
    public String getDescription() {
        return "Get someone's birthday!";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "The person you want to get the birthday of!", false)
        };
    }

}

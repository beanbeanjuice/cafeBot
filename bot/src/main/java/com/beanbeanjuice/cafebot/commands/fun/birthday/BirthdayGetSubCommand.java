package com.beanbeanjuice.cafebot.commands.fun.birthday;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.birthdays.Birthday;
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

        cafeBot.getCafeAPI().getBirthdaysEndpoint().getUserBirthday(user.getId())
                .thenAcceptAsync((birthdayOptional) -> birthdayOptional.ifPresent((birthday) -> sendBirthday(user, isSelf, birthday, event)))
                .exceptionallyAsync((e) -> {
                    cafeBot.getLogger().log(BirthdayGetSubCommand.class, LogLevel.DEBUG, "Error: " + e.getMessage(), e);
                    sendError(event);
                    return null;
                });
    }

    private void sendError(final SlashCommandInteractionEvent event) {
        event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                "Error Getting Birthday",
                "Sorry... there was an error getting their birthday. It might not be set."
        )).queue();
    }

    private void sendBirthday(final User user, final boolean isSelf, final Birthday birthday, final SlashCommandInteractionEvent event) {
        MessageEmbed embed = (isSelf) ? selfBirthdayEmbed(birthday) : birthdayEmbed(user, birthday);
        event.getHook().sendMessageEmbeds(embed).queue();
    }

    private MessageEmbed selfBirthdayEmbed(final Birthday birthday) {
        return new EmbedBuilder()
                .setColor(Helper.getRandomColor())
                .setTitle("Your Birthday")
                .setDescription(String.format(
                        """
                        Your birthday is on **%s %d**. The timezone specified is *%s*.
                        """, birthday.getMonth(), birthday.getDay(), birthday.getTimeZone().getID()))
                .build();
    }

    private MessageEmbed birthdayEmbed(final User user, final Birthday birthday) {
        return new EmbedBuilder()
                .setColor(Helper.getRandomColor())
                .setTitle(user.getName() + "'s Birthday")
                .setDescription(String.format(
                        """
                        Their birthday is on **%s %d**. The timezone specified is *%s*.
                        """, birthday.getMonth(), birthday.getDay(), birthday.getTimeZone().getID()))
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

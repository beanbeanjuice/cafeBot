package com.beanbeanjuice.cafebot.commands.fun.birthday;

import com.beanbeanjuice.cafebot.api.wrapper.api.exception.ApiRequestException;
import com.beanbeanjuice.cafebot.api.wrapper.type.Birthday;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
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
import java.util.ResourceBundle;
import java.util.concurrent.CompletionException;

public class BirthdayGetSubCommand extends Command implements ISubCommand {

    public BirthdayGetSubCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        Optional<OptionMapping> userMapping = Optional.ofNullable(event.getOption("user"));

        User user = userMapping.map(OptionMapping::getAsUser).orElse(event.getUser());
        boolean isSelf = userMapping.isEmpty();

        bot.getCafeAPI().getBirthdayApi().getBirthday(user.getId())
                .thenAccept((birthday) -> {
                    sendBirthday(user, isSelf, birthday, event, ctx);
                })
                .exceptionally((ex) -> {
                    handleError(ex, event, ctx);
                    throw new CompletionException(ex.getCause());
                });
    }

    private void handleError(Throwable ex, SlashCommandInteractionEvent event, CommandContext ctx) {
        if (ex.getCause() instanceof ApiRequestException apiRequestException) {

            if (apiRequestException.getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                sendError(event, ctx.getUserI18n());
                return;
            }

        }

        event.getHook().sendMessageEmbeds(Helper.uncaughtErrorEmbed(ctx.getUserI18n(), ex.getMessage())).queue();
        bot.getLogger().log(this.getClass(), LogLevel.WARN, "Error Getting Birthday: " + ex.getMessage());
    }

    private void sendError(SlashCommandInteractionEvent event, ResourceBundle i18n) {
        event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                i18n.getString("command.birthday.subcommand.get.embed.error.title"),
                i18n.getString("command.birthday.subcommand.get.embed.error.description")
        )).queue();
    }

    private void sendBirthday(User user, boolean isSelf, Birthday birthday, SlashCommandInteractionEvent event, CommandContext ctx) {
        MessageEmbed embed = (isSelf) ? selfBirthdayEmbed(birthday, ctx.getUserI18n()) : birthdayEmbed(user, birthday, ctx.getUserI18n());
        event.getHook().sendMessageEmbeds(embed).queue();
    }

    private MessageEmbed selfBirthdayEmbed(Birthday birthday, ResourceBundle i18n) {
        String description = i18n.getString("command.birthday.subcommand.get.embed.self.description")
                .replace("{month}", Month.of(birthday.getMonth()).getDisplayName(TextStyle.FULL, Locale.ENGLISH))
                .replace("{day}", String.valueOf(birthday.getDay()))
                .replace("{timezone}", birthday.getTimeZone().getId());

        return new EmbedBuilder()
                .setColor(Helper.getRandomColor())
                .setTitle(i18n.getString("command.birthday.subcommand.get.embed.self.title"))
                .setDescription(description)
                .build();
    }

    private MessageEmbed birthdayEmbed(User user, Birthday birthday, ResourceBundle i18n) {
        String title = i18n.getString("command.birthday.subcommand.get.embed.other.title")
                .replace("{user}", user.getName());

        String description = i18n.getString("command.birthday.subcommand.get.embed.other.description")
                .replace("{month}", Month.of(birthday.getMonth()).getDisplayName(TextStyle.FULL, Locale.ENGLISH))
                .replace("{day}", String.valueOf(birthday.getDay()))
                .replace("{timezone}", birthday.getTimeZone().getId());

        return new EmbedBuilder()
                .setColor(Helper.getRandomColor())
                .setTitle(title)
                .setDescription(description)
                .build();
    }

    @Override
    public String getName() {
        return "get";
    }

    @Override
    public String getDescriptionPath() {
        return "command.birthday.subcommand.get.description";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "command.birthday.subcommand.get.arguments.user.description", false)
        };
    }

}

package com.beanbeanjuice.cafebot.commands.cafe;

import com.beanbeanjuice.cafebot.api.wrapper.api.exception.ApiRequestException;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.Nullable;
import tools.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CompletionException;

public class ServeCommand extends Command implements ICommand {

    public ServeCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        String word = event.getOption("word").getAsString();  // ! - Shouldn't be null.
        User user = event.getUser();
        Optional<OptionMapping> optionalReceiver = Optional.ofNullable(event.getOption("customer"));

        bot.getCafeAPI().getServeWordsApi().serveWord(user.getId(), word).thenAcceptAsync((cafeUser) -> {
            float reward = cafeUser.getReward();
            float newBalance = cafeUser.getNewBalance();

            User receiver = optionalReceiver.map(OptionMapping::getAsUser).orElse(null);

            MessageEmbed embed = serveEmbed(user, receiver, word, reward, newBalance, ctx.getGuildI18n());

            event.getHook().sendMessageEmbeds(embed).queue();
        }).exceptionallyAsync((e) -> {
            handleError(e, event, ctx);
            throw new CompletionException(e.getCause());
        });

    }

    private void handleError(Throwable e, SlashCommandInteractionEvent event, CommandContext ctx) {
        if (e.getCause() instanceof ApiRequestException apiRequestException) {
            JsonNode body = apiRequestException.getBody();

            if (body.has("lastServeTime")) {
                Instant lastServeTime = Instant.parse(body.get("lastServeTime").asString());
                event.getHook().sendMessageEmbeds(cannotServeEmbed(lastServeTime, ctx.getUserI18n())).queue();
                return;
            }

            if (body.has("error") && body.get("error").has("word")) {
                event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                        ctx.getUserI18n().getString("command.serve.embed.error.word.title"),
                        ctx.getUserI18n().getString("command.serve.embed.error.word.description")
                )).queue();
                return;
            }
        }

        event.getHook().sendMessageEmbeds(Helper.uncaughtErrorEmbed(ctx.getUserI18n(), e.getMessage())).queue();
        bot.getLogger().log(this.getClass(), LogLevel.WARN, "Error Serving Word: " + e.getMessage(), true, true);
    }

    private MessageEmbed cannotServeEmbed(Instant lastServeTime, ResourceBundle i18n) {
        String description = i18n.getString("command.serve.embed.error.time.description")
                .replace("{time}", String.valueOf(lastServeTime.getEpochSecond()));

        return Helper.errorEmbed(
                i18n.getString("command.serve.embed.error.time.title"),
                description
        );
    }

    private MessageEmbed serveEmbed(User user, @Nullable User receiver, String word, float reward, float newBalance, ResourceBundle i18n) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle(i18n.getString("command.serve.embed.success.title"));

        String description = i18n.getString("command.serve.embed.success.description");
        if (receiver != null) {
            description = i18n.getString("command.serve.embed.success.description_other")
                    .replace("{customer}", receiver.getAsMention());
        }

        description = description
                .replace("{user}", user.getAsMention())
                .replace("{word}", word)
                .replace("{balance}", String.valueOf(newBalance))
                .replace("{reward}", Float.toString(reward));

        embedBuilder.setDescription(description);
        embedBuilder.setFooter(i18n.getString("command.serve.embed.success.footer"));
        embedBuilder.setColor(Helper.getRandomColor());

        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "serve";
    }

    @Override
    public String getDescriptionPath() {
        return "command.serve.description";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.CAFE;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.STRING, "word", "command.serve.arguments.word.description", true),
                new OptionData(OptionType.MENTIONABLE, "customer", "command.serve.arguments.customer.description", false)
        };
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[0];
    }

    @Override
    public boolean isEphemeral() {
        return false;
    }

    @Override
    public boolean isNSFW() {
        return false;
    }

    @Override
    public boolean allowDM() {
        return true;
    }

}

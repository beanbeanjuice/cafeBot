package com.beanbeanjuice.cafebot.commands.cafe;

import com.beanbeanjuice.cafebot.api.wrapper.api.exception.ApiRequestException;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import tools.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.Optional;

public class ServeCommand extends Command implements ICommand {

    public ServeCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        String word = event.getOption("word").getAsString();  // ! - Shouldn't be null.
        User user = event.getUser();
        Optional<OptionMapping> optionalReceiver = Optional.ofNullable(event.getOption("customer"));

        bot.getCafeAPI().getServeWordsApi().serveWord(user.getId(), word).thenAcceptAsync((cafeUser) -> {
            float reward = cafeUser.getReward();
            float newBalance = cafeUser.getNewBalance();

            MessageEmbed embed = optionalReceiver
                    .map(OptionMapping::getAsUser)
                    .map((receiver) -> serveOtherEmbed(user, receiver, word, reward, newBalance))
                    .orElse(serveSelfEmbed(user, word, reward, newBalance));

            event.getHook().sendMessageEmbeds(embed).queue();
        }).exceptionallyAsync((e) -> {
            if (e.getCause() instanceof ApiRequestException apiRequestException) {
                JsonNode body = apiRequestException.getBody();

                if (body.has("lastServeTime")) {
                    Instant lastServeTime = Instant.parse(body.get("lastServeTime").asString());
                    event.getHook().sendMessageEmbeds(cannotServeEmbed(lastServeTime)).queue();
                    return null;
                }

                if (body.has("error") && body.get("error").has("word")) {
                    event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                            "There was an error serving!",
                            String.format("Please make sure to use a real, single, **non-banned** english word: %s", body.get("error").get("word").get(0).asString())
                    )).queue();
                    return null;
                }
                return null;
            }

            event.getHook().sendMessageEmbeds(Helper.defaultErrorEmbed()).queue();
            bot.getLogger().log(this.getClass(), LogLevel.WARN, "Error Serving Word: " + e.getMessage(), true, true);

            return null;
        });

    }

    private MessageEmbed cannotServeEmbed(Instant lastServeTime) {
        return Helper.errorEmbed(
                "Cannot Serve",
                String.format("""
                        You served <t:%s:R>.
                        You need to wait an hour before you last served in order to serve again...
                        """, lastServeTime.getEpochSecond())
        );
    }

    private MessageEmbed serveSelfEmbed(User user, String word, float reward, float newBalance) {
        return Helper.successEmbed(
                "Order Up!",
                String.format("""
                        %s has served %s for **%.2f bC**! They now have **%.2f cC**!
                        """, user.getAsMention(), word, reward, newBalance)
        );
    }

    private MessageEmbed serveOtherEmbed(User sender, User receiver, String word, double reward, float newBalance) {
        return Helper.successEmbed(
                "Order Up!",
                String.format("""
                        %s has served %s to %s for **%.2f cC**! They now have **%.2f cC**!
                        """, sender.getAsMention(), receiver.getAsMention(), word, reward, newBalance)
        );
    }

    @Override
    public String getName() {
        return "serve";
    }

    @Override
    public String getDescription() {
        return "Serve some words to customers to earn some bC (beanCoins)!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.CAFE;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.STRING, "word", "Any english word!", true),
                new OptionData(OptionType.MENTIONABLE, "customer", "The customer you want to serve the word to.", false)
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

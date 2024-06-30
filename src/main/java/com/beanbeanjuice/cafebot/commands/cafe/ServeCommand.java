package com.beanbeanjuice.cafebot.commands.cafe;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.cafe.CafeType;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.cafe.CafeUser;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.words.Word;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class ServeCommand extends Command implements ICommand {

    private final int SERVE_WAIT_TIME = 15;

    public ServeCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        String word = event.getOption("word").getAsString();  // Shouldn't be null.
        User user = event.getUser();

        cafeBot.getCafeAPI().getCafeUsersEndpoint().getAndCreateCafeUser(user.getId()).thenAcceptAsync((cafeUser) -> {
            Timestamp lastServeTime = cafeUser.getLastServingTime().orElse(new Timestamp(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(SERVE_WAIT_TIME + 1)));
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());

            int timeElapsed = (int) TimeUnit.MILLISECONDS.toSeconds(Math.abs(currentTime.getTime() - lastServeTime.getTime()));
            if (timeElapsed <= TimeUnit.MINUTES.toSeconds(SERVE_WAIT_TIME)) {
                event.getHook().sendMessageEmbeds(cannotServeEmbed(timeElapsed)).queue();
                return;
            }

            handleServe(event, cafeUser, word);
        });
    }

    private void handleServe(final SlashCommandInteractionEvent event, final CafeUser user, final String word) {
        cafeBot.getCafeAPI().getWordsEndpoint().getWord(word)
                .thenAcceptAsync((serveWord) -> {
                    double amount = getCoinsFromWord(serveWord);
                    double newBalance = user.getBeanCoins() + amount;
                    Timestamp lastServingTime = new Timestamp(System.currentTimeMillis());

                    cafeBot.getCafeAPI().getCafeUsersEndpoint().updateCafeUser(user.getUserID(), CafeType.BEAN_COINS, newBalance);
                    cafeBot.getCafeAPI().getCafeUsersEndpoint().updateCafeUser(user.getUserID(), CafeType.LAST_SERVING_TIME, lastServingTime);
                    cafeBot.getCafeAPI().getWordsEndpoint().updateWord(word, serveWord.getUses() + 1);

                    Optional<OptionMapping> receiverMapping = Optional.ofNullable(event.getOption("user"));
                    receiverMapping.map(OptionMapping::getAsUser)
                            .ifPresentOrElse(
                                    (receiver) -> event.getHook().sendMessageEmbeds(serveOtherEmbed(event.getUser(), receiver, word, amount)).queue(),
                                    () -> event.getHook().sendMessageEmbeds(serveSelfEmbed(event.getUser(), word, amount)).queue()
                            );
                })
                .exceptionallyAsync((e) -> {
                    event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                            "Error Serving",
                            String.format("There was an error serving **%s**. Is it a real word?", word)
                    )).queue();
                    return null;
                });
    }

    private double getCoinsFromWord(final Word serveWord) {
        String word = serveWord.getWord();
        int uses = serveWord.getUses();
        double lengthMultiplier = Helper.getRandomDouble(0, 5);
        double usesMultiplier = Helper.getRandomDouble(0, 3);

        return Math.max((word.length() * lengthMultiplier) - (uses * usesMultiplier), 15);
    }

    private MessageEmbed cannotServeEmbed(final int timeElapsed) {
        int timeLeft = (int) (TimeUnit.MINUTES.toSeconds(SERVE_WAIT_TIME) - timeElapsed);
        String secondsString = (timeLeft == 1) ? "second" : "seconds";

        return Helper.errorEmbed(
                "Cannot Serve",
                String.format("""
                        You need to wait **%d** %s before you can serve again...
                        """, timeLeft, secondsString)
        );
    }

    private MessageEmbed serveSelfEmbed(final User user, final String word, final double amount) {
        return Helper.successEmbed(
                "Order Up!",
                String.format("""
                        %s has served %s for **%.2f bC**!
                        """, user.getAsMention(), word, amount)
        );
    }

    private MessageEmbed serveOtherEmbed(final User sender, final User receiver, final String word, final double amount) {
        return Helper.successEmbed(
                "Order Up!",
                String.format("""
                        %s has served %s to %s for **%.2f bC**!
                        """, sender.getAsMention(), receiver.getAsMention(), word, amount)
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
        return true;
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

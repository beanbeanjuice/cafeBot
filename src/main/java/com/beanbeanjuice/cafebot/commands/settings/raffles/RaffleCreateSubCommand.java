package com.beanbeanjuice.cafebot.commands.settings.raffles;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomChannelType;
import com.beanbeanjuice.cafebot.api.wrapper.api.exception.ApiRequestException;
import com.beanbeanjuice.cafebot.api.wrapper.type.Raffle;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class RaffleCreateSubCommand extends Command implements ISubCommand {

    public RaffleCreateSubCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        String guildId = event.getGuild().getId();

        bot.getCafeAPI().getCustomChannelApi().getCustomChannel(guildId, CustomChannelType.RAFFLE)
                .thenAccept((customChannel) -> {
                    TextChannel channel = event.getGuild().getTextChannelById(customChannel.getChannelId());

                    if (channel == null) {
                        throw new IllegalStateException("Raffle channel missing");
                    }

                    handleRaffleCreations(event, channel);
                })
                .exceptionally((ex) -> {
                    event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                            "Raffle Channel Not Set",
                            "I-... I can't do that... you don't have a raffle channel set 🥺. Try using `/channel set`!"
                    )).queue();
                    return null;
                });
    }

    private void handleRaffleCreations(SlashCommandInteractionEvent event, TextChannel raffleChannel) {
        String guildId = event.getGuild().getId();
        String title = event.getOption("title").getAsString();
        Optional<String> description = Optional.ofNullable(event.getOption("description")).map(OptionMapping::getAsString);
        int winners = event.getOption("winners").getAsInt();
        int minutesToRun = event.getOption("time").getAsInt();

        String endsAt = Instant.now().plus(minutesToRun, ChronoUnit.MINUTES).toString();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Helper.getRandomColor());
        embedBuilder.setTitle(title);
        embedBuilder.addField("Total Winners", String.format("`%d`", winners), false);
        description.ifPresent(embedBuilder::setDescription);

        raffleChannel.sendMessageEmbeds(embedBuilder.build()).queue((message) -> {
            Raffle raffle = new Raffle(0, guildId, message.getId(), title, description.orElse(null), winners, true, endsAt, null, null);

            bot.getCafeAPI().getRaffleApi().createRaffle(raffle)
                    .thenAccept((newRaffle) -> {
                        embedBuilder.setFooter(String.format("Raffle ID: %d", newRaffle.getId()));
                        embedBuilder.setTimestamp(newRaffle.getEndsAt());
                        embedBuilder.addField("Ends At", String.format("<t:%s>", newRaffle.getEndsAt().getEpochSecond()), true);
                        message.editMessageEmbeds(embedBuilder.build()).queue((finalMessage) -> finalMessage.addReaction(Emoji.fromUnicode("✅")).queue());

                        event.getHook().sendMessageEmbeds(Helper.successEmbed(
                                "Raffle Created!",
                                String.format("Hai!~ Your raffle has been created here: %s", message.getChannel().getAsMention())
                        )).queue();
                    })
                    .exceptionally((ex) -> {
                        message.delete().queue();

                        if (ex.getCause() instanceof ApiRequestException apiRequestException) {
                            String error = apiRequestException.getBody().get("error").get("raffles").get(0).asString();

                            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                                    "Error Creating Raffle",
                                    String.format("I... I couldn't create a raffle for some reason... My boss said this: `%s`.", error)
                            )).queue();
                            return null;
                        }

                        bot.getLogger().log(this.getClass(), LogLevel.DEBUG, ex.getCause().toString(), true, true);
                        return null;
                    });
        });
    }

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getDescriptionPath() {
        return "Create a raffle!";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.STRING, "title", "The raffle title.", true),
                new OptionData(OptionType.INTEGER, "winners", "The number of people who can win the raffle.", true)
                        .setMinValue(1),
                new OptionData(OptionType.INTEGER, "time", "The number of minutes to run the raffle for.", true),
                new OptionData(OptionType.STRING, "description", "The raffle description.", false)
        };
    }

}

package com.beanbeanjuice.cafebot.commands.settings.raffles;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomChannelType;
import com.beanbeanjuice.cafebot.api.wrapper.api.exception.ApiRequestException;
import com.beanbeanjuice.cafebot.api.wrapper.type.Raffle;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.i18n.I18N;
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
        I18N bundle = ctx.getUserI18n();
        String guildId = event.getGuild().getId();

        bot.getCafeAPI().getCustomChannelApi().getCustomChannel(guildId, CustomChannelType.RAFFLE)
                .thenAccept((customChannel) -> {
                    TextChannel channel = event.getGuild().getTextChannelById(customChannel.getChannelId());

                    if (channel == null) {
                        throw new IllegalStateException("Raffle channel missing");
                    }

                    handleRaffleCreations(event, channel, bundle);
                })
                .exceptionally((ex) -> {
                    event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                            bundle.getString("command.raffle.subcommand.create.embed.error.no-channel.title"),
                            bundle.getString("command.raffle.subcommand.create.embed.error.no-channel.description")
                    )).queue();
                    return null;
                });
    }

    private void handleRaffleCreations(SlashCommandInteractionEvent event, TextChannel raffleChannel, I18N bundle) {
        String guildId = event.getGuild().getId();
        String title = event.getOption("title").getAsString();
        Optional<String> description = Optional.ofNullable(event.getOption("description")).map(OptionMapping::getAsString);
        int winners = event.getOption("winners").getAsInt();
        int minutesToRun = event.getOption("time").getAsInt();

        String endsAt = Instant.now().plus(minutesToRun, ChronoUnit.MINUTES).toString();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Helper.getRandomColor());
        embedBuilder.setTitle(title);
        embedBuilder.addField(bundle.getString("command.raffle.subcommand.create.embed.field.winners"), String.format("`%d`", winners), false);
        description.ifPresent(embedBuilder::setDescription);

        raffleChannel.sendMessageEmbeds(embedBuilder.build()).queue((message) -> {
            Raffle raffle = new Raffle(0, guildId, message.getId(), title, description.orElse(null), winners, true, endsAt, null, null);

            bot.getCafeAPI().getRaffleApi().createRaffle(raffle)
                    .thenAccept((newRaffle) -> {
                        embedBuilder.setFooter(String.format("Raffle ID: %d", newRaffle.getId()));
                        embedBuilder.setTimestamp(newRaffle.getEndsAt());
                        embedBuilder.addField(bundle.getString("command.raffle.subcommand.create.embed.field.ends-at"), String.format("<t:%s>", newRaffle.getEndsAt().getEpochSecond()), true);
                        message.editMessageEmbeds(embedBuilder.build()).queue((finalMessage) -> finalMessage.addReaction(Emoji.fromUnicode("✅")).queue());

                        String successDescription = bundle.getString("command.raffle.subcommand.create.embed.success.description")
                                .replace("{channel}", message.getChannel().getAsMention());
                        event.getHook().sendMessageEmbeds(Helper.successEmbed(
                                bundle.getString("command.raffle.subcommand.create.embed.success.title"),
                                successDescription
                        )).queue();
                    })
                    .exceptionally((ex) -> {
                        message.delete().queue();

                        if (ex.getCause() instanceof ApiRequestException apiRequestException) {
                            String error = apiRequestException.getBody().get("error").get("raffles").get(0).asString();

                            String errorDescription = bundle.getString("command.raffle.subcommand.create.embed.error.description")
                                    .replace("{error}", error);
                            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                                    bundle.getString("command.raffle.subcommand.create.embed.error.title"),
                                    errorDescription
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
        return "command.raffle.subcommand.create.description";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.STRING, "title", "command.raffle.subcommand.create.arguments.title.description", true),
                new OptionData(OptionType.INTEGER, "winners", "command.raffle.subcommand.create.arguments.winners.description", true)
                        .setMinValue(1),
                new OptionData(OptionType.INTEGER, "time", "command.raffle.subcommand.create.arguments.time.description", true),
                new OptionData(OptionType.STRING, "description", "command.raffle.subcommand.create.arguments.description.description", false)
        };
    }

}

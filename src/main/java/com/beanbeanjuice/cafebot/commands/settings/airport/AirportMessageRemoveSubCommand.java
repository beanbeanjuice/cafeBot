package com.beanbeanjuice.cafebot.commands.settings.airport;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.api.wrapper.api.enums.AirportMessageType;
import com.beanbeanjuice.cafebot.i18n.I18N;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Arrays;
import java.util.concurrent.CompletionException;

public class AirportMessageRemoveSubCommand extends Command implements ISubCommand {

    public AirportMessageRemoveSubCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        I18N bundle = ctx.getUserI18n();
        AirportMessageType type = AirportMessageType.valueOf(event.getOption("type").getAsString());
        String guildId = event.getGuild().getId();

        bot.getCafeAPI().getAirportApi().deleteAirportMessage(guildId, type).thenRun(() -> {
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    bundle.getString("command.airport.subcommand.remove.embed.success.title"),
                    bundle.getString("command.airport.subcommand.remove.embed.success.description")
            )).queue();
        }).exceptionally((ex) -> {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    bundle.getString("command.airport.subcommand.remove.embed.error.title"),
                    bundle.getString("command.airport.subcommand.remove.embed.error.description")
            )).queue();
            throw new CompletionException(ex);
        });
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescriptionPath() {
        return "command.airport.subcommand.remove.description";
    }

    @Override
    public OptionData[] getOptions() {
        OptionData channelTypeData = new OptionData(OptionType.STRING, "type", "command.airport.subcommand.remove.arguments.type.description", true);

        Arrays.stream(AirportMessageType.values()).forEach((type) -> channelTypeData.addChoice(type.name(), type.name()));

        return new OptionData[] {
                channelTypeData
        };
    }

}

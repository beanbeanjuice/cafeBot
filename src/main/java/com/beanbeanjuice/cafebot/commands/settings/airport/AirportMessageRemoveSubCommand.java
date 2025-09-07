package com.beanbeanjuice.cafebot.commands.settings.airport;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.api.wrapper.api.enums.AirportMessageType;
import com.beanbeanjuice.cafebot.utility.commands.Command;
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
    public void handle(SlashCommandInteractionEvent event) {
        AirportMessageType type = AirportMessageType.valueOf(event.getOption("type").getAsString());

        String guildId = event.getGuild().getId();

        bot.getCafeAPI().getAirportApi().deleteAirportMessage(guildId, type).thenRun(() -> {
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    "Airport Message Removed",
                    "Don't worry! I removed the airport message. You'll still get notifications, so if you want to remove that too, make sure you unset the airport channel with `/channel`!"
            )).queue();
        }).exceptionally((ex) -> {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Error Removing Airport Message",
                    "I... can't remove it for some reason... Can you let me boss know? I'm scared to tell him myself..."
            )).queue();
            throw new CompletionException(ex);
        });
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescription() {
        return "Set the airport message back to the default!";
    }

    @Override
    public OptionData[] getOptions() {
        OptionData channelTypeData = new OptionData(OptionType.STRING, "type", "The message type you want to set", true);

        Arrays.stream(AirportMessageType.values()).forEach((type) -> channelTypeData.addChoice(type.name(), type.name()));

        return new OptionData[] {
                channelTypeData
        };
    }

}

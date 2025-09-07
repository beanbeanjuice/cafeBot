package com.beanbeanjuice.cafebot.commands.fun.rate;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Optional;

public class RateSmartSubCommand extends Command implements ISubCommand {

    public RateSmartSubCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        Optional<OptionMapping> userMapping = Optional.ofNullable(event.getOption("user"));

        User user = userMapping.map(OptionMapping::getAsUser).orElse(event.getUser());
        int percentage = Helper.getRandomInteger(0, 101);

        event.getHook().sendMessageEmbeds(Helper.successEmbed(
                "<:smartPeepo:1000248538376196280> Smart Rating <:smartPeepo:1000248538376196280>",
                String.format("%s is %d%% smart! %s", user.getAsMention(), percentage, "<:bloatedBlush:1154475611893547218>")
        )).mention(user).queue();
    }

    @Override
    public String getName() {
        return "smart";
    }

    @Override
    public String getDescription() {
        return "Rate how smart someone is!";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "The person who's smart levels you want to see.")
        };
    }

}

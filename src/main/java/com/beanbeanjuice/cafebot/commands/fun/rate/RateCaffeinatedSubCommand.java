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

public class RateCaffeinatedSubCommand extends Command implements ISubCommand {

    public RateCaffeinatedSubCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        Optional<OptionMapping> userMapping = Optional.ofNullable(event.getOption("user"));

        User user = userMapping.map(OptionMapping::getAsUser).orElse(event.getUser());
        int percentage = Helper.getRandomInteger(0, 101);

        event.getHook().sendMessageEmbeds(Helper.successEmbed(
                "☕ Caffeine Rating ☕",
                String.format("%s is %d%% caffeinated! %s", user.getAsMention(), percentage, "<a:yayaya:954426079177744445>")
        )).mention(user).queue();
    }

    @Override
    public String getName() {
        return "caffeinated";
    }

    @Override
    public String getDescription() {
        return "Rate how caffeinated you or someone is!";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "The person who's caffeine levels you want to see.")
        };
    }

}

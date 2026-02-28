package com.beanbeanjuice.cafebot.commands.fun.rate;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
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
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        Optional<OptionMapping> userMapping = Optional.ofNullable(event.getOption("user"));

        User user = userMapping.map(OptionMapping::getAsUser).orElse(event.getUser());
        int percentage = Helper.getRandomInteger(0, 101);

        String description = ctx.getGuildI18n().getString("command.rate.subcommand.smart.embed.description")
                .replace("{user}", user.getAsMention())
                .replace("{percent}", String.valueOf(percentage));

        event.getHook().sendMessageEmbeds(Helper.successEmbed(
                ctx.getGuildI18n().getString("command.rate.subcommand.smart.embed.title"),
                description
        )).mention(user).queue();
    }

    @Override
    public String getName() {
        return "smart";
    }

    @Override
    public String getDescriptionPath() {
        return "command.rate.subcommand.smart.description";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "command.rate.subcommand.smart.arguments.user.description")
        };
    }

}

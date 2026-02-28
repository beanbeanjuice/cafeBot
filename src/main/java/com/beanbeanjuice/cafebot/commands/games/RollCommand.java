package com.beanbeanjuice.cafebot.commands.games;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Optional;

public class RollCommand extends Command implements ICommand {

    public RollCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        Optional<OptionMapping> sizeMapping = Optional.ofNullable(event.getOption("size"));
        int size = sizeMapping.map(OptionMapping::getAsInt).orElse(6);
        int roll = Helper.getRandomInteger(1, size + 1);
        event.getHook().sendMessageEmbeds(Helper.descriptionEmbed(String.format("You rolled a **%d**!", roll))).queue();
    }

    @Override
    public String getName() {
        return "roll";
    }

    @Override
    public String getDescriptionPath() {
        return "Roll a pair of dice!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.GAME;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.INTEGER, "size", "The size of the die.", false)
                        .setRequiredRange(1, 10000)
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

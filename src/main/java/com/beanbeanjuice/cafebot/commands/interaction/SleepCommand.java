package com.beanbeanjuice.cafebot.commands.interaction;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.InteractionType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class SleepCommand extends Command implements ICommand, IInteractionCommand {

    public SleepCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        this.handleInteraction(InteractionType.SLEEP, event, bot);
    }

    @Override
    public String getName() {
        return "sleep";
    }

    @Override
    public String getDescriptionPath() {
        return "Sleep with someone~";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.INTERACTION;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "The user you want to sleep with."),
                new OptionData(OptionType.STRING, "message", "An optional message you can send.")
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

    @Override
    public String getSelfString() {
        return "%s is **sleeping**~ goodnight! <:pillow_shy:1161619112284405830>";
    }

    @Override
    public String getOtherString() {
        return "%s is **sleeping** with %s. Aww! A sleepover! <:pillow_shy:1161619112284405830>";
    }

    @Override
    public String getBotString() {
        return "I don't sleep. https://tenor.com/view/kurt-angle-gif-15112361227290036695";
    }

    @Override
    public String getFooterString() {
        return "%s has slept %d times. %s has slept with others %d times.";
    }

}

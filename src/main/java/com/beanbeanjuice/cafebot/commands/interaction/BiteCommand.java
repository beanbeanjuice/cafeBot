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

public class BiteCommand extends Command implements ICommand, IInteractionCommand {

    public BiteCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        this.handleInteraction(InteractionType.BITE, event, bot);
    }

    @Override
    public String getName() {
        return "bite";
    }

    @Override
    public String getDescriptionPath() {
        return "Bite someone!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.INTERACTION;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "The user you want to bite! :3"),
                new OptionData(OptionType.STRING, "message", "An optional message you want to add!")
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
        return "%s just **bit** themself. Wh- why would you do that?!?! <:when_AHHH:842062279372701737>";
    }

    @Override
    public String getOtherString() {
        return "Did- did %s just **bite** %s?! <:cat_shy:1161619110204031046>";
    }

    @Override
    public String getBotString() {
        return "You *do* know I'm made of metal right? 🤦🏻‍♀️";
    }

    @Override
    public String getFooterString() {
        return "%s bit others %d times. %s was bitten %d times.";
    }

}

package com.beanbeanjuice.cafebot.commands.interaction;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.interactions.InteractionType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.sections.interactions.ICommandInteraction;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class DieCommand extends Command implements ICommand, ICommandInteraction {

    public DieCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        this.handleInteraction(InteractionType.DIE, event, cafeBot);
    }

    @Override
    public String getName() {
        return "die";
    }

    @Override
    public String getDescription() {
        return "Die because of someone...";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.INTERACTION;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "The user that caused your death.. :("),
                new OptionData(OptionType.STRING, "message", "An optional message to send.")
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
        return "%s **died**... Like.. just straight up... fell over and **died**... <:bean_moment:841922879166742529>";
    }

    @Override
    public String getOtherString() {
        return "%s **died** because of %s?! What happened?! <:zerotwo_scream2:843673314990882836>";
    }

    @Override
    public String getBotString() {
        return "Wh- why are you blaming me?! <:cafeBot_angry:1171726164092518441>";
    }

    @Override
    public String getFooterString() {
        return "%s died %d times. %s caused %d deaths.";
    }

}

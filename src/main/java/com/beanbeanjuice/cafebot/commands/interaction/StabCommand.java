package com.beanbeanjuice.cafebot.commands.interaction;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.InteractionType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class StabCommand extends Command implements ICommand, IInteractionCommand {

    public StabCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        this.handleInteraction(InteractionType.STAB, event, bot);
    }

    @Override
    public String getName() {
        return "stab";
    }

    @Override
    public String getDescription() {
        return "Stab someone.";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.INTERACTION;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "The user you want to stab."),
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
        return "%s just **stabbed** themself?! Help?! <:zerotwo_scream:841921420904497163>";
    }

    @Override
    public String getOtherString() {
        return "%s just **stabbed** %s!?";
    }

    @Override
    public String getBotString() {
        return "I'm literally made of metal.. <:zerotwo_facepalm:843672933176311808>";
    }

    @Override
    public String getFooterString() {
        return "%s stabbed others %d times. %s was stabbed %d times.";
    }

}

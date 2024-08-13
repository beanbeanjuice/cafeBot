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

public class CryCommand extends Command implements ICommand, ICommandInteraction {

    public CryCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        this.handleInteraction(InteractionType.CRY, event, cafeBot);
    }

    @Override
    public String getName() {
        return "cry";
    }

    @Override
    public String getDescription() {
        return "Cry because of someone... :(";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.INTERACTION;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "The user who made you cry... :("),
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
        return "%s is **crying**! S- someone get a tissue! 😓";
    }

    @Override
    public String getOtherString() {
        return "%s is **crying** because of %s! What did you do?! <:cafeBot_angry:1171726164092518441>";
    }

    @Override
    public String getBotString() {
        return "D- did I do something wrong?! Don't cry please... <a:b_cry:1178932384436060220>";
    }

    @Override
    public String getFooterString() {
        return "%s cried because of others %d times. %s was the reason for others crying %d times.";
    }

}

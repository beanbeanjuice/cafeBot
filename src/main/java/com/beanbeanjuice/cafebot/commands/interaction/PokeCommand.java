package com.beanbeanjuice.cafebot.commands.interaction;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.interactions.InteractionType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.sections.interactions.ICommandInteraction;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class PokeCommand extends Command implements ICommand, ICommandInteraction {

    public PokeCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        this.handleInteraction(InteractionType.POKE, event, cafeBot);
    }

    @Override
    public String getName() {
        return "poke";
    }

    @Override
    public String getDescription() {
        return "Poke someone!";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "The user you want to poke."),
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
        return "%s **poked** themself... why? Checking if you're alive? <:zerotwo_facepalm:843672933176311808>";
    }

    @Override
    public String getOtherString() {
        return "%s **poked** %s!";
    }

    @Override
    public String getBotString() {
        return "Please *do not* do that. Especially while I'm working. <:cafeBot_angry:1171726164092518441>";
    }

    @Override
    public String getFooterString() {
        return "%s poked others %d times. %s was poked %d times.";
    }

}

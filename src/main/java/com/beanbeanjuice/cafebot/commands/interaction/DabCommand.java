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

public class DabCommand extends Command implements ICommand, ICommandInteraction {

    public DabCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        this.handleInteraction(InteractionType.DAB, event, cafeBot);
    }

    @Override
    public String getName() {
        return "dab";
    }

    @Override
    public String getDescription() {
        return "Dab! Why?";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "The user you want to dab toward."),
                new OptionData(OptionType.STRING, "message", "An optional message you want to send.")
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
        return "%s is **dabbing** at themself... wow... 🙄";
    }

    @Override
    public String getOtherString() {
        return "%s is **dabbing** at %s... what is this... 2015?? <:disgusted:1257142116539301909>";
    }

    @Override
    public String getBotString() {
        return "Yeah... umm... please don't do that. <:disgusted:1257142116539301909>";
    }

    @Override
    public String getFooterString() {
        return "%s dabbed %d times. %s was dabbed at %d times.";
    }

}

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

public class AmazedCommand extends Command implements ICommand, IInteractionCommand {

    public AmazedCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        this.handleInteraction(InteractionType.AMAZED, event, bot);
    }

    @Override
    public String getName() {
        return "amazed";
    }

    @Override
    public String getDescriptionPath() {
        return "Be amazed at something or someone!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.INTERACTION;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "Someone to be amazed at!"),
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
        return "Wow... %s is **amazed** at themself... Ego much? <:cafeBot_angry:1171726164092518441>";
    }

    @Override
    public String getOtherString() {
        return "%s is **amazed** at %s! <:flushed_open:841922879465455646>";
    }

    @Override
    public String getBotString() {
        return "I- I'm just doing my job! <:shy_shy:1161619101886722158>";
    }

    @Override
    public String getFooterString() {
        return "%s was amazed %d times. %s caused others to be amazed %d times.";
    }

}

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

public class LoveCommand extends Command implements ICommand, ICommandInteraction {

    public LoveCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        this.handleInteraction(InteractionType.LOVE, event, cafeBot);
    }

    @Override
    public String getName() {
        return "love";
    }

    @Override
    public String getDescription() {
        return "Love someone!~ <3";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.INTERACTION;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "The user you want to express your love to."),
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
        return "%s **loves** themself... ego much? <:disgusted:1257142116539301909>";
    }

    @Override
    public String getOtherString() {
        return "%s **loves** %s! <:aww:1257143681878593727>";
    }

    @Override
    public String getBotString() {
        return "I-... don't feel the same way... <:cafeBot_sad:1171726165040447518>";
    }

    @Override
    public String getFooterString() {
        return "%s loved others %d times. %s was loved %d times.";
    }

}

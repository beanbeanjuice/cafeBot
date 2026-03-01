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

public class HmphCommand extends Command implements ICommand, IInteractionCommand {

    public HmphCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        this.handleInteraction(InteractionType.HMPH, event, bot);
    }

    @Override
    public String getName() {
        return "hmph";
    }

    @Override
    public String getDescriptionPath() {
        return "Hmph at someone~";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.INTERACTION;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "The user you want to hmph at!~"),
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
        return "%s is **hmphing** all alone... sad... 🙄";
    }

    @Override
    public String getOtherString() {
        return "%s **hmphed** at %s! You might want to give them some attention... <:torsty2:1161620852295610429>";
    }

    @Override
    public String getBotString() {
        return "Why are you hmphing at me? What did I do?! <:when_AHHH:842062279372701737>";
    }

    @Override
    public String getFooterString() {
        return "%s hmphed at others %d times. %s was hmphed at %d times.";
    }

}

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

public class HideCommand extends Command implements ICommand, IInteractionCommand {

    public HideCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        this.handleInteraction(InteractionType.HIDE, event, this.bot);
    }

    @Override
    public String getName() {
        return "hide";
    }

    @Override
    public String getDescription() {
        return "Hide from someone... ;^;";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.INTERACTION;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "The user you want to hide from..."),
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
        return "%s is **hiding** for... no reason? What? <:kuromi_question:841921649132568576>";
    }

    @Override
    public String getOtherString() {
        return "%s is **hiding** from %s! Um... what did you do? <:kuromi_question:841921649132568576>";
    }

    @Override
    public String getBotString() {
        return "Why are you hiding from me?! Come back! <:cafeBot_angry:1171726164092518441>";
    }

    @Override
    public String getFooterString() {
        return "%s hid from others %d times. %s caused others to hide %d times.";
    }

}

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

public class BoopCommand extends Command implements ICommand, IInteractionCommand {

    public BoopCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        this.handleInteraction(InteractionType.BOOP, event, bot);
    }

    @Override
    public String getName() {
        return "boop";
    }

    @Override
    public String getDescriptionPath() {
        return "Boop someone!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.INTERACTION;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "The person you want to boop! :3"),
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
        return "%s just **booped** themself... 😭";
    }

    @Override
    public String getOtherString() {
        return "%s just **booped** %s! Awww~ <:aww:1257143681878593727>";
    }

    @Override
    public String getBotString() {
        return "hehe~ what do you need? <:pleading_blush:1257143682776432731>";
    }

    @Override
    public String getFooterString() {
        return "%s booped others %d times. %s was booped %d times.";
    }

}

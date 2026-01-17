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

public class NoseBleedCommand extends Command implements ICommand, IInteractionCommand {

    public NoseBleedCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        this.handleInteraction(InteractionType.NOSEBLEED, event, bot);
    }

    @Override
    public String getName() {
        return "nosebleed";
    }

    @Override
    public String getDescription() {
        return "Tell someone you caused them to have a nosebleed!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.INTERACTION;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "The user that caused your nosebleed."),
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
        return "%s has a **nosebleed** Someone help! <a:man_scream:841921434732724224>";
    }

    @Override
    public String getOtherString() {
        return "%s has a **nosebleed** because of %s!";
    }

    @Override
    public String getBotString() {
        return "Clean that up. I *just* cleaned, it's your turn now.";
    }

    @Override
    public String getFooterString() {
        return "%s had %d nosebleeds. %s gave others nosebleeds %d times.";
    }

}

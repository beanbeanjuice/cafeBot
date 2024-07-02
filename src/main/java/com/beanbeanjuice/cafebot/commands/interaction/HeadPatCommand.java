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

public class HeadPatCommand extends Command implements ICommand, ICommandInteraction {

    public HeadPatCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        this.handleInteraction(InteractionType.HEADPAT, event, cafeBot);
    }

    @Override
    public String getName() {
        return "headpat";
    }

    @Override
    public String getDescription() {
        return "Headpat someone!";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "The user you want to headpat."),
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
        return "%s is **headpatting** themself... okay? <:disgusted:1257142116539301909>\n";
    }

    @Override
    public String getOtherString() {
        return "%s **headpat** %s~ Aww! <:aww:1257143681878593727>\n";
    }

    @Override
    public String getBotString() {
        return "I love headpats <:pleading_blush:1257143682776432731>... wait- I have to get back to work! <:cafeBot_angry:1171726164092518441>";
    }

    @Override
    public String getFooterString() {
        return "%s headpat others %d times. %s was headpat %d times.";
    }

}

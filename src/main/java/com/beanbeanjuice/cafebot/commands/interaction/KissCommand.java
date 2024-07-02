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

public class KissCommand extends Command implements ICommand, ICommandInteraction {

    public KissCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        this.handleInteraction(InteractionType.KISS, event, cafeBot);
    }

    @Override
    public String getName() {
        return "kiss";
    }

    @Override
    public String getDescription() {
        return "Kiss someone!";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "The user you want to hug!"),
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
        return true;
    }

    @Override
    public boolean allowDM() {
        return true;
    }

    @Override
    public String getSelfString() {
        return "%s just **kissed** themself.. That's... really sad. <:disgusted2:1257151393857409216>";
    }

    @Override
    public String getOtherString() {
        return "%s **kissed** %s~ Awww love is so precious! <a:b_blush:1178932381185495070>";
    }

    @Override
    public String getBotString() {
        return "*slaps* Excuse me?! <:cafeBot_angry:1171726164092518441>";
    }

    @Override
    public String getFooterString() {
        return "%s kissed others %d times. %s was kissed %d times.";
    }

}

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

public class CuddleCommand extends Command implements ICommand, ICommandInteraction {

    public CuddleCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        this.handleInteraction(InteractionType.CUDDLE, event, cafeBot);
    }

    @Override
    public String getName() {
        return "cuddle";
    }

    @Override
    public String getDescription() {
        return "Cuddle someone!";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "The user you want to cuddle!~"),
                new OptionData(OptionType.STRING, "message", "An optional message you want to send")
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
        return "%s is **cuddling** themself... um.. that's sad... <:you_are_embarassing:1081417389532528660>";
    }

    @Override
    public String getOtherString() {
        return "%s is **cuddling** %s~ Awww <a:aww_dance:1257143681044054066>";
    }

    @Override
    public String getBotString() {
        return "Ugh. Only a few people are allowed to touch me and you're not one of them. Don't touch me. <:disgusted:1257142116539301909>";
    }

    @Override
    public String getFooterString() {
        return "%s cuddled others %d times. %s was cuddled %d times.";
    }

}

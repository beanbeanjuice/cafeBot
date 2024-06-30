package com.beanbeanjuice.cafebot.commands.games;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class CoinFlipCommand extends Command implements ICommand {

    public CoinFlipCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        boolean isHeads = (Helper.getRandomInteger(0, 2) == 1);
        String coin = (isHeads) ? "HEADS" : "TAILS";
        event.getHook().sendMessageEmbeds(Helper.descriptionEmbed(String.format("The coin is **%s**!", coin))).queue();
    }

    @Override
    public String getName() {
        return "coinflip";
    }

    @Override
    public String getDescription() {
        return "Flip a coin!";
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
}

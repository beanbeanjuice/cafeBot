package com.beanbeanjuice.cafebot.commands.games;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class CoinFlipCommand extends Command implements ICommand {

    public CoinFlipCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        boolean isHeads = (Helper.getRandomInteger(0, 2) == 1);

        String path = "command.coinflip.coin." + (isHeads ? "heads" : "tails");
        String coin = ctx.getGuildI18n().getString(path);

        String description = ctx.getGuildI18n().getString("command.coinflip.embed.description").replace("{side}", coin);

        event.getHook().sendMessageEmbeds(Helper.descriptionEmbed(description)).queue();
    }

    @Override
    public String getName() {
        return "coinflip";
    }

    @Override
    public String getDescriptionPath() {
        return "command.coinflip.description";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.GAME;
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

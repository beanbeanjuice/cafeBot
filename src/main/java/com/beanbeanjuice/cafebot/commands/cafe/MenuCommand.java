package com.beanbeanjuice.cafebot.commands.cafe;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class MenuCommand extends Command implements ICommand {

    public MenuCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        event.getHook()
                .sendMessageEmbeds(bot.getMenuHandler().getAllMenuEmbed())
                .addComponents(ActionRow.of(bot.getMenuHandler().getAllStringSelectMenu()))
                .queue();
    }

    @Override
    public String getName() {
        return "menu";
    }

    @Override
    public String getDescription() {
        return "Hungry? Check the menu out!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.CAFE;
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[0];
    }

    @Override
    public boolean isEphemeral() {
        return true;
    }

    @Override
    public boolean isNSFW() {
        return false;
    }

    @Override
    public boolean allowDM() {
        return false;
    }

}

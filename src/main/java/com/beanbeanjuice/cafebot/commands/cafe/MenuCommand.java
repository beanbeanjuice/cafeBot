package com.beanbeanjuice.cafebot.commands.cafe;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.sections.cafe.CafeCategory;
import com.beanbeanjuice.cafebot.utility.sections.cafe.MenuListener;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.util.Arrays;

public class MenuCommand extends Command implements ICommand {

    public MenuCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
//        Button leftButton = Button.primary("left", "⬅️");
//        Button rightButton = Button.primary("right", "➡️");

        event.getHook()
                .sendMessageEmbeds(cafeBot.getMenuHandler().getAllMenuEmbed())
                .addComponents(
                        ActionRow.of(cafeBot.getMenuHandler().getAllStringSelectMenu())
//                        ActionRow.of(leftButton, rightButton)
                )
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

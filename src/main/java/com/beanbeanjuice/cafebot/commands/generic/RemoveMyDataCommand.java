package com.beanbeanjuice.cafebot.commands.generic;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class RemoveMyDataCommand extends Command implements ICommand {

    private final String FORM_URL = "https://forms.office.com/r/9X5C7ERfV9";

    public RemoveMyDataCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        event.getHook().sendMessageEmbeds(Helper.successEmbed(
                "Data Removal",
                "You can request to remove your data on this bot by clicking the button below."
        )).addComponents(ActionRow.of(Button.link(FORM_URL, "Remove Data").withEmoji(Emoji.fromFormatted("<:html:1000241652444692530>")))).queue();
    }

    @Override
    public String getName() {
        return "remove-my-data";
    }

    @Override
    public String getDescription() {
        return "Remove your data from this bot.";
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
        return true;
    }
}

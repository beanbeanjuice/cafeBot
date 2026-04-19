package com.beanbeanjuice.cafebot.commands.generic;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.i18n.I18N;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class RemoveMyDataCommand extends Command implements ICommand {

    public RemoveMyDataCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        final I18N bundle = ctx.getUserI18n();
        String FORM_URL = "https://dashboard.cafebot.dev";
        event.getHook().sendMessageEmbeds(Helper.successEmbed(
                bundle.getString("command.remove-my-data.embed.title"),
                bundle.getString("command.remove-my-data.embed.description")
        )).addComponents(ActionRow.of(Button.link(FORM_URL, "Remove Data").withEmoji(Emoji.fromFormatted("<:html:1000241652444692530>")))).queue();
    }

    @Override
    public String getName() {
        return "remove-my-data";
    }

    @Override
    public String getDescriptionPath() {
        return "command.remove-my-data.description";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.GENERIC;
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

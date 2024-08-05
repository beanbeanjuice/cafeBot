package com.beanbeanjuice.cafebot.commands.generic;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class BotInviteCommand extends Command implements ICommand {

    private final String URL = "https://discord.com/api/oauth2/authorize?client_id={BOT_ID}&permissions=8&scope=bot%20applications.commands";

    public BotInviteCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        event.getHook().sendMessageComponents(ActionRow.of(getInviteButton())).queue();
    }

    private Button getInviteButton() {
        return Button.link(URL.replace("{BOT_ID}", cafeBot.getSelfUser().getId()), "Invite")
                .withEmoji(Emoji.fromFormatted("<a:cafeBot:1119635469727191190>"));
    }

    @Override
    public String getName() {
        return "bot-invite";
    }

    @Override
    public String getDescription() {
        return "Want to invite this bot to a server? Use this command!";
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

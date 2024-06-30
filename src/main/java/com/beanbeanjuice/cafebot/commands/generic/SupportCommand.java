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

public class SupportCommand extends Command implements ICommand {

    private final String SUPPORT_URL = "https://discord.gg/KrUFw3uHST";

    public SupportCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        event.getHook().sendMessageEmbeds(Helper.successEmbed(
                "Support",
                "Click the button below to get some support with the bot!"
        )).addComponents(ActionRow.of(Button.link(SUPPORT_URL, "Support Discord").withEmoji(Emoji.fromFormatted("<a:cafeBot:1119635469727191190>")))).queue();
    }

    @Override
    public String getName() {
        return "support";
    }

    @Override
    public String getDescription() {
        return "Something wrong with me? Get some support!";
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

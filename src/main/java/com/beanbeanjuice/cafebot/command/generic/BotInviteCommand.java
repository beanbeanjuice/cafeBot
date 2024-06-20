package com.beanbeanjuice.cafebot.command.generic;

import com.beanbeanjuice.cafebot.utility.command.CommandCategory;
import com.beanbeanjuice.cafebot.utility.command.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link ICommand} used to get an invitation link for the bot.
 *
 * @author beanbeanjuice
 */
public class BotInviteCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        event.getHook().sendMessageEmbeds(Helper.successEmbed(
                "Bot Invite Link",
                "Click [this](https://discord.com/api/oauth2/authorize?client_id=787162619504492554&permissions=8&scope=bot%20applications.commands) " +
                        "to invite the bot to your server!"
        )).queue();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Invite the bot to other servers!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/invite-bot`";
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.GENERIC;
    }

    @NotNull
    @Override
    public Boolean allowDM() {
        return true;
    }

    @NotNull
    @Override
    public Boolean isHidden() {
        return true;
    }
}

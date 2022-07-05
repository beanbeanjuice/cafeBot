package com.beanbeanjuice.command.generic;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.helper.Helper;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link ICommand} used to donate to the bot.
 *
 * @author beanbeanjuice
 */
public class BotDonateCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        event.getHook().sendMessageEmbeds(Helper.successEmbed(
                "Bot Donation",
                "Hello! If you want to donate, follow this [link](https://streamelements.com/beanbeanjuice/tip)! Don't feel pressured though, even if " +
                        "you don't donate, you will still get all of the features as the people who do, you just will get less of it. For example, " +
                        "instead of 10 polls, as a non-donator, you only get 3."
        )).queue();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Get information regarding donations to the bot creator!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/bot-donate`";
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

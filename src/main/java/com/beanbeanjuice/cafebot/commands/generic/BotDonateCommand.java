package com.beanbeanjuice.cafebot.commands.generic;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class BotDonateCommand extends Command implements ICommand {

    public BotDonateCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        event.getHook().sendMessageEmbeds(
                Helper.successEmbed(
                        "Donations!",
                        """
                        Donations are absolutely optional, but they help keep me alive! \
                        You can donate [here](https://buymeacoffee.com/beanbeanjuice)! \
                        Thank you so much... <a:twiddle_shy:1161619104659153018>
                        """
                )
        ).queue();
    }

    @Override
    public String getName() {
        return "bot-donate";
    }

    @Override
    public String getDescriptionPath() {
        return "Donate to keep the bot up!";
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

package com.beanbeanjuice.cafebot.commands.fun.birthday.self;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class BirthdayRemoveSubCommand extends Command implements ISubCommand {

    public BirthdayRemoveSubCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        User user = event.getUser();

        cafeBot.getCafeAPI().getBirthdaysEndpoint().removeUserBirthday(user.getId())
                .thenRunAsync(() -> event.getHook().sendMessageEmbeds(Helper.successEmbed(
                        "Birthday Removed",
                        "Your birthday has been *successfully* removed!"
                )).queue());
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescription() {
        return "Remove your birthday.";
    }

}

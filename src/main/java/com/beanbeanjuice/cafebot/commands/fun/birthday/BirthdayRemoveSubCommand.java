package com.beanbeanjuice.cafebot.commands.fun.birthday;

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

        bot.getCafeAPI().getBirthdayApi().deleteBirthday(user.getId())
                .thenRun(() -> {
                    event.getHook().sendMessageEmbeds(Helper.successEmbed(
                            "Birthday Removed 🥺",
                            "<:cafeBot_sad:1171726165040447518> Your birthday has been removed... but I know it's sometimes better to keep things private..."
                    )).queue();
                });
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

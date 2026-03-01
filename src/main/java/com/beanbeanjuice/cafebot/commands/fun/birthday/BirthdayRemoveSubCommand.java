package com.beanbeanjuice.cafebot.commands.fun.birthday;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class BirthdayRemoveSubCommand extends Command implements ISubCommand {

    public BirthdayRemoveSubCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        User user = event.getUser();

        bot.getCafeAPI().getBirthdayApi().deleteBirthday(user.getId())
                .thenRun(() -> {
                    event.getHook().sendMessageEmbeds(Helper.successEmbed(
                            ctx.getUserI18n().getString("command.birthday.subcommand.remove.embed.title"),
                            ctx.getUserI18n().getString("command.birthday.subcommand.remove.embed.description")
                    )).queue();
                });
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescriptionPath() {
        return "command.birthday.subcommand.remove.description";
    }

}

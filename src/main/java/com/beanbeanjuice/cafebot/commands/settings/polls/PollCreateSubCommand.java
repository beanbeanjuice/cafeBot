package com.beanbeanjuice.cafebot.commands.settings.polls;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.listeners.modals.polls.PollModalListener;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class PollCreateSubCommand extends Command implements ISubCommand {

    public PollCreateSubCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        String userId = event.getUser().getId();
        int duration = event.getOption("duration").getAsInt();

        event.replyModal(PollModalListener.getInitialModal(duration)).queue();
        bot.getPollSessionHandler().createSession(event.getGuild().getId(), userId, duration);
    }

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getDescription() {
        return "Create a poll!";
    }

    @Override
    public boolean isModal() {
        return true;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[]{
                new OptionData(OptionType.INTEGER, "duration", "The duration the poll will run (in minutes).", true)
                        .setMinValue(1)
        };
    }

}

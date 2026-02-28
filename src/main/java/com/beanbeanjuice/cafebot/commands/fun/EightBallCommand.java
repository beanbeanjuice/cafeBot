package com.beanbeanjuice.cafebot.commands.fun;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class EightBallCommand extends Command implements ICommand {

    public EightBallCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        String question = event.getOption("question").getAsString();
        event.getHook().sendMessageEmbeds(getAnswerEmbed(question, getAnswer())).queue();
    }

    private MessageEmbed getAnswerEmbed(final String question, final String answer) {
        return new EmbedBuilder()
                .setDescription("\"" + question + "\"")
                .addField("My Mystical Answer", "\"" + answer + "\"", false)
                .setColor(Helper.getRandomColor())
                .build();
    }

    private String getAnswer() {
        String[] yesAnswers = new String[] {
                "It is likely...",
                "Probably.",
                "Without a doubt.",
                "Of course.",
                "More than likely.",
                "YES ABSOLUTELY.",
                "Hmmm... I think so."
        };

        String[] noAnswers = new String[] {
                "Of course not.",
                "Probably not.",
                "It is not likely...",
                "There is some doubt...",
                "Less than likely.",
                "ABSOLUTELY NOT.",
                "Are you kidding? No!"
        };

        if (Helper.getRandomInteger(0, 2) == 1) return yesAnswers[(Helper.getRandomInteger(0, yesAnswers.length))];
        else return noAnswers[(Helper.getRandomInteger(0, noAnswers.length))];
    }

    @Override
    public String getName() {
        return "8ball";
    }

    @Override
    public String getDescriptionPath() {
        return "Ask me a question!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.FUN;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.STRING, "question", "The question you want to ask me!", true)
        };
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

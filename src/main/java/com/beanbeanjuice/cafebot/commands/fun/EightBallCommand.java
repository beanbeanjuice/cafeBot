package com.beanbeanjuice.cafebot.commands.fun;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.i18n.I18N;
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

import java.util.List;

public class EightBallCommand extends Command implements ICommand {

    public EightBallCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        String question = event.getOption("question").getAsString();
        String answer = getAnswer(ctx.getDefaultBundle());
        MessageEmbed answerEmbed = getAnswerEmbed(question, answer, ctx);
        event.getHook().sendMessageEmbeds(answerEmbed).queue();
    }

    private MessageEmbed getAnswerEmbed(String question, String answer, CommandContext ctx) {
        String fieldTitle = ctx.getDefaultBundle().getString("command.eightball.embed.field_title");
        String fieldDescription = ctx.getDefaultBundle().getString("command.eightball.embed.field_description").replace("{answer}", answer);

        return new EmbedBuilder()
                .setDescription("\"" + question + "\"")
                .addField(fieldTitle, fieldDescription, false)
                .setColor(Helper.getRandomColor())
                .build();
    }

    private String getAnswer(I18N i18n) {
        boolean isYes = Helper.getRandomInteger(0, 2) == 1;

        List<String> answers = i18n.getStringArray("command.eightball.answers." + (isYes ? "positive" : "negative"));

        int answerIndex = Helper.getRandomInteger(0, answers.size());
        return answers.get(answerIndex);
    }

    @Override
    public String getName() {
        return "8ball";
    }

    @Override
    public String getDescriptionPath() {
        return "command.eightball.description";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.FUN;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.STRING, "question", "command.eightball.arguments.question.description", true)
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

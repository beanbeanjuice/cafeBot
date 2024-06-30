package com.beanbeanjuice.cafebot.commands.generic;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class StatsCommand extends Command implements ICommand {

    public StatsCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        event.getHook().sendMessageEmbeds(statsEmbed()).queue();
    }

    private MessageEmbed statsEmbed() {
        return new EmbedBuilder()
                .setColor(Helper.getRandomColor())
                .setAuthor("Bot Statistics", null, cafeBot.getJDA().getSelfUser().getAvatarUrl())
                .addField("<a:cafeBot:1119635469727191190> Total Text Channels", "```" + cafeBot.getTotalChannels() + "```", true)
                .addField("<:smartPeepo:1000248538376196280> Total Servers", "```" + cafeBot.getTotalServers() + "```", true)
                .addField("⚙ Commands Run (After Restart)", "```" + cafeBot.getCommandsRun() + "```", true)
                .addField("<a:catpats:950514533875720232> Total Users", "```" + cafeBot.getTotalUsers() + "```", true)
                .setFooter("If you are enjoying this bot, please consider using /bot-upvote!")
                .build();
    }

    @Override
    public String getName() {
        return "stats";
    }

    @Override
    public String getDescription() {
        return "Get statistics about the bot!";
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

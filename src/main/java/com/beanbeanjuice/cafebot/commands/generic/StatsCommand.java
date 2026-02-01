package com.beanbeanjuice.cafebot.commands.generic;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
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
        String totalTextChannels = formatNumber(bot.getTotalChannels());
        String totalServers = formatNumber(bot.getTotalServers());
        String totalCommandsRun = formatNumber(bot.getCommandsRun().get());
        String totalUsers = formatNumber(bot.getTotalUsers());
        String totalShards = formatNumber(bot.getShardCount());

        return new EmbedBuilder()
                .setColor(Helper.getRandomColor())
                .setAuthor("Bot Statistics", null, bot.getSelfUser().getAvatarUrl())
                .addField("📝 Total Text Channels", totalTextChannels, true)
                .addField("<:smartPeepo:1000248538376196280> Total Servers", totalServers, true)
                .addField("⚙ Commands Run (After Restart)", totalCommandsRun, true)
                .addField("<a:catpats:950514533875720232> Total Users", totalUsers, true)
                .addField("<a:cafeBot:1119635469727191190> Total Shards", totalShards, true)
                .setFooter("If you are enjoying this bot, please consider using /bot-upvote!")
                .build();
    }

    private String formatNumber(int number) {
        return String.format("```%s```", Helper.formatNumber(number));
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
    public CommandCategory getCategory() {
        return CommandCategory.GENERIC;
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

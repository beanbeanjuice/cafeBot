package com.beanbeanjuice.cafebot.commands.generic;

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

public class StatsCommand extends Command implements ICommand {

    public StatsCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        event.getHook().sendMessageEmbeds(statsEmbed(ctx.getUserI18n())).queue();
    }

    private MessageEmbed statsEmbed(final I18N bundle) {
        String totalTextChannels = formatNumber(bot.getTotalChannels());
        String totalServers = formatNumber(bot.getTotalServers());
        String totalCommandsRun = formatNumber(bot.getCommandsRun().get());
        String totalUsers = formatNumber(bot.getTotalUsers());
        String totalShards = formatNumber(bot.getShardCount());

        return new EmbedBuilder()
                .setColor(Helper.getRandomColor())
                .setAuthor(bundle.getString("command.stats.embed.author"), null, bot.getSelfUser().getAvatarUrl())
                .addField(bundle.getString("command.stats.embed.channels"), totalTextChannels, true)
                .addField(bundle.getString("command.stats.embed.servers"), totalServers, true)
                .addField(bundle.getString("command.stats.embed.commands"), totalCommandsRun, true)
                .addField(bundle.getString("command.stats.embed.users"), totalUsers, true)
                .addField(bundle.getString("command.stats.embed.shards"), totalShards, true)
                .setFooter(bundle.getString("command.stats.embed.footer"))
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
    public String getDescriptionPath() {
        return "command.stats.description";
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

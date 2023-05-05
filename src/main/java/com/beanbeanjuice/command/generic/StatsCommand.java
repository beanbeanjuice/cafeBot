package com.beanbeanjuice.command.generic;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link ICommand} used to get stats for the bot.
 *
 * @author beanbeanjuice
 * @since v3.1.0
 */
public class StatsCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        event.getHook().sendMessageEmbeds(statsEmbed()).queue();
    }

    private MessageEmbed statsEmbed() {
        return new EmbedBuilder()
                .setColor(Helper.getRandomColor())
                .setAuthor("Bot Statistics", null, Bot.getBot().getSelfUser().getAvatarUrl())
                .addField("<a:cafeBot:841945919926173707> Total Text Channels", "```" + Helper.getTotalChannels() + "```", true)
                .addField("<:smartPeepo:1000248538376196280> Total Servers", "```" + Helper.getTotalServers() + "```", true)
                .addField("⚙ Commands Run (After Restart)", "```" + Bot.commandsRun + "```", true)
                .addField("<a:catpats:950514533875720232> Total Users", "```" + Helper.getTotalUsers() + "```", true)
                .setFooter("If you are enjoying this bot, please consider using /bot-upvote!")
                .build();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Get stats regarding the bot!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/stats`";
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.GENERIC;
    }

    @NotNull
    @Override
    public Boolean allowDM() {
        return true;
    }

}

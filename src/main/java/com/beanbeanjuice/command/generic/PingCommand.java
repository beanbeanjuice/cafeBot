package com.beanbeanjuice.command.generic;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.helper.Helper;
import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.sun.management.OperatingSystemMXBean;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;

/**
 * A basic {@link ICommand} for testing bot functions.
 *
 * @author beanbeanjuice
 */
public class PingCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        Bot.getBot().getRestPing().queue(
                (ping) -> event.getHook().sendMessageEmbeds(messageEmbed(ping, Bot.getBot().getGatewayPing())).queue()
        );

        if (event.getOption("any_string") != null) {
            event.getHook().sendMessage(event.getOption("any_string").getAsString()).queue();
        }
    }

    @NotNull
    private MessageEmbed messageEmbed(@NotNull Long botPing, @NotNull Long gatewayPing) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("ping!", "https://www.beanbeanjuice.com/cafeBot.html");
        StringBuilder descriptionBuilder = new StringBuilder();
        double cpuLoad = (double) Math.round((ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class).getCpuLoad()*100) * 100) / 100;
        long systemMemoryTotal = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class).getTotalMemorySize()/1048576;
        long systemMemoryUsage = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class).getCommittedVirtualMemorySize()/1048576;
        long dedicatedMemoryTotal = Runtime.getRuntime().maxMemory()/1048576;
        long dedicatedMemoryUsage = Runtime.getRuntime().totalMemory()/1048576;
        descriptionBuilder
                .append("**Rest Ping** - `").append(botPing).append("`\n")
                .append("**Gateway Ping** - `").append(gatewayPing).append("`\n")
                .append("**Current Version** - `").append(Bot.BOT_VERSION).append("`\n")
                .append("**CPU Usage** - `").append(cpuLoad).append("%`\n")
                .append("**OS Memory Usage** - `").append(systemMemoryUsage).append("` mb / `").append(systemMemoryTotal).append("` mb\n")
                .append("**Bot Memory Usage** - `").append(dedicatedMemoryUsage).append("` mb / `").append(dedicatedMemoryTotal).append("` mb\n")
                .append("**Bot Uptime** - `").append(Helper.millisToDays(ManagementFactory.getRuntimeMXBean().getUptime())).append("`\n")
                .append("**Commands Run (After Restart)** - `").append(Bot.commandsRun).append("`\n")
                .append("Hello there! How are you? Would you like to order some coffee?");

        embedBuilder
                .setDescription(descriptionBuilder.toString())
                .setFooter("Author: beanbeanjuice - " + "https://github.com/beanbeanjuice/cafeBot")
                .setThumbnail(Bot.DISCORD_AVATAR_URL)
                .setColor(Helper.getRandomColor());
        return embedBuilder.build();
    }

    @Override
    @NotNull
    public String getDescription() {
        return "Ping!";
    }

    @Override
    @NotNull
    public String exampleUsage() {
        return "`/ping` or `/ping hello` or `/ping @beanbeanjuice`";
    }

    @Override
    @NotNull
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "any_string", "Add any string to be repeated back to you.", false, false));
        options.add(new OptionData(OptionType.USER, "user", "Add any user to be repeated back to you.", false, false));
        return options;
    }

    @Override
    @NotNull
    public CommandCategory getCategoryType() {
        return CommandCategory.GENERIC;
    }

    @Override
    @NotNull
    public Boolean allowDM() {
        return true;
    }

    @NotNull
    @Override
    public Boolean isHidden() {
        return true;
    }
}

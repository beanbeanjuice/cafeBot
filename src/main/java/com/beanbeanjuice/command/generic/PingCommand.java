package com.beanbeanjuice.command.generic;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import com.beanbeanjuice.utility.logger.LogLevel;
import com.sun.management.OperatingSystemMXBean;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.lang.management.ManagementFactory;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * A general ping {@link ICommand} to show bot information.
 *
 * @author beanbeanjuice
 */
public class PingCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        CafeBot.getJDA().getRestPing().queue(
                (ping) -> event.getChannel()
                        .sendMessageEmbeds(messageEmbed(ping, CafeBot.getJDA().getGatewayPing())).queue()
        );

        if (args.size() == 1) {
            if (args.get(0).equals("testing")) {
                Exception exception = new SQLException("Fake SQL Exception");
                CafeBot.getLogManager().log(this.getClass(), LogLevel.DEBUG, "Testing Log: " + exception.getMessage(), exception);
            }
        }
    }

    @NotNull
    private MessageEmbed messageEmbed(@NotNull Long botPing, @NotNull Long gatewayPing) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(getName() + "!", "https://www.beanbeanjuice.com/cafeBot.html");
        StringBuilder descriptionBuilder = new StringBuilder();
        double cpuLoad = (double) Math.round((ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class).getCpuLoad()*100) * 100) / 100;
        long systemMemoryTotal = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class).getTotalMemorySize()/1048576;
        long systemMemoryUsage = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class).getCommittedVirtualMemorySize()/1048576;
        long dedicatedMemoryTotal = Runtime.getRuntime().maxMemory()/1048576;
        long dedicatedMemoryUsage = Runtime.getRuntime().totalMemory()/1048576;
        descriptionBuilder.append("**Rest Ping** - `").append(botPing).append("`\n")
                .append("**Gateway Ping** - `").append(gatewayPing).append("`\n")
                .append("**Current Version** - `").append(CafeBot.getBotVersion()).append("`\n")
                .append("**CPU Usage** - `").append(cpuLoad).append("%`\n")
                .append("**OS Memory Usage** - `").append(systemMemoryUsage).append("` mb / `").append(systemMemoryTotal).append("` mb\n")
                .append("**Bot Memory Usage** - `").append(dedicatedMemoryUsage).append("` mb / `").append(dedicatedMemoryTotal).append("` mb\n")
                .append("**Bot Uptime** - `").append(CafeBot.getGeneralHelper().formatTimeDays(ManagementFactory.getRuntimeMXBean().getUptime())).append("`\n")
                .append("**Commands Run** - `").append(CafeBot.getCommandsRun()).append("`\n");

        try {
            descriptionBuilder.append("**Bot Upvotes** - `").append(CafeBot.getTopGGAPI().getBot(System.getenv("CAFEBOT_TOPGG_ID")).toCompletableFuture().get()
                    .getPoints()).append("`\n\n");
        } catch (InterruptedException | ExecutionException e) {
            descriptionBuilder.append("**Bot Upvotes** - `Unable to Get Vote Count`\n\n");
        }
        descriptionBuilder.append("Hello there! How are you? Would you like to order some coffee?");
        embedBuilder.setDescription(descriptionBuilder.toString());
        embedBuilder.setFooter("Author: beanbeanjuice - " + "https://github.com/beanbeanjuice/cafeBot");
        embedBuilder.setThumbnail(CafeBot.getDiscordAvatarUrl());
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Ping the bot!";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "ping`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.TEXT, "Any Text", false);
        usage.addUsage(CommandType.LINK, "Any Link", false);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.GENERIC;
    }
}
package com.beanbeanjuice.command.generic;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.Helper;
import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.CommandOption;
import com.beanbeanjuice.utility.command.ICommand;
import com.sun.management.OperatingSystemMXBean;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;

public class PingCommand implements ICommand {

    @Override
    public void handle(@NotNull ArrayList<OptionMapping> args, @NotNull SlashCommandInteractionEvent event) {
        Bot.getBot().getRestPing().queue(
                (ping) -> event.getHook().sendMessageEmbeds(messageEmbed(ping, Bot.getBot().getGatewayPing())).setEphemeral(true).queue()
        );

        if (event.getOption("any_string") != null) {
            event.getHook().sendMessage(args.get(0).getAsString()).setEphemeral(true).queue();
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
                .append("**Current Version** - `").append(Bot.BOT_VERSION).append("`\n")
                .append("**CPU Usage** - `").append(cpuLoad).append("%`\n")
                .append("**OS Memory Usage** - `").append(systemMemoryUsage).append("` mb / `").append(systemMemoryTotal).append("` mb\n")
                .append("**Bot Memory Usage** - `").append(dedicatedMemoryUsage).append("` mb / `").append(dedicatedMemoryTotal).append("` mb\n")
                .append("**Bot Uptime** - `").append(Helper.millisToDays(ManagementFactory.getRuntimeMXBean().getUptime())).append("`\n")
                .append("**Commands Run** - `").append(Bot.commandsRun).append("`\n");

        descriptionBuilder.append("Hello there! How are you? Would you like to order some coffee?");
        embedBuilder.setDescription(descriptionBuilder.toString());
        embedBuilder.setFooter("Author: beanbeanjuice - " + "https://github.com/beanbeanjuice/cafeBot");
        embedBuilder.setThumbnail(Bot.DISCORD_AVATAR_URL);
        embedBuilder.setColor(Helper.getRandomColor());
        return embedBuilder.build();
    }

    @Override
    public @NotNull String getName() {
        return "ping";
    }

    @Override
    public @NotNull String getDescription() {
        return "Ping!";
    }

    @Override
    public @NotNull String exampleUsage(String prefix) {
        return "/ping";
    }

    @Override
    public @NotNull ArrayList<CommandOption> getOptions() {
        ArrayList<CommandOption> options = new ArrayList<>();
        options.add(new CommandOption(OptionType.STRING, "any_string", "Add any string to be repeated back to you.", false, false));
        options.add(new CommandOption(OptionType.USER, "user", "Add any user to be repeated back to you.", false, false));
        return options;
    }

    @Override
    public @NotNull CommandCategory getCategoryType() {
        return CommandCategory.GENERIC;
    }

    @Override
    public @NotNull Boolean allowDM() {
        return false;
    }
}

package com.beanbeanjuice.cafebot.commands.generic;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.sun.management.OperatingSystemMXBean;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.lang.management.ManagementFactory;
import java.util.Optional;

public class PingCommand extends Command implements ICommand {

    public PingCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        this.cafeBot.getJDA().getRestPing().queue((botPing) -> {
            long gatewayPing = this.cafeBot.getJDA().getGatewayPing();
            event.getHook().sendMessageEmbeds(messageEmbed(botPing, gatewayPing)).queue();
        });

        Optional<OptionMapping> wordOptionMapping = Optional.ofNullable(event.getOption("word"));
        Optional<OptionMapping> numberOptionMapping = Optional.ofNullable(event.getOption("number"));

        wordOptionMapping.map(OptionMapping::getAsString).ifPresent((word) -> {

            if (event.getUser().getId().equals("690927484199370753") && word.equalsIgnoreCase("menu")) {
                cafeBot.getMenuHandler().refreshMenu();
                event.getHook().sendMessage("Successfully refreshed the menu!").queue();
                return;
            }

            if (event.getUser().getId().equals("690927484199370753") && word.equalsIgnoreCase("ai")) {
                cafeBot.getAiResponseListener().refreshMaps();
                event.getHook().sendMessage("Successfully refreshed the AI!").queue();
                return;
            }

            event.getHook().sendMessage(word).queue();
        });
        numberOptionMapping.map(OptionMapping::getAsInt).ifPresent((number) -> event.getHook().sendMessage(String.valueOf(number)).queue());
    }

    private MessageEmbed messageEmbed(final long botPing, final long gatewayPing) {
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
                .append("**Current Version** - `").append(this.cafeBot.getBotVersion()).append("`\n")
                .append("**CPU Usage** - `").append(cpuLoad).append("%`\n")
                .append("**OS Memory Usage** - `").append(systemMemoryUsage).append("` mb / `").append(systemMemoryTotal).append("` mb\n")
                .append("**Bot Memory Usage** - `").append(dedicatedMemoryUsage).append("` mb / `").append(dedicatedMemoryTotal).append("` mb\n")
                .append("**Bot Uptime** - `").append(Helper.millisToDays(ManagementFactory.getRuntimeMXBean().getUptime())).append("`\n")
                .append("**Commands Run (After Restart)** - `").append(this.cafeBot.getCommandsRun()).append("`\n")
                .append("Hello there! How are you? Would you like to order some coffee?");

        embedBuilder
                .setDescription(descriptionBuilder.toString())
                .setFooter("Author: beanbeanjuice - " + "https://github.com/beanbeanjuice/cafeBot")
                .setThumbnail(this.cafeBot.getDiscordAvatarUrl())
                .setColor(Helper.getRandomColor());
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getDescription() {
        return "Pong!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.GENERIC;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.STRING, "word", "A word to repeat back to you.", false, false),
                new OptionData(OptionType.INTEGER, "number", "An integer to repeat back to you.", false, false)
                        .addChoice("One", 1)
                        .addChoice("2", 2)
        };
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[] { };
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

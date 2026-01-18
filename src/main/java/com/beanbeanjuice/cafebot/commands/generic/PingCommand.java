package com.beanbeanjuice.cafebot.commands.generic;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.scheduling.UpdateMessageScheduler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Optional;

public class PingCommand extends Command implements ICommand {

    public PingCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        int shardId = event.isFromGuild() ? event.getJDA().getShardInfo().getShardId() : -1;

        event.getHook().sendMessageEmbeds(messageEmbed(shardId)).queue();

        Optional<OptionMapping> wordOptionMapping = Optional.ofNullable(event.getOption("word"));
        Optional<OptionMapping> numberOptionMapping = Optional.ofNullable(event.getOption("number"));

        wordOptionMapping.map(OptionMapping::getAsString).ifPresent((word) -> event.getHook().sendMessage(word).queue());
        numberOptionMapping.map(OptionMapping::getAsInt).ifPresent((number) -> event.getHook().sendMessage(String.valueOf(number)).queue());
    }

    private MessageEmbed messageEmbed(int shardId) {
        EmbedBuilder embedBuilder = new EmbedBuilder(UpdateMessageScheduler.getUpdateEmbed(this.bot));

        embedBuilder.setTitle("ping!", "https://www.beanbeanjuice.com/cafeBot.html");
        embedBuilder.appendDescription("\n\nHello!~ Would you like to order some coffee?");

        if (shardId > -1) embedBuilder.appendDescription(String.format("\n\nYour shard ID is %d!", shardId));

        embedBuilder
                .setFooter("Author: beanbeanjuice - " + "https://github.com/beanbeanjuice/cafeBot")
                .setThumbnail(this.bot.getDiscordAvatarUrl())
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

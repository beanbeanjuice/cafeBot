package com.beanbeanjuice.cafebot.commands.generic;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
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
import java.util.ResourceBundle;

public class PingCommand extends Command implements ICommand {

    public PingCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        int shardId = event.isFromGuild() ? event.getJDA().getShardInfo().getShardId() : -1;

        event.getHook().sendMessageEmbeds(messageEmbed(shardId, ctx.getUserI18n())).queue();

        Optional<OptionMapping> wordOptionMapping = Optional.ofNullable(event.getOption("word"));
        Optional<OptionMapping> numberOptionMapping = Optional.ofNullable(event.getOption("number"));

        wordOptionMapping.map(OptionMapping::getAsString).ifPresent((word) -> event.getHook().sendMessage(word).queue());
        numberOptionMapping.map(OptionMapping::getAsInt).ifPresent((number) -> event.getHook().sendMessage(String.valueOf(number)).queue());
    }

    private MessageEmbed messageEmbed(int shardId, ResourceBundle bundle) {
        EmbedBuilder embedBuilder = new EmbedBuilder(UpdateMessageScheduler.getUpdateEmbed(this.bot));

        embedBuilder.setTitle("ping!", "https://www.beanbeanjuice.com/cafeBot.html");
        embedBuilder
                .appendDescription("\n\n")
                .appendDescription(bundle.getString("command.ping.embed.description"));

        if (shardId > -1) {
            String shardIdString = bundle.getString("command.ping.embed.shard").replace("{SHARD_ID}", String.valueOf(shardId));
            embedBuilder
                    .appendDescription("\n\n")
                    .appendDescription(shardIdString);
        }

        embedBuilder
                .setFooter(bundle.getString("command.ping.embed.author"))
                .setThumbnail(this.bot.getDiscordAvatarUrl())
                .setColor(Helper.getRandomColor());
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getDescriptionPath() {
        return "commands.ping.description";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.GENERIC;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.STRING, "word", "command.ping.arguments.word.description", false, false),
                new OptionData(OptionType.INTEGER, "number", "command.ping.arguments.number.description", false, false)
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

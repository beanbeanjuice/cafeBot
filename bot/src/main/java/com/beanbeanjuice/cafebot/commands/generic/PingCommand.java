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
        double gatewayPing = this.cafeBot.getShardManager().getAverageGatewayPing();
        event.getHook().sendMessageEmbeds(messageEmbed(gatewayPing)).queue();

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

    private MessageEmbed messageEmbed(final double gatewayPing) {
        EmbedBuilder embedBuilder = new EmbedBuilder(this.cafeBot.getUpdateEmbed(gatewayPing));

        embedBuilder.setTitle("ping!", "https://www.beanbeanjuice.com/cafeBot.html");
        embedBuilder.appendDescription("\n\nHello!~ Would you like to order some coffee?");
        embedBuilder
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

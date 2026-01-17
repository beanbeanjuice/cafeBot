package com.beanbeanjuice.cafebot.commands.fun;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.types.PotentialSnipeMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

public class SnipeCommand extends Command implements ICommand {

    public SnipeCommand(CafeBot bot) {
        super(bot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        bot.getSnipeHandler().popLastMessage(event.getChannelId()).ifPresentOrElse(
                (snipedMessage) -> event.getHook().sendMessageEmbeds(this.getSnipedMessageEmbed(snipedMessage)).queue(),
                () -> event.getHook().sendMessageEmbeds(this.getNoSnipeMessageEmbed()).queue()
        );
    }

    private MessageEmbed getSnipedMessageEmbed(PotentialSnipeMessage snipe) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle("Snipe!");
        embedBuilder.setAuthor(snipe.getUser().getName(), null, snipe.getUser().getAvatarUrl());
        embedBuilder.setColor(Helper.getRandomColor());
        embedBuilder.setDescription(String.format("%s said \"%s\"", snipe.getUser().getAsMention(), snipe.getMessage()));
        embedBuilder.setTimestamp(snipe.getCreatedAt());

        return embedBuilder.build();
    }

    private MessageEmbed getNoSnipeMessageEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle("No Snipe Found!");
        embedBuilder.setColor(Helper.getRandomColor());
        embedBuilder.setDescription("No one has deleted any messages in this channel recently...");

        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "snipe";
    }

    @Override
    public String getDescription() {
        return "Snipe a recently deleted message!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.FUN;
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[] {
                Permission.MESSAGE_HISTORY,
        };
    }

    @Override
    public boolean isEphemeral() {
        return false;
    }

    @Override
    public boolean isNSFW() {
        return false;
    }

    @Override
    public boolean allowDM() {
        return false;
    }

}

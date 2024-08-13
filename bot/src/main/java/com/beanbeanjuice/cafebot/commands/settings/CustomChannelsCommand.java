package com.beanbeanjuice.cafebot.commands.settings;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.sections.settings.CustomChannels;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CustomChannelsCommand extends Command implements ICommand {

    public CustomChannelsCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();

        this.cafeBot.getCafeAPI().getGuildsEndpoint().getGuildInformation(guild.getId())
                .thenAcceptAsync((guildInformation) -> {
                    String channels = Arrays.stream(CustomChannels.values()).map((type) -> {
                        String channelID = guildInformation.getSetting(type.getType());
                        TextChannel channel = guild.getTextChannelById(channelID);

                        String channelMention = (channel == null) ? "*Unset*" : channel.getAsMention();

                        return String.format("**%s** - %s", type.name(), channelMention);
                    }).collect(Collectors.joining("\n"));

                    event.getHook().sendMessageEmbeds(Helper.successEmbed(
                            "Custom Channels",
                            channels
                    )).queue();
                });
    }

    @Override
    public String getName() {
        return "customchannels";
    }

    @Override
    public String getDescription() {
        return "List all of the custom channels on this server!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.SETTINGS;
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
        return false;
    }

}

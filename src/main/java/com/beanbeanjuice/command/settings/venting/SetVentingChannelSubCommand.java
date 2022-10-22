package com.beanbeanjuice.command.settings.venting;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ISubCommand;
import com.beanbeanjuice.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.utility.helper.Helper;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ISubCommand} used to set the venting {@link TextChannel TextChannel}
 * for a specified {@link net.dv8tion.jda.api.entities.Guild Guild}.
 *
 * @author beanbeanjuice
 */
public class SetVentingChannelSubCommand implements ISubCommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {

        // Checking if a text channel.
        if (!Helper.isTextChannel(event.getChannel())) {
            event.getHook().sendMessageEmbeds(Helper.notATextChannelEmbed(event.getChannelType())).queue();
            return;
        }

        TextChannel channel = event.getChannel().asTextChannel();
        if (event.getOption("venting_channel") != null)
            channel = event.getOption("venting_channel").getAsChannel().asTextChannel();

        // If the channel is already set, notify them that this cannot be done.
        if (GuildHandler.getCustomGuild(event.getGuild()).isDailyChannel(channel.getId())) {
            event.getHook().sendMessageEmbeds(Helper.alreadyDailyChannel()).queue();
            return;
        }

        if (GuildHandler.getCustomGuild(event.getGuild()).setVentingChannelID(channel.getId())) {
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    "Set Venting Channel",
                    "This channel will now receive anonymous vents! This can cause many moderation issues within your server as " +
                            "you will no longer be able to tell who sent what message in this chat. To disable this, just do `/venting-channel remove`."
            )).queue();
            return;
        }
        event.getHook().sendMessageEmbeds(Helper.sqlServerError()).queue();
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.CHANNEL, "venting_channel", "The text channel to send anonymous vent information to.", false)
                .setChannelTypes(ChannelType.TEXT));
        return options;
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Set a channel to the venting channel!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/venting-channel set`";
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.SETTINGS;
    }

    @NotNull
    @Override
    public String getName() {
        return "set";
    }

}

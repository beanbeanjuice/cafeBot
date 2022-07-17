package com.beanbeanjuice.command.settings.birthday;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ISubCommand;
import com.beanbeanjuice.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.utility.helper.Helper;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ISubCommand} used to set the birthday {@link net.dv8tion.jda.api.entities.TextChannel TextChannel}.
 *
 * @author beanbeanjuice
 * @since v3.0.1
 */
public class SetBirthdayChannelSubCommand implements ISubCommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        TextChannel textChannel = event.getTextChannel();
        if (event.getOption("birthday_channel") != null)
            textChannel = event.getOption("birthday_channel").getAsTextChannel();

        if (GuildHandler.getCustomGuild(event.getGuild()).setBirthdayChannelID(textChannel.getId())) {
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    "Updated Birthday Channel",
                    "This channel has been set to an active birthday channel! Any user who has a birthday " +
                            "set using `/birthday set` will have their birthday displayed here!"
            )).queue();
            return;
        }
        event.getHook().sendMessageEmbeds(Helper.sqlServerError()).queue();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Set the birthday channel!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/birthday-channel set` or `/birthday-channel set #general`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.CHANNEL, "birthday_channel", "The birthday channel to set.", false)
                .setChannelTypes(ChannelType.TEXT));
        return options;
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

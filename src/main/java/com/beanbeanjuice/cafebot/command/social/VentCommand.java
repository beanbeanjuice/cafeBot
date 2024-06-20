package com.beanbeanjuice.cafebot.command.social;

import com.beanbeanjuice.cafebot.utility.command.CommandCategory;
import com.beanbeanjuice.cafebot.utility.command.ICommand;
import com.beanbeanjuice.cafebot.utility.handler.guild.CustomGuild;
import com.beanbeanjuice.cafebot.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;

/**
 * An {@link ICommand} used to send an anonymous vent!
 *
 * @author beanbeanjuice
 */
public class VentCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        CustomGuild guild = GuildHandler.getCustomGuild(event.getGuild());
        TextChannel ventChannel = guild.getVentingChannel();

        if (ventChannel == null) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Cannot Send Vent",
                    "There is no anonymous venting channel enabled on this server. Let them know " +
                            "they can enable it by doing `/venting-channel set`!"
            )).queue();
            return;
        }

        ventChannel.sendMessageEmbeds(ventEmbed(event.getOption("vent_message").getAsString())).queue((e) -> {
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    "Vent Sent",
                    "Your anonymous vent has been successfully sent!"
            )).queue();
        });
    }

    @NotNull
    private MessageEmbed ventEmbed(@NotNull String message) {
        return new EmbedBuilder()
                .setTitle("Anonymous Vent")
                .setDescription(message)
                .setColor(Helper.getRandomColor())
                .setTimestamp(new Date().toInstant())
                .build();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Send an anonymous vent if the server has it enabled!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/vent i'm sad :(`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "vent_message", "The message you want to send anonymously in the vent channel! (If the server has it " +
                "enabled)", true));
        return options;
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.SOCIAL;
    }

    @NotNull
    @Override
    public Boolean isHidden() {
        return true;
    }

}

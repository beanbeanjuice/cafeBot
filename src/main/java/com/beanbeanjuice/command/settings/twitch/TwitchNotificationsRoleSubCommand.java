package com.beanbeanjuice.command.settings.twitch;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ISubCommand;
import com.beanbeanjuice.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.utility.helper.Helper;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ISubCommand} to set the live notifications {@link Role} for the specified
 * {@link net.dv8tion.jda.api.entities.Guild Guild}.
 *
 * @author beanbeanjuice
 */
public class TwitchNotificationsRoleSubCommand implements ISubCommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        String option = event.getOption("option").getAsString();

        // Removing the role.
        if (option.equalsIgnoreCase("remove")) {
            if (GuildHandler.getCustomGuild(event.getGuild()).setLiveNotificationsRoleID("0")) {
                event.getHook().sendMessageEmbeds(Helper.successEmbed(
                        "Removed Live Notifications Role",
                        "Successfully removed the live notifications role."
                )).queue();
                return;
            }
            event.getHook().sendMessageEmbeds(Helper.sqlServerError()).queue();
            return;
        }

        // When setting the role, we need to make sure the role is not null.
        if (event.getOption("role") == null) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Role Required",
                    "When setting the role, a role name is required!"
            )).queue();
            return;
        }

        Role role = event.getOption("role").getAsRole();
        if (GuildHandler.getCustomGuild(event.getGuild()).setLiveNotificationsRoleID(role.getId())) {
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    "Set Live Notifications Role",
                    "Successfully updated the role to " + role.getAsMention() + "."
            )).queue();
            return;
        }

        event.getHook().sendMessageEmbeds(Helper.sqlServerError()).queue();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Set or remove the live notifications role!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/twitch-notifications role set @Live` or `/twitch-notifications role remove`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "option", "Set or remove the live notifications role!", true)
                .addChoice("set", "set")
                .addChoice("remove", "remove"));
        options.add(new OptionData(OptionType.ROLE, "role", "The role to be set to.", false));
        return options;
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.SETTINGS;
    }

    @NotNull
    @Override
    public Boolean allowDM() {
        return false;
    }

    @NotNull
    @Override
    public String getName() {
        return "role";
    }
}

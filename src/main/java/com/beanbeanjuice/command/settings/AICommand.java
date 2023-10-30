package com.beanbeanjuice.command.settings;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.utility.helper.Helper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to enable/disable the AI for the specified {@link net.dv8tion.jda.api.entities.Guild Guild}.
 *
 * @author beanbeanjuice
 */
public class AICommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {

        // If no arguments, just send the current AI state for the server.
        if (event.getOption("option") == null) {
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    "Current AI State",
                    "The current AI state is: `" + GuildHandler.getCustomGuild(event.getGuild()).getAIState() + "`."
            )).queue();
            return;
        }

        String state = event.getOption("option").getAsString();
        Boolean newAiState = state.equalsIgnoreCase("enable");
        if (GuildHandler.getCustomGuild(event.getGuild()).setAIState(newAiState)) {
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    "AI Updated",
                    "The AI state has been set to: `" + newAiState + "`."
            )).queue();
            return;
        }

        event.getHook().sendMessageEmbeds(Helper.sqlServerError()).queue();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Enable or disable the ai!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/ai true`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "option", "Enable or disable the AI for this server!", false)
                .addChoice("Enable", "enable")
                .addChoice("Disable", "disable"));
        return options;
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.SETTINGS;
    }

    @NotNull
    @Override
    public Boolean isHidden() {
        return true;
    }

    @Nullable
    @Override
    public ArrayList<Permission> getPermissions() {
        ArrayList<Permission> permissions = new ArrayList<>();
        permissions.add(Permission.MANAGE_SERVER);
        return permissions;
    }

}

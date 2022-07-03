package com.beanbeanjuice.command.moderation.counting;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.CommandOption;
import com.beanbeanjuice.utility.command.ISubCommand;
import com.beanbeanjuice.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ISubCommand} used to set the counting failure {@link Role} for
 * a specified {@link net.dv8tion.jda.api.entities.Guild Guild}.
 *
 * @author beanbeanjuice
 */
public class CountingChannelFailureRoleSubCommand implements ISubCommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {

        // If the counting role is not enabled
        if (!event.getOption("enable_failure_role").getAsBoolean()) {
            if (Bot.getCountingHelper().setCountingFailureRoleID(event.getGuild().getId(), "0")) {
                event.getHook().sendMessageEmbeds(Helper.successEmbed(
                        "Removed Counting Failure Role",
                        "Successfully removed the counting failure role."
                )).queue();
                return;
            }
            event.getHook().sendMessageEmbeds(Helper.sqlServerError()).queue();
            return;
        }

        // Otherwise, add the role.
        Role role;

        // Error checking. Making sure the user has specified a failure role.
        try {
            role = event.getOption("failure_role").getAsRole();
        } catch (NullPointerException e) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Error Enabling Counting Failure Role",
                    "You must provide a valid role."
            )).queue();
            return;
        }

        // If the role is unable to update in the database
        if (!Bot.getCountingHelper().setCountingFailureRoleID(event.getGuild().getId(), role.getId())) {
            event.getHook().sendMessageEmbeds(Helper.sqlServerError()).queue();
            return;
        }

        event.getHook().sendMessageEmbeds(successfulRoleChangeEmbed(role)).queue();
    }

    @NotNull
    private MessageEmbed successfulRoleChangeEmbed(@NotNull Role role) {
        return new EmbedBuilder()
                .setColor(Helper.getRandomColor())
                .setTitle("Successfully changed the Counting Failure Role")
                .setDescription("Successfully changed the counting failure role to " + role.getAsMention())
                .build();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Set or remove the role to give a user when they suck at counting.";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/counting-channel failure_role @bad at counting`";
    }

    @NotNull
    @Override
    public ArrayList<CommandOption> getOptions() {
        ArrayList<CommandOption> options = new ArrayList<>();
        options.add(new CommandOption(OptionType.BOOLEAN, "enable_failure_role", "Set to true to enable the failure role.", true, false));
        options.add(new CommandOption(OptionType.ROLE, "failure_role", "The role to give a user when they suck at counting.", false, false));
        return options;
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.MODERATION;
    }

    @NotNull
    @Override
    public Boolean allowDM() {
        return false;
    }

    @NotNull
    @Override
    public Boolean isHidden() {
        return true;
    }

    @NotNull
    @Override
    public String getName() {
        return "set-failure-role";
    }

}

package com.beanbeanjuice.command.twitch;

import com.beanbeanjuice.main.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;

/**
 * A command used for setting the Live Notifications {@link Role} for a {@link net.dv8tion.jda.api.entities.Guild Guild}.
 *
 * @author beanbeanjuice
 */
public class SetLiveNotificationsRoleCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {

        if (!CafeBot.getGeneralHelper().isModerator(event.getMember(), event.getGuild(), event)) {
            return;
        }

        // Checks if "none" is selected.
        if (args.get(0).equalsIgnoreCase("none")) {
            if (ctx.getCustomGuild().setLiveNotificationsRoleID("0")) {
                event.getChannel().sendMessage(noneEmbed()).queue();
            } else {
                errorMessage(event);
            }
            return;
        }

        Role liveNotificationsRole;

        try {
            liveNotificationsRole = CafeBot.getGeneralHelper().getRole(event.getGuild(), args.get(0));
        } catch (NumberFormatException e) {
            // This means that a role/none was not specified.
            event.getChannel().sendMessage(invalidRoleEmbed(args.get(0))).queue();
            return;
        }

        if (liveNotificationsRole != null) {
            if (ctx.getCustomGuild().setLiveNotificationsRole(liveNotificationsRole)) {
                event.getChannel().sendMessage(CafeBot.getGeneralHelper().successEmbed(
                        "Updated Live Notifications Role",
                        "Successfully updated the role to " + liveNotificationsRole.getAsMention() + "."
                )).queue();
            } else {
                errorMessage(event);
            }
            return;
        }

    }

    private MessageEmbed invalidRoleEmbed(@NotNull String argument) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Invalid Role");
        embedBuilder.setDescription("The argument `" + argument + "` is not a valid role. " +
                "Please select `none` or provide a proper role mention.");
        embedBuilder.setColor(Color.red);
        return embedBuilder.build();
    }

    private void errorMessage(@NotNull GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(CafeBot.getGeneralHelper().errorEmbed(
                "Error Updating Live Notifications Role",
                "There was an error updating the live notifications role. Please try again."
        ));
    }

    private MessageEmbed noneEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Updated Live Notifications Role");
        embedBuilder.setDescription("Successfully removed the live notifications role!");
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "set-live-notifications-role";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("setlivenotificationsrole");
        arrayList.add("set-twitch-notifications-role");
        arrayList.add("settwitchnotificationsrole");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Set a role to be notified for live notifications! Set to `none` to disable.";
    }

    @Override
    public String exampleUsage() {
        return "`!!set-live-notifications-role @LiveNotifications`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.TEXT, "Role/none", true);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.TWITCH;
    }
}

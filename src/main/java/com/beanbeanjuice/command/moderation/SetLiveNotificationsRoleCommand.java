package com.beanbeanjuice.command.moderation;

import com.beanbeanjuice.main.BeanBot;
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

import java.util.ArrayList;

public class SetLiveNotificationsRoleCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {

        if (!BeanBot.getGeneralHelper().isModerator(event.getMember(), event.getGuild(), event)) {
            return;
        }

        // Checks if "none" is selected.
        if (args.get(0).equalsIgnoreCase("none")) {
            if (BeanBot.getGuildHandler().getCustomGuild(event.getGuild()).setLiveNotificationsRoleID("0")) {
                event.getChannel().sendMessage(noneEmbed()).queue();
            } else {
                errorMessage(event);
            }
            return;
        }

        Role liveNotificationsRole = BeanBot.getGeneralHelper().getRole(event.getGuild(), args.get(0));

        if (liveNotificationsRole != null) {
            if (BeanBot.getGuildHandler().getCustomGuild(event.getGuild()).setLiveNotificationsRole(liveNotificationsRole)) {
                event.getChannel().sendMessage(BeanBot.getGeneralHelper().successEmbed(
                        "Updated Live Notifications Role",
                        "Successfully updated the role to " + liveNotificationsRole.getAsMention() + "."
                )).queue();
            } else {
                errorMessage(event);
            }
            return;
        }

        // This means that a role/none was not specified.
        event.getChannel().sendMessage(invalidRoleEmbed(args.get(0))).queue();

    }

    private MessageEmbed invalidRoleEmbed(@NotNull String argument) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Invalid Role");
        embedBuilder.setDescription("The argument `" + argument + "` is not a valid role. " +
                "Please select `none` or provide a proper role mention.");
        embedBuilder.setColor(BeanBot.getGeneralHelper().getRandomColor());
        return embedBuilder.build();
    }

    private void errorMessage(@NotNull GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(BeanBot.getGeneralHelper().errorEmbed(
                "Error Updating Live Notifications Role",
                "There was an error updating the live notifications role. Please try again."
        ));
    }

    private MessageEmbed noneEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Updated Live Notifications Role");
        embedBuilder.setDescription("Successfully removed the live notifications role!");
        embedBuilder.setColor(BeanBot.getGeneralHelper().getRandomColor());
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
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.TEXT, "Role/none", true);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.MODERATION;
    }
}

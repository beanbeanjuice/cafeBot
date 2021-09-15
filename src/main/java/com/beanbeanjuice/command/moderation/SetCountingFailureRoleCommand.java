package com.beanbeanjuice.command.moderation;

import com.beanbeanjuice.CafeBot;
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
 * A class used for changing the countingFailure {@link Role}.
 *
 * @author beanbeanjuice
 */
public class SetCountingFailureRoleCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        if (!CafeBot.getGeneralHelper().isAdministrator(event.getMember(), event)) {
            return;
        }

        String argument = args.get(0).replace("<@&", "").replace(">", "");

        if (args.get(0).equals("0")) {
            if (CafeBot.getCountingHelper().setCountingFailureRoleID(event.getGuild().getId(), "0")) {
                event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().successEmbed(
                        "Removed Counting Failure Role",
                        "Successfully removed the counting failure role."
                )).queue();
                return;
            }
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().sqlServerError()).queue();
            return;
        }

        Role role = event.getGuild().getRoleById(argument);

        if (role == null) {
            event.getChannel().sendMessageEmbeds(unknownRoleEmbed(argument)).queue();
            return;
        }

        if (!CafeBot.getCountingHelper().setCountingFailureRoleID(event.getGuild().getId(), role.getId())) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().sqlServerError()).queue();
            return;
        }

        event.getChannel().sendMessageEmbeds(successfulRoleChangeEmbed(role)).queue();
    }

    @NotNull
    private MessageEmbed unknownRoleEmbed(@NotNull String roleName) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.red);
        embedBuilder.setTitle("Unknown Role");
        embedBuilder.setDescription("`" + roleName + "` is not a role.");
        return embedBuilder.build();
    }

    @NotNull
    private MessageEmbed successfulRoleChangeEmbed(@NotNull Role role) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        embedBuilder.setTitle("Successfully changed the Counting Failure Role");
        embedBuilder.setDescription("Successfully changed the counting failure role to " + role.getAsMention());
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "set-counting-failure-role";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("setcountingfailurerole");
        arrayList.add("counting-failure-role");
        arrayList.add("countingfailurerole");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Sets a role to give someone when they fail counting.";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "set-counting-failure-role @bad at counting` or `" + prefix + "set-counting-failure-role 0`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.ROLE, "Role/0", true);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.MODERATION;
    }
}

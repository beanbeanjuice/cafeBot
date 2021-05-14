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

/**
 * A command to set the muted {@link Role}.
 *
 * @author beanbeanjuice
 */
public class SetMutedRoleCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {

        if (!BeanBot.getGeneralHelper().isAdministrator(event.getMember(), event)) {
            return;
        }

        Role role = BeanBot.getGeneralHelper().getRole(event.getGuild(), args.get(0));

        if (!BeanBot.getGuildHandler().getCustomGuild(event.getGuild()).updateMutedRole(role.getId())) {
            event.getChannel().sendMessage(BeanBot.getGeneralHelper().sqlServerError()).queue();
            return;
        }

        event.getChannel().sendMessage(successfulRoleChangeEmbed(role)).queue();
    }

    @NotNull
    private MessageEmbed successfulRoleChangeEmbed(@NotNull Role role) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(BeanBot.getGeneralHelper().getRandomColor());
        embedBuilder.setAuthor("Successfully changed the Muted Role");
        embedBuilder.setDescription("Successfully changed the muted role to " + role.getAsMention());
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "set-muted-role";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("setmutedrole");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Sets the muted role for the server.";
    }

    @Override
    public String exampleUsage() {
        return "`!!setmutedrole @MutedRole`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.ROLE, "Mentioned Role", true);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.MODERATION;
    }
}

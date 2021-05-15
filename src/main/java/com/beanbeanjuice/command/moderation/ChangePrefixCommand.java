package com.beanbeanjuice.command.moderation;

import com.beanbeanjuice.main.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * A command to change the prefix for a guild.
 *
 * @author beanbeanjuice
 */
public class ChangePrefixCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        if (!CafeBot.getGeneralHelper().isAdministrator(event.getMember(), event)) {
            return;
        }

        if (!CafeBot.getGuildHandler().getCustomGuild(event.getGuild()).setPrefix(args.get(0))) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().sqlServerError()).queue();
            return;
        }

        event.getChannel().sendMessage(successfulPrefixChangeEmbed(args.get(0))).queue();
    }

    @NotNull
    private MessageEmbed successfulPrefixChangeEmbed(String prefix) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        embedBuilder.setAuthor("Successfully updated prefix.");
        embedBuilder.setDescription("The prefix has been successfully updated to `" + prefix + "`.");
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "change-prefix";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("changeprefix");
        arrayList.add("setprefix");
        arrayList.add("set-prefix");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Change the prefix for the server.";
    }

    @Override
    public String exampleUsage() {
        return "`!!changeprefix b!`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.TEXT, "New Prefix", true);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.MODERATION;
    }
}

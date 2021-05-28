package com.beanbeanjuice.command.moderation;

import com.beanbeanjuice.main.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

/**
 * A command used for setting the counting {@link net.dv8tion.jda.api.entities.TextChannel TextChannel}.
 *
 * @author beanbeanjuice
 */
public class SetCountingChannelCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        if (!CafeBot.getGeneralHelper().isAdministrator(event.getMember(), event)) {
            return;
        }

        if (args.size() == 1) {
            String commandTerm = args.get(0);
            if (commandTerm.equalsIgnoreCase("remove") || commandTerm.equalsIgnoreCase("disable") || commandTerm.equalsIgnoreCase("0")) {
                if (CafeBot.getGuildHandler().getCustomGuild(event.getGuild()).setCountingChannel("0")) {
                    event.getChannel().sendMessage(CafeBot.getGeneralHelper().successEmbed(
                            "Removed Counting Channel",
                            "Successfully removed the counting channel."
                    )).queue();
                    return;
                }
                event.getChannel().sendMessage(CafeBot.getGeneralHelper().sqlServerError()).queue();
                return;
            }

            event.getChannel().sendMessage(CafeBot.getGeneralHelper().errorEmbed(
                    "Incorrect Extra Term",
                    "You can run this command without extra arguments. You had the extra argument `" + commandTerm + "`. " +
                            "The available command terms for this command are `disable`, `remove`, and `0`."
            )).queue();
            return;
        }

        if (CafeBot.getGuildHandler().getCustomGuild(event.getGuild()).setCountingChannel(event.getChannel().getId())) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().successEmbed(
                    "Updated Counting Channel",
                    "Successfully set the counting channel to this channel. " +
                            "To remove counting, just delete the channel."
            )).queue();

            CafeBot.getCountingHelper().createNewRow(event.getGuild());
            return;
        }
        event.getChannel().sendMessage(CafeBot.getGeneralHelper().sqlServerError()).queue();
    }

    @Override
    public String getName() {
        return "set-counting-channel";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("setcountingchannel");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Set the current channel to the active counting channel.";
    }

    @Override
    public String exampleUsage() {
        return "`!!setcountingchannel`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.TEXT, "disable/remove/0", false);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.MODERATION;
    }
}

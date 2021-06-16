package com.beanbeanjuice.command.moderation;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to set the venting {@link net.dv8tion.jda.api.entities.TextChannel TextChannel} for the {@link net.dv8tion.jda.api.entities.Guild}.
 *
 * @author beanbeanjuice
 */
public class SetVentingChannelCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        if (!CafeBot.getGeneralHelper().isAdministrator(event.getMember(), event)) {
            return;
        }

        if (args.size() == 1) {
            String commandTerm = args.get(0);
            if (commandTerm.equalsIgnoreCase("remove") || commandTerm.equalsIgnoreCase("disable") || commandTerm.equalsIgnoreCase("0")) {
                if (CafeBot.getGuildHandler().getCustomGuild(event.getGuild()).setVentingChannelID("0")) {
                    event.getChannel().sendMessage(CafeBot.getGeneralHelper().successEmbed(
                            "Removed Venting Channel",
                            "Successfully removed the venting channel."
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

        if (CafeBot.getGuildHandler().getCustomGuild(event.getGuild()).setVentingChannelID(event.getChannel().getId())) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().successEmbed(
                    "Set Venting Channel",
                    "This channel will now receive anonymous vents! This can cause many moderation issues within your server as " +
                            "you will no longer be able to tell who sent what message in this chat. To disable this, just do `set-venting-channel disable`."
            )).queue();
            return;
        }
        event.getChannel().sendMessage(CafeBot.getGeneralHelper().sqlServerError()).queue();
    }

    @Override
    public String getName() {
        return "set-venting-channel";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("setventingchannel");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Sets the current channel to the channel that receives anonymous vents.";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "setventingchannel` or `" + prefix + "setventingchannel 0`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.TEXT, "Disable/Remove/0", false);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.MODERATION;
    }

}

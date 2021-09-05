package com.beanbeanjuice.command.moderation;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.cafeapi.exception.AuthorizationException;
import com.beanbeanjuice.cafeapi.exception.CafeException;
import com.beanbeanjuice.cafeapi.exception.ConflictException;
import com.beanbeanjuice.cafeapi.exception.ResponseException;
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
                    event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().successEmbed(
                            "Removed Counting Channel",
                            "Successfully removed the counting channel."
                    )).queue();
                    return;
                }
                event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().sqlServerError()).queue();
                return;
            }

            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                    "Incorrect Extra Term",
                    "You can run this command without extra arguments. You had the extra argument `" + commandTerm + "`. " +
                            "The available command terms for this command are `disable`, `remove`, and `0`."
            )).queue();
            return;
        }

        if (CafeBot.getGuildHandler().getCustomGuild(event.getGuild()).setCountingChannel(event.getChannel().getId())) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().successEmbed(
                    "Updated Counting Channel",
                    "Successfully set the counting channel to this channel. " +
                            "To remove counting, just delete the channel."
            )).queue();

            try {
                CafeBot.getCafeAPI().countingInformations().createGuildCountingInformation(event.getGuild().getId());
            } catch (ConflictException ignored) {}
            catch (AuthorizationException | ResponseException e) {
                event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                        "Error Creating Counting Information",
                        "There has been an error creating counting information. Please re-add the counting channel."
                )).queue();
                return;
            }
            return;
        }
        event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().sqlServerError()).queue();
    }

    @Override
    public String getName() {
        return "set-counting-channel";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("setcountingchannel");
        arrayList.add("set-counting");
        arrayList.add("setcounting");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Set the current channel to the active counting channel.";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "setcountingchannel` or `" + prefix + "set-counting-channel 0`";
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

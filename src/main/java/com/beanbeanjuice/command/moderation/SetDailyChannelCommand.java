package com.beanbeanjuice.command.moderation;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import com.beanbeanjuice.utility.guild.CustomChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * An {@link ICommand} used to set the daily {@link net.dv8tion.jda.api.entities.TextChannel TextChannel} for the {@link net.dv8tion.jda.api.entities.Guild Guild}.
 *
 * @author beanbeanjuice
 */
public class SetDailyChannelCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        if (!CafeBot.getGeneralHelper().isAdministrator(event.getMember(), event)) {
            return;
        }

        if (args.size() == 1) {
            String commandTerm = args.get(0);
            if (commandTerm.equalsIgnoreCase("remove") || commandTerm.equalsIgnoreCase("disable") || commandTerm.equalsIgnoreCase("0")) {
                if (CafeBot.getGuildHandler().getCustomGuild(event.getGuild()).setDailyChannelID("0")) {
                    event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().successEmbed(
                            "Removed Daily Channel",
                            "Successfully removed the daily channel."
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

        AtomicBoolean alreadySet = new AtomicBoolean(false);
        CafeBot.getGuildHandler().getCustomGuild(event.getGuild()).getCustomChannelIDs().forEach((customChannel, channelID) -> {
            if (!customChannel.equals(CustomChannel.DAILY)) {
                if (channelID.equals(event.getChannel().getId())) {
                    alreadySet.set(true);
                }
            }
        });

        if (alreadySet.get()) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed("Channel Already Set",
                    "This current channel is already set to something. For a list of channels you are already using with this bot, do `" +
                    ctx.getPrefix() + "get-custom-channels`")).queue();
            return;
        }

        if (CafeBot.getGuildHandler().getCustomGuild(event.getGuild()).setDailyChannelID(event.getChannel().getId())) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().successEmbed(
                    "Updated Daily Channel",
                    "Successfully set the daily channel to this channel. " +
                            "To remove the daily channel, just delete the channel. Just a reminder, " +
                            "any integrations you have with this channel will be deleted upon the copy of this channel."
            )).queue();

            return;
        }
        event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().sqlServerError()).queue();
    }

    @Override
    public String getName() {
        return "set-daily-channel";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("setdailychannel");
        arrayList.add("setdaily");
        arrayList.add("set-daily");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Sets the current channel to the daily channel. This channel will be deleted and re-added every day, once a day.";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "set-daily-channel` or `" + prefix + "set-daily-channel remove`";
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

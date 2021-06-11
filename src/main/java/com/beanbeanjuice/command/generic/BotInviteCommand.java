package com.beanbeanjuice.command.generic;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to get an invite link for the bot.
 *
 * @author beanbeanjuice
 */
public class BotInviteCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(CafeBot.getGeneralHelper().successEmbed(
                "Bot Invite Link",
                "Click [this](https://discord.com/api/oauth2/authorize?client_id=787162619504492554&permissions=305917254&scope=bot) to invite the bot to your server!"
        )).queue();
    }

    @Override
    public String getName() {
        return "invite-bot";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("invitebot");
        arrayList.add("bot-invite");
        arrayList.add("botinvite");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Get an invite for the bot to add it to your server!";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "botinvite`";
    }

    @Override
    public Usage getUsage() {
        return new Usage();
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.GENERIC;
    }
}

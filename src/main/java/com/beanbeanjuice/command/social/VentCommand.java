package com.beanbeanjuice.command.social;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;

/**
 * An {@link ICommand} used to vent anonymously.
 *
 * @author beanbeanjuice
 */
public class VentCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        event.getMessage().delete().queue();
        TextChannel ventChannel = CafeBot.getGuildHandler().getCustomGuild(event.getGuild()).getVentingChannel();

        // Making sure the venting channel exists.
        if (ventChannel == null) {
            CafeBot.getGeneralHelper().pmUser(user, CafeBot.getGeneralHelper().errorEmbed(
                    "Venting Not Enabled",
                    "The server you are trying to anonymously vent on currently does not have anonymous venting setup."
            ));
            return;
        }

        StringBuilder contentBuilder = new StringBuilder();
        for (int i = 0; i < args.size(); i++) {
            contentBuilder.append(args.get(i));

            if (i != args.size() - 1) {
                contentBuilder.append(" ");
            }
        }

        ventChannel.sendMessage(ventBuilder(CafeBot.getGeneralHelper().shortenToLimit(contentBuilder.toString(), 2048))).queue();
    }

    private MessageEmbed ventBuilder(@NotNull String ventContent) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Anonymous Vent");
        embedBuilder.setThumbnail("http://cdn.beanbeanjuice.com/images/cafeBot/social/anonymous_venting.png");
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        embedBuilder.setDescription(ventContent);
        embedBuilder.setTimestamp(new Date().toInstant());
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "vent";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("anonymously-vent");
        arrayList.add("anonymous-vent");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Anonymously vent! (If the server has that setup...)";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "vent I hate eggs...`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.SENTENCE, "Stuff To Vent About", true);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.SOCIAL;
    }

}

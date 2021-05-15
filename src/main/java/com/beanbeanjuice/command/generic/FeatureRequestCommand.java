package com.beanbeanjuice.command.generic;

import com.beanbeanjuice.main.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

/**
 * A command used for sending the user a feature request link.
 *
 * @author beanbeanjuice
 */
public class FeatureRequestCommand implements ICommand {

    private final String FEATURE_REQUEST_URL = "https://github.com/beanbeanjuice/cafeBot/issues/new?assignees=beanbeanjuice&labels=feature&template=feature_request.md&title=%5BFEATURE%5D+%2A%2ADESCRIBE+THE+FEATURE+YOU+WANT+AS+SHORT+AS+POSSIBLE+HERE%2A%2A";

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(featureRequestEmbed()).queue();
    }

    private MessageEmbed featureRequestEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Feature Request", FEATURE_REQUEST_URL);
        embedBuilder.setDescription("If you want to submit a feature request, please click on the title above.");
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "feature-request";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("featurerequest");
        arrayList.add("request-feature");
        arrayList.add("requestfeature");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Request a feature!";
    }

    @Override
    public String exampleUsage() {
        return "`!!feature-request`";
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

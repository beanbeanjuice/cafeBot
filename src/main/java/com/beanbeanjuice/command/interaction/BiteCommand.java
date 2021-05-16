package com.beanbeanjuice.command.interaction;

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
 * A command used to bite people!
 *
 * @author beanbeanjuice
 */
public class BiteCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        String url = CafeBot.getInteractionHandler().getBiteImage();
        String biter = user.getName();
        String bitee = CafeBot.getGeneralHelper().getUser(args.get(0)).getName();
        String message = "**" + bitee + "**, you have been *bitten* by **" + biter + "**!";

        if (args.size() == 1) {
            event.getChannel().sendMessage(message).embed(biteEmbed(url)).queue();
        } else {
            event.getChannel().sendMessage(message).embed(biteWithDescriptionEmbed(url, args)).queue();
        }
    }

    @NotNull
    private MessageEmbed biteEmbed(@NotNull String link) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setImage(link);
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        return embedBuilder.build();
    }

    @NotNull
    private MessageEmbed biteWithDescriptionEmbed(@NotNull String link, @NotNull ArrayList<String> args) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setImage(link);
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());

        StringBuilder descriptionBuilder = new StringBuilder();
        descriptionBuilder.append("\"");

        for (int i = 1; i < args.size(); i++) {
            descriptionBuilder.append(args.get(i));
            if (i != args.size() - 1) {
                descriptionBuilder.append(" ");
            }
        }

        descriptionBuilder.append("\"");
        embedBuilder.setDescription(descriptionBuilder.toString());
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "bite";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "bite someone!";
    }

    @Override
    public String exampleUsage() {
        return "`!!bite @beanbeanjuice` or `!!bite @beanbeanjuice :O`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.USER, "User Mention", true);
        usage.addUsage(CommandType.SENTENCE, "Extra Message", false);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.INTERACTION;
    }

}

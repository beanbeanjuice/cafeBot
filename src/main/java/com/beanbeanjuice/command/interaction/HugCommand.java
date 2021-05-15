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
 * A command used to hug people.
 *
 * @author beanbeanjuice
 */
public class HugCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        String url = CafeBot.getInteractionHandler().getHugImage();
        if (args.size() == 1) {
            event.getChannel().sendMessage(hugEmbed(user, CafeBot.getGeneralHelper().getUser(args.get(0)), url)).queue();
        } else {
            event.getChannel().sendMessage(hugWithDescriptionEmbed(user, CafeBot.getGeneralHelper().getUser(args.get(0)), url, args)).queue();
        }
    }

    @NotNull
    private MessageEmbed hugEmbed(@NotNull User hugger, @NotNull User huggee, @NotNull String link) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setImage(link);
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        embedBuilder.setDescription(hugger.getAsMention() + " hugs " + huggee.getAsMention() + ".");
        return embedBuilder.build();
    }

    @NotNull
    private MessageEmbed hugWithDescriptionEmbed(@NotNull User hugger, @NotNull User huggee, @NotNull String link, @NotNull ArrayList<String> args) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setImage(link);
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());

        StringBuilder descriptionBuilder = new StringBuilder();
        descriptionBuilder.append(hugger.getAsMention()).append(" hugs ").append(huggee.getAsMention()).append(".\n\n")
                .append("*");

        for (int i = 1; i < args.size(); i++) {
            descriptionBuilder.append(args.get(i));

            if (i != args.size() - 1) {
                descriptionBuilder.append(" ");
            }
        }

        descriptionBuilder.append("*");
        embedBuilder.setDescription(descriptionBuilder.toString());
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "hug";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Hug someone!";
    }

    @Override
    public String exampleUsage() {
        return "`!!hug @beanbeanjuice` or `!!hug @beanbeanjuice You're so cool`";
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

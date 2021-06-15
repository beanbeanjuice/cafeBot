package com.beanbeanjuice.command.generic;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.CommandUsage;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;

/**
 * A general help command.
 *
 * @author beanbeanjuice
 */
public class HelpCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        String prefix = ctx.getPrefix();

        // Checking if the arguments is empty.
        if (args.isEmpty()) {
            channel.sendMessage(categoryEmbed(ctx.getPrefix())).queue(); // Sends the list of categories.
            return;
        }

        // Setting the Search Term
        String search = args.get(0);
        int count = 1;

        // Goes through each category. If the first argument is equal to the name, then print commands for that category.
        for (CategoryType categoryType : CategoryType.values()) {
            if (categoryType.toString().equalsIgnoreCase(search) || String.valueOf(count++).equals(search)) {
                channel.sendMessage(searchCategoriesEmbed(prefix, categoryType)).queue();
                return;
            }
        }

        ICommand command = CafeBot.getCommandManager().getCommand(search);

        // Checks to see if any commands exist for that command.
        if (command == null) {
            channel.sendMessage(noCommandFoundEmbed(search, ctx.getPrefix())).queue();
            return;
        }

        // Logic to show command and optional parameters.
        channel.sendMessage(commandEmbed(prefix, command)).queue();
    }

    @NotNull
    private MessageEmbed commandEmbed(@NotNull String prefix, @NotNull ICommand command) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("`").append(prefix).append(command.getName());
        StringBuilder paramBuilder = new StringBuilder();
        embedBuilder.setTitle(command.getName().toUpperCase() + "");
        ArrayList<CommandUsage> usages = command.getUsage().getUsages();

        for (int i = 0; i < usages.size(); i++) {
            CommandUsage usage = usages.get(i);
            stringBuilder.append(" <").append(usage.getType()).append(">");

            paramBuilder.append("***").append(i + 1).append("***. ").append(usage.getName());

            if (usage.isRequired()) {
                paramBuilder.append(" - *__REQUIRED__*\n");
            } else if (!usage.isRequired()) {
                paramBuilder.append(" - *__OPTIONAL__*\n");
            }
        }
        stringBuilder.append("`");

        embedBuilder.addField("Usage", stringBuilder.toString(), false);

        if (!usages.isEmpty()) {
            embedBuilder.addField("Arguments", paramBuilder.toString(), false);
        }

        if (!command.getAliases().isEmpty()) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("`");
            for (int i = 0; i < command.getAliases().size(); i++) {
                stringBuilder.append(command.getAliases().get(i));
                if (i != command.getAliases().size() - 1) {
                    stringBuilder.append(", ");
                }
            }
            stringBuilder.append("`");
            embedBuilder.addField("Aliases", stringBuilder.toString(), false);
        }

        embedBuilder.addField("Example", command.exampleUsage(prefix), false);
        embedBuilder.addField("Description", command.getDescription(), false);
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        embedBuilder.setFooter("If you need more help with commands, visit https://www.github.com/beanbeanjuice/cafeBot!");
        return embedBuilder.build();
    }

    @NotNull
    private MessageEmbed searchCategoriesEmbed(@NotNull String prefix, @NotNull CategoryType categoryType) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        int count = 1;

        for (ICommand command : CafeBot.getCommandManager().getCommands()) {
            if (command.getCategoryType().equals(categoryType)) {
                stringBuilder.append("**").append(count++).append("** `").append(prefix).append(command.getName());
                stringBuilder.append("`\n");
            }
        }

        if (count == 1) {
            stringBuilder.append("There are no commands here right now :( but if this section is here, that means I'm working on it!");
        }

        embedBuilder.addField("**Commands in " + categoryType + "**", stringBuilder.toString(), true);
        embedBuilder.setThumbnail(categoryType.getLink());
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        embedBuilder.setFooter("For help with a specific command, do " + prefix + "help (command name).");
        return embedBuilder.build();
    }

    @NotNull
    private MessageEmbed categoryEmbed(@NotNull String prefix) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        int count = 1;

        for (CategoryType categoryType : CategoryType.values()) {
            stringBuilder.append("**").append(count++).append("** `").append(categoryType.toString());

            stringBuilder.append("`\n");
        }

        embedBuilder.addField("**Command Categories**", stringBuilder.toString(), true);
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        embedBuilder.setFooter("If you're stuck, use " + prefix + "help (category name/number).");
        return embedBuilder.build();
    }

    @NotNull
    private MessageEmbed noCommandFoundEmbed(@NotNull String commandName, @NotNull String prefix) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("No Command Found");
        embedBuilder.setDescription("No command has been found for `" + commandName + "`.");
        embedBuilder.setColor(Color.red);
        embedBuilder.setFooter("Please see " + prefix + "help!");
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("h");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Shows the list of commands.";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "help` or `" + prefix + "help moderation` or `" + prefix + "help bug-report`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.TEXT, "Command/Section Name", false);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.GENERIC;
    }

}

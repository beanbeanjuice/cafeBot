package com.beanbeanjuice.command.generic;

import com.beanbeanjuice.main.CafeBot;
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
            channel.sendMessage(categoryEmbed()).queue(); // Sends the list of categories.
            return;
        }

        String search = args.get(0);

        // Goes through each category. If the first argument is equal to the name, then print commands for that category.
        for (CategoryType categoryType : CategoryType.values()) {
            if (categoryType.toString().equalsIgnoreCase(search)) {
                channel.sendMessage(searchCategoriesEmbed(prefix, categoryType)).queue();
                return;
            }
        }

        ICommand command = CafeBot.getCommandManager().getCommand(search);

        // Checks to see if any commands exist for that command.
        if (command == null) {
            channel.sendMessage(noCommandFoundEmbed(search)).queue();
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
        embedBuilder.setAuthor(command.getName().toUpperCase() + " Command");
        ArrayList<CommandUsage> usages = command.getUsage().getUsages();
        paramBuilder.append("`");

        for (int i = 0; i < usages.size(); i++) {
            if (i < 5) {
                stringBuilder.append(" <").append("Parameter ").append(i + 1).append(">");

                CommandUsage usage = usages.get(i);
                paramBuilder.append(i + 1).append(". ").append("<DESCRIPTION:").append(usage.getName()).append(">:")
                        .append("<TYPE:").append(usage.getType().getDescription()).append(">:");

                if (usage.isRequired()) {
                    paramBuilder.append("<REQUIRED>\n");
                } else if (!usage.isRequired()) {
                    paramBuilder.append("<OPTIONAL>\n");
                }
            }

            if (i == 5) {
                paramBuilder.append("\n There are more usages... but it won't fit. If you see this message, just add a sentence " +
                        "instead of a parameter.");
            }
        }
        stringBuilder.append("`");
        paramBuilder.append("`");

        embedBuilder.addField("Usage", stringBuilder.toString(), false);

        if (!usages.isEmpty()) {
            embedBuilder.addField("Parameters", paramBuilder.toString(), false);
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
            embedBuilder.addField("Command Aliases", stringBuilder.toString(), false);
        }

        embedBuilder.addField("Command Example", command.exampleUsage(), false);
        embedBuilder.addField("Command Description", command.getDescription(), false);
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        embedBuilder.setFooter("If you need more help with commands, visit https://www.github.com/beanbeanjuice/beanBot!");
        return embedBuilder.build();
    }

    @NotNull
    private MessageEmbed searchCategoriesEmbed(@NotNull String prefix, @NotNull CategoryType categoryType) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        int count = 1;

        for (ICommand command : CafeBot.getCommandManager().getCommands()) {
            if (command.getCategoryType().equals(categoryType)) {
                stringBuilder.append(count++).append(". ").append("`").append(prefix).append(command.getName());
                stringBuilder.append("`\n");
            }
        }

        embedBuilder.addField("**Commands in " + categoryType.toString() + "**", stringBuilder.toString(), true);
        embedBuilder.setThumbnail(categoryType.getLink());
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        embedBuilder.setFooter("If you need more help with commands, visit https://www.github.com/beanbeanjuice/beanBot!");
        return embedBuilder.build();
    }

    private MessageEmbed categoryEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        int count = 1;

        for (CategoryType categoryType : CategoryType.values()) {
            stringBuilder.append(count++).append(". ").append("`").append(categoryType.toString());

            stringBuilder.append("`\n");
        }

        embedBuilder.addField("**Command Categories**", stringBuilder.toString(), true);
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        embedBuilder.setFooter("If you need more help with commands, visit https://www.github.com/beanbeanjuice/beanBot!");
        return embedBuilder.build();
    }

    @NotNull
    private MessageEmbed noCommandFoundEmbed(@NotNull String commandName) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("No Command Found");
        embedBuilder.setDescription("No command has been found for `" + commandName + "`.");
        embedBuilder.setColor(Color.red);
        embedBuilder.setFooter("If you need more help with commands, visit https://www.github.com/beanbeanjuice/beanBot!");
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
    public String exampleUsage() {
        return "`!!help` or `!!help moderation` or `!!help bug-report`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.TEXT, "command name", false);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.GENERIC;
    }

}

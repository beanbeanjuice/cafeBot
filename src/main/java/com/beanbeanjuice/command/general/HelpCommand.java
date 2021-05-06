package com.beanbeanjuice.command.general;

import com.beanbeanjuice.main.BeanBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.CommandManager;
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

        event.getMessage().delete().queue();

        TextChannel channel = event.getChannel();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        StringBuilder builder = new StringBuilder();
        String prefix = ctx.getPrefix();

        if (args.isEmpty()) {
            int count = 1;

            for (CategoryType categoryType : CategoryType.values()) {
                builder.append(count++).append(".").append("`").append(categoryType.toString());

                builder.append("`\n");
            }

            embedBuilder.addField("**Command Categories**", builder.toString(), true);
            embedBuilder.setColor(BeanBot.getGeneralHelper().getRandomColor());
            channel.sendMessage(embedBuilder.build()).queue();

            return;
        }

        if (args.get(0).equalsIgnoreCase("play")) {
            embedBuilder.setAuthor("PLAY Command");
            embedBuilder.addField("Usage", prefix + "play", false);

            ICommand command = BeanBot.getCommandManager().getCommand("play");

            builder = new StringBuilder();
            builder.append("`");
            for (int i = 0; i < command.getAliases().size(); i++) {
                builder.append(command.getAliases().get(i));
                if (i != command.getAliases().size() - 1) {
                    builder.append(", ");
                }
            }
            builder.append("`");
            embedBuilder.addField("Usage", "`1. <DESCRIPTION:Any text/link>:<TYPE:LINK/TEXT>:<OPTIONAL>`", false);
            embedBuilder.addField("Command Aliases", builder.toString(), false);
            embedBuilder.addField("Command Description", command.getDescription(), false);
            embedBuilder.setColor(BeanBot.getGeneralHelper().getRandomColor());
            event.getChannel().sendMessage(embedBuilder.build()).queue();
            return;
        }

        String search = args.get(0);

        for (CategoryType categoryType : CategoryType.values()) {

            if (categoryType.toString().equalsIgnoreCase(search)) {

                int count = 1;

                for (ICommand command : BeanBot.getCommandManager().getCommands()) {

                    if (command.getCategoryType().equals(categoryType)) {

                        builder.append(count++).append(".").append("`").append(prefix).append(command.getName());

                        builder.append("`\n");
                    }
                }

                embedBuilder.addField("**Commands in " + categoryType.toString() + "**", builder.toString(), true);
                embedBuilder.setThumbnail(categoryType.getLink());
                embedBuilder.setColor(BeanBot.getGeneralHelper().getRandomColor());

                channel.sendMessage(embedBuilder.build()).queue();
                return;

            }

        }

        ICommand command = BeanBot.getCommandManager().getCommand(search);

        if (command == null) {
            channel.sendMessage(noCommandFoundEmbed(search)).queue();
            return;
        }

        // Logic to show command and optional parameters.
        builder.append("`").append(prefix).append(command.getName());
        StringBuilder paramBuilder = new StringBuilder();
        embedBuilder.setAuthor(command.getName().toUpperCase() + " Command");
        ArrayList<CommandUsage> usages = command.getUsage().getUsages();
        paramBuilder.append("`");

        for (int i = 0; i < usages.size(); i++) {
            builder.append(" <").append("Parameter ").append(i + 1).append(">");

            CommandUsage usage = usages.get(i);
            paramBuilder.append(i+1).append(". ").append("<DESCRIPTION:").append(usage.getName()).append(">:")
                    .append("<TYPE:").append(usage.getType().getDescription()).append(">:");

            if (usage.isRequired()) {
                paramBuilder.append("<REQUIRED>\n");
            } else if (!usage.isRequired()) {
                paramBuilder.append("<OPTIONAL>\n");
            }
        }
        builder.append("`");
        paramBuilder.append("`");

        embedBuilder.addField("Usage", builder.toString(), false);

        if (!usages.isEmpty()) {
            embedBuilder.addField("Parameters", paramBuilder.toString(), false);
        }

        if (!command.getAliases().isEmpty()) {
            builder = new StringBuilder();
            builder.append("`");
            for (int i = 0; i < command.getAliases().size(); i++) {
                builder.append(command.getAliases().get(i));
                if (i != command.getAliases().size() - 1) {
                    builder.append(", ");
                }
            }
            builder.append("`");
            embedBuilder.addField("Command Aliases", builder.toString(), false);
        }

        embedBuilder.addField("Command Description", command.getDescription(), false);
        embedBuilder.setColor(BeanBot.getGeneralHelper().getRandomColor());

        channel.sendMessage(embedBuilder.build()).queue();

    }

    @NotNull
    private MessageEmbed noCommandFoundEmbed(@NotNull String commandName) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("No Command Found");
        embedBuilder.setDescription("No command has been found for `" + commandName + "`.");
        embedBuilder.setColor(Color.red);
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

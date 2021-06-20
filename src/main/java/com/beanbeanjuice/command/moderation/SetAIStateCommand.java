package com.beanbeanjuice.command.moderation;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to set the AI state for the bot.
 *
 * @author beanbeanjuice
 */
public class SetAIStateCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {

        if (!CafeBot.getGeneralHelper().isAdministrator(event.getMember(), event)) {
            return;
        }

        if (args.isEmpty()) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().successEmbed(
                    "Current AI State",
                    "The current AI state is: `" + CafeBot.getGuildHandler().getCustomGuild(event.getGuild()).getAIState() + "`."
            )).queue();
            return;
        }

        String commandTerm = args.get(0);
        if (!commandTerm.equalsIgnoreCase("enable") && !commandTerm.equalsIgnoreCase("disable")) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().errorEmbed(
                    "Incorrect Arguments",
                    "The correct additional terms for this command are `enable` and `disable`."
            )).queue();
            return;
        }

        Boolean newAiState = commandTerm.equalsIgnoreCase("enable");
        if (CafeBot.getGuildHandler().getCustomGuild(event.getGuild()).setAIState(newAiState)) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().successEmbed(
                    "AI Updated",
                    "The AI state has been set to: `" + newAiState + "`."
            )).queue();
            return;
        }

        event.getChannel().sendMessage(CafeBot.getGeneralHelper().sqlServerError()).queue();

    }

    @Override
    public String getName() {
        return "ai-status";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("ai");
        arrayList.add("ai-state");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Enable or Disable the current AI behavior. When this is on, this allows the bot to respond to basic things such as `hello` or `hi`.";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + " ai-state enable` or `" + prefix + " ai-state disable`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.TEXT, "Enable/Disable", false);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.MODERATION;
    }
}

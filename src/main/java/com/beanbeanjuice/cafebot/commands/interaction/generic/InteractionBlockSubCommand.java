package com.beanbeanjuice.cafebot.commands.interaction.generic;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.i18n.I18N;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.concurrent.CompletionException;

public class InteractionBlockSubCommand extends Command implements ISubCommand {

    public InteractionBlockSubCommand(CafeBot bot) {
        super(bot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        final I18N bundle = ctx.getUserI18n();
        User userToBlock = event.getOption("user").getAsUser(); // Should not be null.

        bot.getCafeAPI().getInteractionsApi().blockUser(event.getUser().getId(), userToBlock.getId()).thenAccept((blockList) -> {
            String description = bundle.getString("command.interaction.block.success.description")
                    .replace("{user}", userToBlock.getAsMention())
                    .replace("{count}", String.valueOf(blockList.size()));
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    bundle.getString("command.interaction.block.success.title"),
                    description
            )).queue();
        }).exceptionally((ex) -> {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    bundle.getString("command.interaction.block.error.title"),
                    bundle.getString("command.interaction.block.error.description")
            )).queue();

            bot.getLogger().log(this.getClass(), LogLevel.WARN, String.format("Error Blocking User: %s", ex.getMessage()), true, true);
            throw new CompletionException(ex);
        });
    }

    @Override
    public String getName() {
        return "block";
    }

    @Override
    public String getDescriptionPath() {
        return "command.interaction.block.description";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "command.interaction.block.arguments.user.description", true)
        };
    }

}

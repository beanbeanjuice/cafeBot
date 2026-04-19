package com.beanbeanjuice.cafebot.commands.moderation;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.i18n.I18N;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.concurrent.CompletableFuture;

public class ClearChatCommand extends Command implements ICommand {

    public ClearChatCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        final I18N bundle = ctx.getUserI18n();
        int amount = event.getOption("amount").getAsInt();
        MessageChannel channel = event.getChannel();

        channel.getHistory().retrievePast(amount).queue((messages) -> {
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    bundle.getString("command.clearchat.deleting.title"),
                    bundle.getString("command.clearchat.deleting.description").replace("{amount}", String.valueOf(amount))
            )).queue();

            CompletableFuture.allOf(channel.purgeMessages(messages).toArray(new CompletableFuture[0])).thenAcceptAsync((ignored) -> {
                event.getHook().editOriginalEmbeds(Helper.successEmbed(
                        bundle.getString("command.clearchat.deleted.title"),
                        bundle.getString("command.clearchat.deleted.description").replace("{amount}", String.valueOf(amount))
                )).setReplace(true).queue();
            });
        });
    }

    @Override
    public String getName() {
        return "clearchat";
    }

    @Override
    public String getDescriptionPath() {
        return "command.clearchat.description";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.MODERATION;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.INTEGER, "amount", "command.clearchat.arguments.amount.description", true)
                        .setRequiredRange(1, 100)
        };
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[] {
                Permission.MESSAGE_MANAGE
        };
    }

    @Override
    public boolean isEphemeral() {
        return true;
    }

    @Override
    public boolean isNSFW() {
        return false;
    }

    @Override
    public boolean allowDM() {
        return false;
    }

}

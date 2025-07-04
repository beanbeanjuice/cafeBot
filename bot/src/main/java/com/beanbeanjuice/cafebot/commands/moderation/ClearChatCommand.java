package com.beanbeanjuice.cafebot.commands.moderation;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
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
    public void handle(SlashCommandInteractionEvent event) {
        int amount = event.getOption("amount").getAsInt();
        MessageChannel channel = event.getChannel();

        channel.getHistory().retrievePast(amount).queue((messages) -> {
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    "Deleting Messages...",
                    String.format("Attempting to delete **%d** messages.", amount)
            )).queue();

            CompletableFuture.allOf(channel.purgeMessages(messages).toArray(new CompletableFuture[0])).thenAcceptAsync((ignored) -> {
                event.getHook().editOriginalEmbeds(Helper.successEmbed(
                        "Messages Deleted",
                        String.format("**%d** messages have been successfully deleted.", amount)
                )).setReplace(true).queue();
            });
        });
    }

    @Override
    public String getName() {
        return "clearchat";
    }

    @Override
    public String getDescription() {
        return "Clear the chat!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.MODERATION;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.INTEGER, "amount", "The amount of messages you want to clear.", true)
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

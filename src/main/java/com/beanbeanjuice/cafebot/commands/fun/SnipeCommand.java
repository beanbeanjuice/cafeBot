package com.beanbeanjuice.cafebot.commands.fun;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.i18n.I18N;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.types.PotentialSnipeMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class SnipeCommand extends Command implements ICommand {

    public SnipeCommand(CafeBot bot) {
        super(bot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        bot.getSnipeHandler().popLastMessage(event.getChannelId()).ifPresentOrElse(
                (snipedMessage) -> event.getHook().sendMessageEmbeds(this.getSnipedMessageEmbed(snipedMessage, ctx.getGuildI18n())).queue(),

                () -> event.getHook().sendMessageEmbeds(this.getNoSnipeMessageEmbed(ctx.getGuildI18n())).queue()
        );
    }

    private MessageEmbed getSnipedMessageEmbed(PotentialSnipeMessage snipe, I18N guildBundle) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        String title = guildBundle.getString("command.snipe.embed.snipe.title");
        String description = guildBundle.getString("command.snipe.embed.snipe.description")
                .replace("{user}", snipe.getUser().getAsMention())
                .replace("{message}", snipe.getMessage());

        embedBuilder.setTitle(title);
        embedBuilder.setAuthor(snipe.getUser().getName(), null, snipe.getUser().getAvatarUrl());
        embedBuilder.setColor(Helper.getRandomColor());
        embedBuilder.setDescription(description);
        embedBuilder.setTimestamp(snipe.getCreatedAt());

        return embedBuilder.build();
    }

    private MessageEmbed getNoSnipeMessageEmbed(I18N guildBundle) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        String title = guildBundle.getString("command.snipe.embed.none.title");
        String description = guildBundle.getString("command.snipe.embed.none.description");

        embedBuilder.setTitle(title);
        embedBuilder.setColor(Helper.getRandomColor());
        embedBuilder.setDescription(description);

        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "snipe";
    }

    @Override
    public String getDescriptionPath() {
        return "command.snipe.description";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.FUN;
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[] {
                Permission.MESSAGE_HISTORY,
        };
    }

    @Override
    public boolean isEphemeral() {
        return false;
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

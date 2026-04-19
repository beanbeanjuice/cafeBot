package com.beanbeanjuice.cafebot.commands.generic;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.i18n.I18N;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class InfoCommand extends Command implements ICommand {

    public InfoCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        event.getHook().sendMessageEmbeds(infoEmbed(ctx.getUserI18n())).queue();
    }

    private MessageEmbed infoEmbed(final I18N bundle) {
        return new EmbedBuilder()
                .setColor(Helper.getRandomColor())
                .setAuthor(bundle.getString("command.info.embed.author"), null, bot.getSelfUser().getAvatarUrl())
                .addField(bundle.getString("command.info.embed.commands-run"), String.format("```%s```", Helper.formatNumber(bot.getCommandsRun().get())), true)
                .addField(bundle.getString("command.info.embed.creator"), bundle.getString("command.info.embed.creator-value"), true)
                .addField(bundle.getString("command.info.embed.frameworks"), bundle.getString("command.info.embed.frameworks-value"), true)
                .addField(bundle.getString("command.info.embed.about"), bundle.getString("command.info.embed.about-description"), false)
                .setFooter(bundle.getString("command.info.embed.footer"))
                .build();
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescriptionPath() {
        return "command.info.description";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.GENERIC;
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[0];
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
        return true;
    }

}

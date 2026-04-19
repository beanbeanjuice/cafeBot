package com.beanbeanjuice.cafebot.commands.social;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class MemberCountCommand extends Command implements ICommand {

    public MemberCountCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        event.getGuild().retrieveMetaData().queue((metadata) -> {
            String description = ctx.getDefaultBundle().getString("command.membercount.embed.description")
                    .replace("{count}", String.valueOf(metadata.getApproximateMembers()));
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    ctx.getDefaultBundle().getString("command.membercount.embed.title"),
                    description
            )).queue();
        });
    }

    @Override
    public String getName() {
        return "membercount";
    }

    @Override
    public String getDescriptionPath() {
        return "command.membercount.description";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.SOCIAL;
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[0];
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

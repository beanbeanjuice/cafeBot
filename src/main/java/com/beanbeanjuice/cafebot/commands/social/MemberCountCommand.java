package com.beanbeanjuice.cafebot.commands.social;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class MemberCountCommand extends Command implements ICommand {

    public MemberCountCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        int members = guild.getMemberCount();

        event.getHook().sendMessageEmbeds(Helper.successEmbed(
                "Member Count",
                String.format("There are %d people in this server!", members)
        )).queue();
    }

    @Override
    public String getName() {
        return "membercount";
    }

    @Override
    public String getDescription() {
        return "Get the member count for your server!";
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

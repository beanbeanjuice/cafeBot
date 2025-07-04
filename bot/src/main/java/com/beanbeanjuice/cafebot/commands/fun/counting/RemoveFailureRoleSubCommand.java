package com.beanbeanjuice.cafebot.commands.fun.counting;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.counting.CountingEndpoint;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class RemoveFailureRoleSubCommand extends Command implements ISubCommand {

    public RemoveFailureRoleSubCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        if (!hasPermission(event.getMember())) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Insufficient Permissions",
                    "You don't have permission to edit the failure role."
            )).queue();
            return;
        }

        String guildID = event.getGuild().getId();
        CountingEndpoint endpoint = cafeBot.getCafeAPI().getCountingEndpoint();
        endpoint.getGuildCountingInformation(guildID)
                .thenComposeAsync((information) -> {
                    return endpoint.updateGuildCountingInformation(
                            guildID, information.getHighestNumber(), information.getLastNumber(), information.getLastUserID(), "0"
                    );
                })
                .thenRunAsync(() -> event.getHook().sendMessageEmbeds(Helper.successEmbed(
                        "Failure Role Removed",
                        "The failure role has been successfully removed."
                )).queue());
    }

    private boolean hasPermission(Member member) {
        return member.hasPermission(
                Permission.MANAGE_ROLES
        );
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescription() {
        return "Remove the failure role.";
    }
}

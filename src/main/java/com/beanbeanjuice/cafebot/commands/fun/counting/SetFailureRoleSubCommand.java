package com.beanbeanjuice.cafebot.commands.fun.counting;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.counting.CountingEndpoint;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class SetFailureRoleSubCommand extends Command implements ISubCommand {

    public SetFailureRoleSubCommand(final CafeBot cafeBot) {
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

        Role role = event.getOption("role").getAsRole();
        String guildID = event.getGuild().getId();
        CountingEndpoint endpoint = cafeBot.getCafeAPI().getCountingEndpoint();
        endpoint.getGuildCountingInformation(guildID)
                .thenComposeAsync((information) -> endpoint.updateGuildCountingInformation(
                        guildID,
                        information.getHighestNumber(),
                        information.getLastNumber(),
                        information.getLastUserID(),
                        role.getId())
                )
                .thenAcceptAsync((ignored) -> {
                    event.getHook().sendMessageEmbeds(Helper.successEmbed(
                            "Failure Role Set",
                            String.format("The failure role has been successfully set to %s", role.getAsMention())
                    )).queue();
                })
                .exceptionallyAsync((e) -> {
                    event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                            "Error Setting Failure Role",
                            "There was an error setting the failure role. Is the counting channel even set?"
                    )).queue();
                    return null;
                });
    }

    private boolean hasPermission(Member member) {
        return member.hasPermission(
                Permission.MANAGE_ROLES
        );
    }

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String getDescription() {
        return "Set the failure role. This role is given when someone messes up the counting channel.";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.ROLE, "role", "The role to give someone when they mess up counting.", true)
        };
    }
}

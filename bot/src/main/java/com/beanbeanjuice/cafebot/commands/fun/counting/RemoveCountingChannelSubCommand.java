package com.beanbeanjuice.cafebot.commands.fun.counting;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.guilds.GuildInformationType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class RemoveCountingChannelSubCommand extends Command implements ISubCommand {

    public RemoveCountingChannelSubCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        if (!this.hasPermission(event.getMember())) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Insufficient Permissions",
                    "You don't have permission to edit the counting channel."
            )).queue();
            return;
        }

        String guildID = event.getGuild().getId();
        cafeBot.getCafeAPI().getGuildsEndpoint()
                .updateGuildInformation(guildID, GuildInformationType.COUNTING_CHANNEL_ID, "0")
                .thenRunAsync(() -> event.getHook().sendMessageEmbeds(Helper.successEmbed(
                        "Counting Channel Removed",
                        "The counting channel has been successfully removed."
                )).queue());
    }

    private boolean hasPermission(Member member) {
        return member.hasPermission(
                Permission.MANAGE_CHANNEL,
                Permission.MANAGE_SERVER
        );
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescription() {
        return "Remove the counting channel for this server.";
    }

}

package com.beanbeanjuice.cafebot.commands.social.vent;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.guilds.GuildInformationType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class RemoveVentChannelSubCommand extends Command implements ISubCommand {

    public RemoveVentChannelSubCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        if (!hasPermission(event.getMember())) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Insufficient Permission",
                    "You do not have permission to remove the venting channel."
            )).queue();
            return;
        }

        String guildID = event.getGuild().getId();

        cafeBot.getCafeAPI().getGuildsEndpoint()
                .updateGuildInformation(guildID, GuildInformationType.VENTING_CHANNEL_ID, "0")
                .thenAcceptAsync((ignored) -> event.getHook().sendMessageEmbeds(Helper.successEmbed(
                        "Venting Channel Removed",
                        "The venting channel has been successfully removed."
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
        return "Remove the venting channel.";
    }
}

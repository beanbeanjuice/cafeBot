package com.beanbeanjuice.cafebot.commands.fun.birthday;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.guilds.GuildInformationType;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.guilds.GuildsEndpoint;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class RemoveBirthdayChannelSubCommand extends Command implements ISubCommand {

    public RemoveBirthdayChannelSubCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        if (!event.isFromGuild()) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Must Be In Server",
                    "You must be in a server to use this command."
            )).queue();
            return;
        }

        if (!this.hasPermission(event.getMember())) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Insufficient Permissions",
                    "You don't have permission to use this command."
            )).queue();
            return;
        }

        GuildsEndpoint guildsEndpoint = cafeBot.getCafeAPI().getGuildsEndpoint();
        guildsEndpoint
                .updateGuildInformation(event.getGuild().getId(), GuildInformationType.BIRTHDAY_CHANNEL_ID, "0")
                .thenRunAsync(() -> event.getHook().sendMessageEmbeds(Helper.successEmbed(
                        "Removed Birthday Channel",
                        "The birthday channel is no longer set!"
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
        return "Remove the birthday channel from the server!";
    }

}

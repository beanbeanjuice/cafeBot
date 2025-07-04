package com.beanbeanjuice.cafebot.commands.fun.counting;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.guilds.GuildInformationType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Optional;

public class SetCountingChannelSubCommand extends Command implements ISubCommand {

    public SetCountingChannelSubCommand(final CafeBot cafeBot) {
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
        Optional<OptionMapping> channelMapping = Optional.ofNullable(event.getOption("channel"));
        GuildChannelUnion channel = channelMapping.map(OptionMapping::getAsChannel).orElse((GuildChannelUnion) event.getChannel());

        cafeBot.getCafeAPI().getGuildsEndpoint()
                .updateGuildInformation(guildID, GuildInformationType.COUNTING_CHANNEL_ID, channel.getId())
                .thenRunAsync(() -> event.getHook().sendMessageEmbeds(Helper.successEmbed(
                        "Counting Channel Set",
                        String.format("The counting channel has been set to %s.", channel.getAsMention())
                )).queue());

        cafeBot.getCafeAPI().getCountingEndpoint().createGuildCountingInformation(guildID);
    }

    private boolean hasPermission(Member member) {
        return member.hasPermission(
                Permission.MANAGE_CHANNEL,
                Permission.MANAGE_SERVER
        );
    }

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String getDescription() {
        return "Set the counting channel for this server.";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.CHANNEL, "channel", "The channel you want to set to the counting channel.", false)
        };
    }

}

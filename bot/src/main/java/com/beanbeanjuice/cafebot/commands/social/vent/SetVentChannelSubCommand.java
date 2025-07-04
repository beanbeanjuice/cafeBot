package com.beanbeanjuice.cafebot.commands.social.vent;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.guilds.GuildInformationType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Optional;

public class SetVentChannelSubCommand extends Command implements ISubCommand {

    public SetVentChannelSubCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        if (!hasPermission(event.getMember())) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Insufficient Permission",
                    "You don't have permission to set the venting channel."
            )).queue();
            return;
        }

        Optional<OptionMapping> channelMapping = Optional.ofNullable(event.getOption("channel"));
        GuildChannelUnion channel = channelMapping.map(OptionMapping::getAsChannel).orElse((GuildChannelUnion) event.getChannel());
        String guildID = event.getGuild().getId();
        String channelID = channel.getId();

        cafeBot.getCafeAPI().getGuildsEndpoint()
                .updateGuildInformation(guildID, GuildInformationType.VENTING_CHANNEL_ID, channelID)
                .thenAcceptAsync((ignored) -> event.getHook().sendMessageEmbeds(Helper.successEmbed(
                        "Venting Channel Set",
                        String.format("The venting channel has been successfully set to %s.", channel.getAsMention())
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
        return "set";
    }

    @Override
    public String getDescription() {
        return "Set the anonymous venting channel!";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.CHANNEL, "channel", "The channel you want to set the venting channel to.")
        };
    }

}

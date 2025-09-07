package com.beanbeanjuice.cafebot.commands.fun;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Optional;

public class AvatarCommand extends Command implements ICommand {

    public AvatarCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        Optional<OptionMapping> userOption = Optional.ofNullable(event.getOption("user"));
        Optional<OptionMapping> serverOption = Optional.ofNullable(event.getOption("server"));

        User user = userOption.map(OptionMapping::getAsUser).orElse(event.getUser());
        Member member = userOption.map(OptionMapping::getAsMember).orElse(event.getMember());
        boolean useServer = serverOption.map(OptionMapping::getAsBoolean).orElse(false);

        if (useServer && !event.isFromGuild()) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Must Be In Server",
                    "You must be in a Discord server to use the server avatar!"
            )).queue();
            return;
        }

        Optional<String> urlOptional = (useServer) ? getServerAvatarURL(member) : getUserAvatarURL(user);

        urlOptional.ifPresentOrElse(
                (url) -> event.getHook().sendMessageEmbeds(avatarEmbed(user.getName(), url)).queue(),
                () -> event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                        "No User Avatar",
                        "The specified user does not have a Discord avatar."
                )).queue()
        );
    }

    private Optional<String> getUserAvatarURL(final User user) {
        return Optional.ofNullable(user.getAvatarUrl());
    }

    private Optional<String> getServerAvatarURL(final Member member) {
        return Optional.ofNullable(member.getAvatarUrl());
    }

    private MessageEmbed avatarEmbed(final String name, final String avatarURL) {
        return new EmbedBuilder()
                .setTitle(name + "'s Avatar")
                .setImage(avatarURL + "?size=512")
                .setColor(Helper.getRandomColor())
                .build();
    }

    @Override
    public String getName() {
        return "avatar";
    }

    @Override
    public String getDescription() {
        return "Get someone's avatar!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.FUN;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "The user you want to get the avatar of.", false),
                new OptionData(OptionType.BOOLEAN, "server", "Whether to get their server or user avatar.", false)
        };
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

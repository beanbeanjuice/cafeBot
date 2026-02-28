package com.beanbeanjuice.cafebot.commands.fun;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.util.Optional;

public class BannerCommand extends Command implements ICommand {

    public BannerCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        Optional<OptionMapping> userOption = Optional.ofNullable(event.getOption("user"));

        User user = userOption.map(OptionMapping::getAsUser).orElse(event.getUser());

        user.retrieveProfile().queue((profile) -> {
            Optional<String> urlOptional = Optional.ofNullable(profile.getBannerUrl());

            urlOptional.ifPresentOrElse(
                    (url) -> event.getHook().sendMessageEmbeds(bannerEmbed(user.getName(), user.getAvatarUrl(), url, profile.getAccentColor())).queue(),
                    () -> event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                            "No User Banner",
                            "The specified user does not have a Discord banner."
                    )).queue()
            );
        });
    }

    private MessageEmbed bannerEmbed(final String username, final String avatarURL, final String bannerURL, final Color accent) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(accent)
                .setAuthor(username + "'s Banner", null, avatarURL);

        embedBuilder.setImage(bannerURL + "?size=600");

        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "banner";
    }

    @Override
    public String getDescriptionPath() {
        return "Get someone's banner!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.FUN;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "The person you want to get the banner of.", false),
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

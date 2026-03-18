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
import java.util.ResourceBundle;

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
                    (url) -> event.getHook().sendMessageEmbeds(bannerEmbed(user.getName(), user.getAvatarUrl(), url, profile.getAccentColor(), ctx.getUserI18n())).queue(),
                    () -> event.getHook().sendMessageEmbeds(noBannerError(ctx.getUserI18n())).queue()
            );
        });
    }

    private MessageEmbed bannerEmbed(String username, String avatarURL, String bannerURL, Color accent, ResourceBundle i18n) {
        String title = i18n.getString("command.banner.embed.title").replace("{user}", username);

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(accent)
                .setAuthor(title, null, avatarURL);

        embedBuilder.setImage(bannerURL + "?size=600");

        return embedBuilder.build();
    }

    private MessageEmbed noBannerError(ResourceBundle i18n) {
        String title = i18n.getString("command.banner.error.title");
        String description = i18n.getString("command.banner.error.description");

        return Helper.errorEmbed(title, description);
    }

    @Override
    public String getName() {
        return "banner";
    }

    @Override
    public String getDescriptionPath() {
        return "command.banner.description";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.FUN;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "command.banner.arguments.user.description", false),
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

package com.beanbeanjuice.cafebot.command.fun;

import com.beanbeanjuice.cafebot.utility.command.CommandCategory;
import com.beanbeanjuice.cafebot.utility.command.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to get someone's Discord avatar.
 *
 * @author beanbeanjuice
 */
public class AvatarCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        String type = event.getOption("type").getAsString();

        String name = "";
        String url = "";

        if (type.equals("USER")) {
            User user = event.getUser();
            if (event.getOption("user") != null)
                user = event.getOption("user").getAsUser();
            name = user.getName();
            url = user.getAvatarUrl();

            if (url == null) {
                event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                        "No User Avatar",
                        "The specified user does not have a Discord avatar."
                )).queue();
                return;
            }
        } else {
            Member member = event.getMember();
            if (event.getOption("user") != null)
                member = event.getOption("user").getAsMember();
            name = member.getUser().getName();
            url = member.getAvatarUrl();

            if (url == null) {
                event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                        "No User Server Avatar",
                        "The specified user does not have a custom avatar for this server."
                )).queue();
                return;
            }
        }

        event.getHook().sendMessageEmbeds(avatarEmbed(name, url)).queue();
    }

    @NotNull
    private MessageEmbed avatarEmbed(@NotNull String name, @NotNull String avatarURL) {
        return new EmbedBuilder()
                .setTitle(name + "'s Avatar")
                .setImage(avatarURL + "?size=512")
                .setColor(Helper.getRandomColor())
                .build();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Get the avatar of a user!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/avatar` or `/avatar @beanbeanjuice`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "type", "Get their user or server avatar.", true, false)
                .addChoice("User Avatar", "USER")
                .addChoice("Server Avatar", "SERVER"));
        options.add(new OptionData(OptionType.USER, "user", "The user to get the avatar of.", false, false));
        return options;
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.FUN;
    }

    @NotNull
    @Override
    public Boolean allowDM() {
        return true;
    }

}

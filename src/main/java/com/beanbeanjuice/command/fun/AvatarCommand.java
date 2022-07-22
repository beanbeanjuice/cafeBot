package com.beanbeanjuice.command.fun;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
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
        User user = event.getUser();

        if (event.getOption("user") != null)
            user = event.getOption("user").getAsUser();

        event.getHook().sendMessageEmbeds(avatarEmbed(user)).queue();
    }

    @NotNull
    private MessageEmbed avatarEmbed(@NotNull User user) {
        return new EmbedBuilder()
                .setTitle(user.getName() + "'s Avatar")
                .setImage(user.getAvatarUrl() + "?size=512")
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

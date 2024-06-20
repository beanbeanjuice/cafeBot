package com.beanbeanjuice.cafebot.command.social;

import com.beanbeanjuice.cafebot.utility.command.CommandCategory;
import com.beanbeanjuice.cafebot.utility.command.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link ICommand} used to count the {@link net.dv8tion.jda.api.entities.Member Members} in the {@link net.dv8tion.jda.api.entities.Guild Guild}.
 *
 * @author beanbeanjuice
 */
public class CountMembersCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        int count = 0;

        for (Member member : event.getGuild().getMembers()) {
            if (!member.getUser().isBot())
                count++;
        }

        event.getHook().sendMessageEmbeds(Helper.successEmbed(
                "Member Count",
                "You currently have `" + count + "` members in your server!"
        )).queue();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Count the members in the server!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/count-members`";
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.SOCIAL;
    }

    @NotNull
    @Override
    public Boolean isHidden() {
        return true;
    }
}

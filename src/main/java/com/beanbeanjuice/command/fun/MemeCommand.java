package com.beanbeanjuice.command.fun;

import com.beanbeanjuice.utility.api.SubRedditAPI;
import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.helper.Helper;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to send a meme.
 *
 * @author beanbeanjuice
 */
public class MemeCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        event.getHook().sendMessageEmbeds(new SubRedditAPI(getSubreddits().get(Helper.getRandomNumber(0, getSubreddits().size()))).getRedditEmbed()).queue();
    }

    @NotNull
    private ArrayList<String> getSubreddits() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("memes");
        arrayList.add("dankmemes");
        arrayList.add("me_irl");
        arrayList.add("PrequelMemes");
        arrayList.add("thatHappened");
        return arrayList;
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Send a meme!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/meme`";
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

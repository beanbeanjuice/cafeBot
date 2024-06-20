package com.beanbeanjuice.cafebot.command.fun;

import com.beanbeanjuice.cafebot.utility.api.SubRedditAPI;
import com.beanbeanjuice.cafebot.utility.command.CommandCategory;
import com.beanbeanjuice.cafebot.utility.command.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ICommand} to send Tea memes.
 *
 * @author beanbeanjuice
 */
public class TeaMemeCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        event.getHook().sendMessageEmbeds(new SubRedditAPI(getSubreddits().get(Helper.getRandomNumber(0, getSubreddits().size()))).getRedditEmbed()).queue();
    }

    @NotNull
    private ArrayList<String> getSubreddits() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("TeaPorn");
        arrayList.add("TeaPictures");
        return arrayList;
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Send a tea meme!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/tea-meme`";
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

package com.beanbeanjuice.cafebot.commands.fun;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.api.RedditAPIWrapper;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class JokeCommand extends Command implements ICommand {

    public JokeCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        int subredditIndex = Helper.getRandomInteger(0, this.getSubreddits().length);
        RedditAPIWrapper redditAPIWrapper = new RedditAPIWrapper(this.cafeBot, this.getSubreddits()[subredditIndex]);

        redditAPIWrapper.getRandom().thenAcceptAsync((embed) -> event.getHook().sendMessageEmbeds(embed).queue());
    }

    private String[] getSubreddits() {
        return new String[] {
                "oneliners",
                "dadjokes",
                "jokes",
                "cleanjokes"
        };
    }

    @Override
    public String getName() {
        return "joke";
    }

    @Override
    public String getDescription() {
        return "Get a random joke!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.FUN;
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[0];
    }

    @Override
    public boolean isEphemeral() {
        return false;
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

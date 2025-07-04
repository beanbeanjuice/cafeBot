package com.beanbeanjuice.cafebot.commands.fun.meme;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.api.RedditAPIWrapper;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class CoffeeMemeSubCommand extends Command implements ISubCommand {

    public CoffeeMemeSubCommand(final CafeBot cafeBot) {
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
                "coffeeporn",
                "coffeememes",
                "coffeewithaview"
        };
    }

    @Override
    public String getName() {
        return "coffee";
    }

    @Override
    public String getDescription() {
        return "Get a coffee meme!";
    }

}

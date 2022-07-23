package com.beanbeanjuice.command.generic;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link ICommand} used to get info on the {@link net.dv8tion.jda.api.JDA bot}.
 *
 * @author beanbeanjuice
 * @since v3.1.0
 */
public class InfoCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        event.getHook().sendMessageEmbeds(infoEmbed()).queue();
    }

    @NotNull
    private MessageEmbed infoEmbed() {
        return new EmbedBuilder()
                .setColor(Helper.getRandomColor())
                .setAuthor("Bot Information", null, Bot.getBot().getSelfUser().getAvatarUrl())
                .addField("⚙ Commands Run", "```" + Bot.commandsRun + "```", true)
                .addField("<a:wowowow:886217210010431508> Creator", "```beanbeanjuice#4595```", true)
                .addField("<:html:1000241652444692530> Frameworks", "Built With: [Discord JDA](https://github.com/DV8FromTheWorld/JDA), " +
                        "[Twitch4J](https://github.com/twitch4j/twitch4j), [KawaiiAPI](https://kawaii.red/), " +
                        "and [Maven](https://maven.apache.org/)!", true)
                .addField("<a:cafeBot:841945919926173707> About Me", "Hello! I'm cafeBot, a general/" +
                        "multipurpose bot that is used to do a multitude of things! You can do `/help` to see the " +
                        "list of my commands. I hope you enjoy me!", false)
                .setFooter("If you want to upvote me, please do so with /bot-upvote!")
                .build();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Get the bot's information!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/info`";
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.GENERIC;
    }

}

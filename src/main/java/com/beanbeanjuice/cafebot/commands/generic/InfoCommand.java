package com.beanbeanjuice.cafebot.commands.generic;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class InfoCommand extends Command implements ICommand {

    public InfoCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        event.getHook().sendMessageEmbeds(infoEmbed()).queue();
    }

    private MessageEmbed infoEmbed() {
        return new EmbedBuilder()
                .setColor(Helper.getRandomColor())
                .setAuthor("Bot Information", null, bot.getSelfUser().getAvatarUrl())
                .addField("⚙ Commands Run", "```" + bot.getCommandsRun() + "```", true)
                .addField("<a:wowowow:886217210010431508> Creator", "```@beanbeanjuice```", true)
                .addField("<:html:1000241652444692530> Frameworks", "Built With: [Discord JDA](https://github.com/DV8FromTheWorld/JDA), " +
                        "[Twitch4J](https://github.com/twitch4j/twitch4j), [KawaiiAPI](https://kawaii.red/), " +
                        "and [Gradle](https://gradle.org/)!", true)
                .addField("<a:cafeBot:1119635469727191190> About Me", "Hello! I'm cafeBot, a general/" +
                        "multipurpose bot that is used to do a multitude of things! You can do `/help` to see the " +
                        "list of my commands. I hope you enjoy me!", false)
                .setFooter("If you want to upvote me, please do so with /bot-upvote!")
                .build();
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "Get information about the bot!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.GENERIC;
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

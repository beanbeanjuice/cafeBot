package com.beanbeanjuice.command.interaction;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.section.interaction.Interaction;
import io.github.beanbeanjuice.cafeapi.cafebot.interactions.InteractionType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to kiss people.
 *
 * @author beanbeanjuice
 */
public class KissCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        new Interaction(InteractionType.KISS,
                "**{sender}** *kissed themselves*! You kissed your hand right **{sender}**? Right?!?",
                "**{sender}** *kissed* **{receiver}**!",
                "{sender} kissed others {amount_sent} times. {receiver} was kissed {amount_received} times.",
                "Y- you can't! I'm working... <:jett_moan:841916896563429386>",
                event);
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Kiss someone.";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/kiss` or `/kiss @beanbeanjuice`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "receiver", "The person to kiss.", false, false));
        options.add(new OptionData(OptionType.STRING, "message", "An optional message to add.", false, false));
        return options;
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.INTERACTION;
    }

    @NotNull
    @Override
    public Boolean allowDM() {
        return false;
    }

}

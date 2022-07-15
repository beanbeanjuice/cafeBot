package com.beanbeanjuice.command.fun.birthday;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.ISubCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * An {@link ICommand} used to get someone's {@link com.beanbeanjuice.cafeapi.cafebot.birthdays.Birthday Birthday}.
 *
 * @author beanbeanjuice
 */
public class BirthdayCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) { }

    @NotNull
    @Override
    public String getDescription() {
        return "Get, set, or remove your birthday!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/birthday get` or `/birthday get @beanbeanjuice` or `/birthday set january 1` or `/birthday remove`";
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.FUN;
    }

    @NotNull
    @Override
    public ArrayList<ISubCommand> getSubCommands() {
        ArrayList<ISubCommand> subCommands = new ArrayList<>();
        subCommands.add(new GetBirthdaySubCommand());
        subCommands.add(new SetBirthdaySubCommand());
        subCommands.add(new RemoveBirthdaySubCommand());
        return subCommands;
    }

    @NotNull
    @Override
    public Boolean allowDM() {
        return true;
    }

    @NotNull
    @Override
    public Boolean isHidden() {
        return true;
    }

    @Nullable
    @Override
    public HashMap<String, ArrayList<String>> getAutoComplete() {
        HashMap<String, ArrayList<String>> autoCompleteMap = new HashMap<>();
        autoCompleteMap.put("timezone", new ArrayList<>());

        for (String timezone : TimeZone.getAvailableIDs())
            autoCompleteMap.get("timezone").add(timezone);

        return autoCompleteMap;
    }
}

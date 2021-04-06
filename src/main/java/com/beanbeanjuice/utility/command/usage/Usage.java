package com.beanbeanjuice.utility.command.usage;

import com.beanbeanjuice.main.BeanBot;
import com.beanbeanjuice.utility.command.usage.types.CommandErrorType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.ArrayList;

/**
 * A class used for containing {@link CommandUsage}.
 *
 * @author beanbeanjuice
 */
public class Usage {

    private final ArrayList<CommandUsage> usages;
    private int incorrectIndex;
    private CommandErrorType errorType;
    private int totalRequired;

    /**
     * Creates a new instance of the {@link Usage} object.
     */
    public Usage() {
        usages = new ArrayList<>();
        totalRequired = 0;
    }

    /**
     * Adds a new {@link CommandUsage} to the {@link Usage} object.
     * @param type The {@link CommandType} for the {@link com.beanbeanjuice.utility.command.ICommand ICommand}.
     * @param name The name of the {@link com.beanbeanjuice.utility.command.ICommand ICommand}.
     * @param required Whether or not the {@link CommandUsage} is required.
     */
    public void addUsage(@NotNull CommandType type, @NotNull String name, @NotNull Boolean required) {
        usages.add(new CommandUsage(name, required, type));

        if (required) {
            totalRequired++;
        }
    }

    /**
     * @return All {@link ArrayList<CommandUsage>} for the {@link com.beanbeanjuice.utility.command.ICommand ICommand}.
     */
    @Nullable
    public ArrayList<CommandUsage> getUsages() {
        return usages;
    }

    /**
     * @param args The {@link com.beanbeanjuice.utility.command.ICommand ICommand} arguments.
     * @return The {@link com.beanbeanjuice.utility.command.usage.types.CommandErrorType} for the {@link com.beanbeanjuice.utility.command.ICommand ICommand}.
     */
    @NotNull
    public CommandErrorType getERROR(@NotNull ArrayList<String> args) {
        int count = 0;
        boolean incorrect;

        if (args.size() == 0 && totalRequired == 0) {
            return CommandErrorType.SUCCESS;
        }

        if (args.size() < totalRequired) {

            if (args.size() == 0) {
                incorrectIndex = 0;
            } else {
                incorrectIndex = args.size() - 1;
            }
            return CommandErrorType.NOT_ENOUGH_ARGUMENTS;
        }

        if (args.size() > usages.size()) {
            incorrectIndex = usages.size();
            return CommandErrorType.TOO_MANY_ARGUMENTS;
        }

        for (CommandUsage entry : usages) {
            CommandType type = entry.getType();

            if (type.equals(CommandType.LINK)) {
                incorrect = !isLink(args.get(count));
                if (incorrect) {
                    incorrectIndex = count;
                    errorType = CommandErrorType.LINK;
                    return getErrorType();
                }
            } else if (type.equals(CommandType.NUMBER)) {
                incorrect = !isNumber(args.get(count));
                if (incorrect) {
                    incorrectIndex = count;
                    errorType = CommandErrorType.NUMBER;
                    return getErrorType();
                }
            } else if (type.equals(CommandType.USER)) {
                incorrect = !isUser(args.get(count));
                if (incorrect) {
                    incorrectIndex = count;
                    errorType = CommandErrorType.USER;
                    return getErrorType();
                }
            } else if (type.equals(CommandType.ROLE)) {
                incorrect = !isRole(args.get(count));
                if (incorrect) {
                    incorrectIndex = count;
                    errorType = CommandErrorType.ROLE;
                    return getErrorType();
                }
            }

            if (args.size() - 1 == count) {
                return CommandErrorType.SUCCESS;
            }
            count++;
        }

        return CommandErrorType.SUCCESS;
    }

    /**
     * @return The first index of the incorrect {@link CommandUsage}.
     */
    @NotNull
    public Integer getIncorrectIndex() {
        return incorrectIndex;
    }

    /**
     * @return The {@link CommandErrorType} for the index of the incorrect {@link CommandUsage}.
     */
    @NotNull
    private CommandErrorType getErrorType() {
        return errorType;
    }

    /**
     * Checks whether or not the provided {@link String} is a link.
     * @param string The {@link String} to be checked.
     * @return Whether or not the {@link String} is a link.
     */
    @NotNull
    private Boolean isLink(@NotNull String string) {

        try {
            new URL(string).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks whether or not the provided {@link String} is a number.
     * @param string The {@link String} to be checked.
     * @return Whether or not the {@link String} is a number.
     */
    @NotNull
    private Boolean isNumber(@NotNull String string) {
        if (string == null) {
            return false;
        }

        try {
            double d = Double.parseDouble(string);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    /**
     * Checks whether or not the provided {@link String} is a {@link net.dv8tion.jda.api.entities.User User}.
     * @param userID The {@link net.dv8tion.jda.api.entities.User User's} ID.
     * @return Whether or not the {@link net.dv8tion.jda.api.entities.User User} is a {@link net.dv8tion.jda.api.entities.User User}.
     */
    @NotNull
    private Boolean isUser(@NotNull String userID) {
        userID = userID.replace("<@!", "");
        userID = userID.replace(">", "");

        try {
            BeanBot.getJDA().getUserById(userID);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    /**
     * Checks whether or not the provided {@link String} is a {@link net.dv8tion.jda.api.entities.User User}.
     * @param roleID The ID of the {@link net.dv8tion.jda.api.entities.Role Role}.
     * @return Whether or not the specified {@link String} is a {@link net.dv8tion.jda.api.entities.Role Role}.
     */
    @NotNull
    private Boolean isRole(@NotNull String roleID) {
        roleID = roleID.replace("<@!", "");
        roleID = roleID.replace(">", "");

        try {
            BeanBot.getJDA().getRoleById(roleID);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

}
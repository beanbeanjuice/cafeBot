package com.beanbeanjuice.utility.command.usage;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.usage.types.CommandErrorType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import net.dv8tion.jda.api.entities.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
     * @param guild The {@link Guild} that sent the command.
     * @return The {@link com.beanbeanjuice.utility.command.usage.types.CommandErrorType} for the {@link com.beanbeanjuice.utility.command.ICommand ICommand}.
     */
    @NotNull
    public CommandErrorType getERROR(@NotNull ArrayList<String> args, @NotNull Guild guild) {
        int count = 0;
        boolean incorrect;

        // Checking if there are not supposed to be any arguments.
        if (args.size() == 0 && totalRequired == 0) {
            return CommandErrorType.SUCCESS;
        }

        // Checking if there are not enough arguments.
        if (args.size() < totalRequired) {
            if (args.size() == 0) {
                incorrectIndex = 0;
            } else {
                incorrectIndex = args.size() - 1;
            }
            return CommandErrorType.NOT_ENOUGH_ARGUMENTS;
        }

        // Checking if there are too many arguments.
        if (args.size() > usages.size()) {
            incorrectIndex = usages.size();

            if (usages.size() == 0) {
                return CommandErrorType.TOO_MANY_ARGUMENTS;
            }

            if (!usages.get(usages.size() - 1).getType().equals(CommandType.SENTENCE)) {
                return CommandErrorType.TOO_MANY_ARGUMENTS;
            }

        }

        for (CommandUsage entry : usages) {
            CommandType type = entry.getType();

            switch (type) {

                case LINK -> {
                    incorrect = !isLink(args.get(count));
                    if (incorrect) {
                        incorrectIndex = count;
                        errorType = CommandErrorType.LINK;
                        return getErrorType();
                    }
                }

                case NUMBER -> {
                    incorrect = !isNumber(args.get(count));
                    if (incorrect) {
                        incorrectIndex = count;
                        errorType = CommandErrorType.NUMBER;
                        return getErrorType();
                    }
                }

                case USER -> {
                    incorrect = !isUser(args.get(count));
                    if (incorrect) {
                        incorrectIndex = count;
                        errorType = CommandErrorType.USER;
                        return getErrorType();
                    }
                }

                case ROLE -> {
                    incorrect = !isRole(guild, args.get(count));
                    if (incorrect) {
                        incorrectIndex = count;
                        errorType = CommandErrorType.ROLE;
                        return getErrorType();
                    }
                }

                case DATE -> {
                    incorrect = !isDate(args.get(count));
                    if (incorrect) {
                        incorrectIndex = count;
                        errorType = CommandErrorType.DATE;
                        return getErrorType();
                    }
                }

                case TEXT_CHANNEL -> {
                    incorrect = !isTextChannel(guild, args.get(count));
                    if (incorrect) {
                        incorrectIndex = count;
                        errorType = CommandErrorType.TEXT_CHANNEL;
                        return getErrorType();
                    }
                }

                case VOICE_CHANNEL -> {
                    incorrect = !isVoiceChannel(guild, args.get(count));
                    if (incorrect) {
                        incorrectIndex = count;
                        errorType = CommandErrorType.VOICE_CHANNEL;
                        return getErrorType();
                    }
                }

                case LANGUAGE_CODE -> {
                    incorrect = !isLanguageCode(args.get(count));
                    if (incorrect) {
                        incorrectIndex = count;
                        errorType = CommandErrorType.LANGUAGE_CODE;
                        return getErrorType();
                    }
                }

            }

            // I have no idea what this does.
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
     * Checks whether or not the provided {@link String} is a valid {@link VoiceChannel}.
     * @param guild The {@link Guild} to check.
     * @param voiceChannelID The ID of the {@link VoiceChannel}.
     * @return Whether or not the {@link String} is a {@link VoiceChannel} in the {@link Guild}.
     */
    @NotNull
    private Boolean isVoiceChannel(@NotNull Guild guild, @NotNull String voiceChannelID) {
        try {
            VoiceChannel channel = guild.getVoiceChannelById(voiceChannelID);
            if (channel == null) {
                return false;
            }
        } catch (NullPointerException | NumberFormatException e) {
            return false;
        }
        return true;
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
            Integer.parseInt(string);
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
        userID = userID.replace("<@", "");

        try {
            User user = CafeBot.getJDA().getUserById(userID);
            if (user == null) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Checks whether or not the provided {@link String} is a {@link net.dv8tion.jda.api.entities.TextChannel TextChannel}.
     * @param guild The {@link Guild} that contains the {@link net.dv8tion.jda.api.entities.TextChannel TextChannel}.
     * @param textChannelID The ID of the {@link net.dv8tion.jda.api.entities.TextChannel TextChannel}.
     * @return Whether or not the specified {@link String} is a {@link net.dv8tion.jda.api.entities.TextChannel TextChannel}.
     */
    @NotNull
    private Boolean isTextChannel(@NotNull Guild guild, @NotNull String textChannelID) {
        textChannelID = textChannelID.replace("<#", "");
        textChannelID = textChannelID.replace(">", "");

        try {
            TextChannel channel = guild.getTextChannelById(textChannelID);
            if (channel == null) {
                return false;
            }
        } catch (NullPointerException | NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the provided {@link String} is a language code.
     * @param languageCode The language code specified.
     * @return True, if the language code exists.
     */
    @NotNull
    private Boolean isLanguageCode(@NotNull String languageCode) {
        String[] languageCodes = new String[]{"en_US", "hi", "es", "fr", "ja", "ru", "en_GB", "de", "it", "ko", "pt-BR", "ar", "tr"};
        return new ArrayList<>(List.of(languageCodes)).contains(languageCode);
    }

    /**
     * Checks whether or not the provided {@link String} is a {@link net.dv8tion.jda.api.entities.User User}.
     * @param guild The {@link Guild} that contains the {@link net.dv8tion.jda.api.entities.Role Role}.
     * @param roleID The ID of the {@link net.dv8tion.jda.api.entities.Role Role}.
     * @return Whether or not the specified {@link String} is a {@link net.dv8tion.jda.api.entities.Role Role}.
     */
    @NotNull
    private Boolean isRole(@NotNull Guild guild, @NotNull String roleID) {
        roleID = roleID.replace("<@&", "");
        roleID = roleID.replace("<@", "");
        roleID = roleID.replace(">", "");

        try {
            Role role = guild.getRoleById(roleID);
            if (role == null) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Checks whether a provided {@link String} is a {@link java.util.Date}.
     * @param dateString The date {@link String} provided.
     * @return Whether or not the {@link String} is a {@link java.util.Date}.
     */
    @NotNull
    private Boolean isDate(@NotNull String dateString) {
        try {
            new SimpleDateFormat("yyyy-MM-dd").parse("2000-" + dateString);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

}
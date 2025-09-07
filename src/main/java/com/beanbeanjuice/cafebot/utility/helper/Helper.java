package com.beanbeanjuice.cafebot.utility.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * A general class used for everything.
 *
 * @author beanbeanjuice
 */
public class Helper {


    /**
     * Formats the specified time, including days.
     * @param timeInMillis The time as a {@link Long} value.
     * @return The formatted time {@link String}.
     */
    @NotNull
    public static String formatTimeDays(@NotNull Long timeInMillis) {
        final long days = timeInMillis / TimeUnit.DAYS.toMillis(1);
        final long hours = timeInMillis % TimeUnit.DAYS.toMillis(1) / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis % TimeUnit.DAYS.toMillis(1) % TimeUnit.HOURS.toMillis(1) / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.DAYS.toMillis(1) % TimeUnit.HOURS.toMillis(1) % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d:%02d", days, hours, minutes, seconds);
    }

    /**
     * Shorten the message to a certain limit.
     * @param message The contents of the message.
     * @param limit The limit of the new message.
     * @return The new, limited message.
     */
    @NotNull
    public static String shortenToLimit(@NotNull String message, @NotNull Integer limit) {
        message = message.replace("\"", "\\\"");
        if (message.length() >= limit) {
            return message.substring(0, limit - 3) + "...";
        }

        return message;
    }

    /**
     * Formats the specified time, including days.
     * @param timeInMillis The time as a {@link Long} value.
     * @return The formatted time {@link String}.
     */
    @NotNull
    public static String millisToDays(@NotNull Long timeInMillis) {
        final long days = timeInMillis / TimeUnit.DAYS.toMillis(1);
        final long hours = timeInMillis % TimeUnit.DAYS.toMillis(1) / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis % TimeUnit.DAYS.toMillis(1) % TimeUnit.HOURS.toMillis(1) / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.DAYS.toMillis(1) % TimeUnit.HOURS.toMillis(1) % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d:%02d", days, hours, minutes, seconds);
    }

    /**
     * @return A random {@link Color}.
     */
    @NotNull
    public static Color getRandomColor() {
        Random random = new Random();
        float r = random.nextFloat();
        float g = random.nextFloat();
        float b = random.nextFloat();
        return new Color(r, g, b);
    }

    /**
     * Check if a {@link String} is a number.
     * @param check The {@link String} to check.
     * @return True, if the {@link String} is a number.
     */
    @NotNull
    public static Boolean isNumber(@NotNull String check) {
        try {
            Integer.parseInt(check);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @NotNull
    public static Integer stringToPositiveInteger(@NotNull String timeString) {
        try {
            int num = Integer.parseInt(timeString);

            if (num < 1) return -1;
            return num;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static MessageEmbed errorEmbed(@NotNull String title, @NotNull String description) {
        return new EmbedBuilder()
                .setTitle(title)
                .setDescription(description)
                .setColor(Color.red)
                .build();
    }

    public static MessageEmbed defaultErrorEmbed() {
        return errorEmbed("Uncaught Error", "There was an error... and I don't know what's wrong... if this persists please submit a support ticket!");
    }

    public static MessageEmbed successEmbed(final String title, final String description) {
        return new EmbedBuilder()
                .setTitle(title)
                .setDescription(description)
                .setColor(Color.GREEN)
                .build();
    }

    public static MessageEmbed descriptionEmbed(final String description) {
        return new EmbedBuilder()
                .setDescription(description)
                .setColor(getRandomColor())
                .build();
    }

    /**
     * Creates a small {@link MessageEmbed}.
     * @param author The {@link String author} or {@link String title} for the {@link MessageEmbed}.
     * @param url The {@link String url} for the {@link MessageEmbed}.
     * @param authorImageURL The {@link String url} for the icon to show up next to the author.
     * @param description The {@link String description} of the {@link MessageEmbed}.
     * @return The created {@link MessageEmbed}.
     */
    @NotNull
    public static MessageEmbed smallAuthorEmbed(@NotNull String author, @Nullable String url, @NotNull String authorImageURL,
                                                @NotNull String description) {
        return new EmbedBuilder()
                .setAuthor(author, url, authorImageURL)
                .setDescription(description)
                .setColor(getRandomColor())
                .build();
    }

    /**
     * Gets a {@link Role} from the ID.
     * @param guild The {@link Guild} that contains the {@link Role}.
     * @param roleID The ID of the {@link Role}.
     * @return The {@link Role}.
     */
    @Nullable
    public static Role getRole(@NotNull Guild guild, @NotNull String roleID) {
        roleID = roleID.replace("<@&", "");
        roleID = roleID.replace("<@", "");
        roleID = roleID.replace(">", "");

        try {
            return guild.getRoleById(roleID);
        } catch (NullPointerException | NumberFormatException e) {
            return null;
        }
    }

    /**
     * Round the specified {@link Double} to 2 decimal places.
     * @param amount The {@link Double} to round.
     * @return The rounded {@link Double}.
     */
    @NotNull
    public static Double roundDouble(@NotNull Double amount) {
        return Math.round(amount * 100.0) / 100.0;
    }

    /**
     * Round the specified {@link Float} to 2 decimal places.
     * @param amount The {@link Float} amount.
     * @return The rounded {@link Float}.
     */
    public static float roundFloat(float amount) {
        return Math.round(amount * 100.0f) / 100.0f;
    }

    public static int getRandomInteger(final int minimum, final int maximum) {
        return (int) ((Math.random() * (maximum - minimum)) + minimum);
    }

    public static double getRandomDouble(final int minimum, final int maximum) {
        return minimum + new Random().nextDouble() * maximum;
    }

    public static void pmUser(final User user, final MessageEmbed embed) {
        user.openPrivateChannel().flatMap(channel -> channel.sendMessageEmbeds(embed)).queue();
    }

    /**
     * Generates a Random Alpha-Numeric {@link String}.
     * @param n The length of the random alphanumeric {@link String}.
     * @return The randomly generated alphanumeric {@link String}.
     */
    @NotNull
    public static String getRandomAlphaNumericString(@NotNull Integer n) {

        // Chooses a Random Character from This String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // Creates a String Builder with the specified size
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    /**
     * @return A {@link MessageEmbed} specifying that the selected channel is already a Daily channel.
     */
    @NotNull
    public static MessageEmbed alreadyDailyChannel() {
        return Helper.errorEmbed(
                "Specified Channel is Daily Channel",
                "The channel you have specified is already set to the daily channel. " +
                        "This means that it cannot be set to this channel. You can choose another channel " +
                        "or remove the specified channel from being a daily-reset channel."
        );
    }



    /**
     * Check if a {@link MessageChannelUnion} is a {@link net.dv8tion.jda.api.entities.channel.concrete.TextChannel}.
     * @param channel The {@link MessageChannelUnion channel} specified.
     * @return True, if the {@link MessageChannelUnion} is a {@link net.dv8tion.jda.api.entities.channel.concrete.TextChannel}.
     */
    @NotNull
    public static Boolean isTextChannel(@NotNull MessageChannelUnion channel) {
        try {
            channel.asTextChannel();
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    /**
     * Return a {@link MessageEmbed} stating that this is not of type {@link net.dv8tion.jda.api.entities.channel.concrete.TextChannel TextChannel}.
     * @param type The {@link ChannelType type}.
     * @return The completed {@link MessageEmbed}.
     */
    @NotNull
    public static MessageEmbed notATextChannelEmbed(@NotNull ChannelType type) {
        return errorEmbed("Not A Text Channel", "The channel type you are trying to execute " +
                "this command for is: `" + type.name() + "`.");
    }

    /**
     * Return a {@link JsonNode} from a given json {@link String filePath}.
     * @param filePath The {@link String filePath} of the JSON file.
     * @return The specified file as a {@link JsonNode}.
     * @throws IOException Thrown if the given {@link String filePath} is unreachable.
     */
    @NotNull
    public static JsonNode parseJson(String filePath) throws IOException {
        File file = new File(filePath);
        return new ObjectMapper().readTree(file);
    }

    /**
     * Return a {@link String[]} from a given {@link String input}.
     * The input wil be a comma separated {@link String}.
     * @param input The comma-separated {@link String}.
     * @return The {@link String[]} of values, now separated.
     */
    public static String[] removeCommaSpace(String input) {
        return input.split(",\\s*");
    }

    /**
     * @apiNote This only works if all of the {@link ModalMapping} are of type "String".
     * @param mappings The modal {@link ModalMapping mappings}.
     * @return A keyed version of the {@link ModalMapping} values.
     */
    public static Map<String, String> modalValuesToMap(final List<ModalMapping> mappings) {
        Map<String, String> modalMap = new HashMap<>();
        mappings.forEach((mapping) -> modalMap.put(mapping.getCustomId(), mapping.getAsString()));
        return modalMap;
    }

    public static boolean isSingleEmoji(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }

        // Count Unicode "grapheme clusters" (visible characters)
        BreakIterator it = BreakIterator.getCharacterInstance();
        it.setText(input);

        int count = 0;
        int start = it.first();
        for (int end = it.next(); end != BreakIterator.DONE; start = end, end = it.next()) {
            count++;
            // If there's more than 1 grapheme cluster, it's not a single emoji
            if (count > 1) return false;
        }

        // Now check if that grapheme cluster is an emoji
        int codePoint = input.codePointAt(0);
        return isEmoji(codePoint);
    }

    private static boolean isEmoji(int codePoint) {
        // Broad Unicode emoji ranges
        return (codePoint >= 0x1F600 && codePoint <= 0x1F64F)   // Emoticons
                || (codePoint >= 0x1F300 && codePoint <= 0x1F5FF) // Misc Symbols & Pictographs
                || (codePoint >= 0x1F680 && codePoint <= 0x1F6FF) // Transport & Map
                || (codePoint >= 0x2600 && codePoint <= 0x26FF)   // Misc symbols
                || (codePoint >= 0x2700 && codePoint <= 0x27BF)   // Dingbats
                || (codePoint >= 0xFE00 && codePoint <= 0xFE0F)   // Variation Selectors
                || (codePoint >= 0x1F900 && codePoint <= 0x1F9FF) // Supplemental Symbols
                || (codePoint >= 0x1F1E6 && codePoint <= 0x1F1FF); // Flags
    }

}

package com.beanbeanjuice.cafebot.utility.handlers;

import com.beanbeanjuice.cafebot.i18n.I18N;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.time.Instant;

/**
 * Shared builder for the anonymous-confession embed used by both {@code /confess}
 * and the "Confess" message-context relay command. Keeps the presentation
 * consistent between the two entry points.
 */
public final class ConfessionEmbedBuilder {

    private ConfessionEmbedBuilder() {}

    public static MessageEmbed build(final String description, final I18N bundle) {
        return build(description, false, bundle);
    }

    public static MessageEmbed build(final String description, final boolean hasAttachment, final I18N bundle) {
        String footer = bundle.getString("command.confess.embed.footer");
        if (hasAttachment) footer += "\n" + bundle.getString("command.confess.embed.attachment-warning");

        EmbedBuilder builder = new EmbedBuilder()
                .setTitle(bundle.getString("command.confess.embed.title"))
                .setColor(Helper.getRandomColor())
                .setTimestamp(Instant.now())
                .setFooter(footer);

        if (description != null && !description.isBlank()) builder.setDescription(description);

        return builder.build();
    }

}

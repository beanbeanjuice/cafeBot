package com.beanbeanjuice.cafebot.commands.generic;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Optional;

public class EmbedCommand extends Command implements ICommand {

    public EmbedCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        GuildChannelUnion channel = event.getOption("channel").getAsChannel();  // Should not be null.
        Optional<String> messageOptional = Optional.ofNullable(event.getOption("message")).map(OptionMapping::getAsString);
        Optional<Message.Attachment> thumbnailOptional = Optional.ofNullable(event.getOption("thumbnail")).map(OptionMapping::getAsAttachment);
        Optional<Message.Attachment> imageOptional = Optional.ofNullable(event.getOption("image")).map(OptionMapping::getAsAttachment);

        if (!checkValidImages(thumbnailOptional.orElse(null), imageOptional.orElse(null))) {
            String title = ctx.getUserI18n().getString("command.embed.error.attachment.title");
            String description = ctx.getUserI18n().getString("command.embed.error.attachment.description");

            event.getHook().sendMessageEmbeds(Helper.errorEmbed(title, description)).queue();
            return;
        }

        if (!channel.getType().equals(ChannelType.TEXT)) {
            String title = ctx.getUserI18n().getString("command.embed.error.channel.title");
            String description = ctx.getUserI18n().getString("command.embed.error.channel.description")
                    .replace("{channel}", channel.getAsMention());

            event.getHook().sendMessageEmbeds(Helper.errorEmbed(title, description)).queue();
            return;
        }

        Optional<MessageEmbed> optionalEmbed = createEmbed(event);

        if (optionalEmbed.isEmpty()) {
            String title = ctx.getUserI18n().getString("command.embed.error.invalid.title");
            String description = ctx.getUserI18n().getString("command.embed.error.invalid.description");

            event.getHook().sendMessageEmbeds(Helper.errorEmbed(title, description)).queue();
            return;
        }

        MessageEmbed embed = optionalEmbed.get();

        messageOptional.ifPresentOrElse(
                (message) -> channel.asTextChannel().sendMessage(message).addEmbeds(embed).queue(),
                () -> channel.asTextChannel().sendMessageEmbeds(embed).queue()
        );

        String title = ctx.getUserI18n().getString("command.embed.success.title");
        String description = ctx.getUserI18n().getString("command.embed.success.description")
                .replace("{channel}", channel.getAsMention());

        event.getHook().sendMessageEmbeds(Helper.successEmbed(title, description)).queue();
    }

    private boolean checkValidImages(final @Nullable Message.Attachment thumbnail, final @Nullable Message.Attachment image) {
        if (thumbnail != null && !thumbnail.isImage()) return false;
        if (image != null && !image.isImage()) return false;
        return true;
    }

    private Optional<MessageEmbed> createEmbed(final SlashCommandInteractionEvent event) {
        Optional<String> titleOptional = Optional.ofNullable(event.getOption("title")).map(OptionMapping::getAsString);
        Optional<String> descriptionOptional = Optional.ofNullable(event.getOption("description")).map(OptionMapping::getAsString);
        Optional<String> authorOptional = Optional.ofNullable(event.getOption("author")).map(OptionMapping::getAsString);
        Optional<String> footerOptional = Optional.ofNullable(event.getOption("footer")).map(OptionMapping::getAsString);
        Optional<Message.Attachment> thumbnailOptional = Optional.ofNullable(event.getOption("thumbnail")).map(OptionMapping::getAsAttachment);
        Optional<Message.Attachment> imageOptional = Optional.ofNullable(event.getOption("image")).map(OptionMapping::getAsAttachment);
        Optional<String> colorOptional = Optional.ofNullable(event.getOption("color")).map(OptionMapping::getAsString);

        EmbedBuilder embedBuilder = new EmbedBuilder();

        titleOptional.ifPresent(embedBuilder::setTitle);
        descriptionOptional.ifPresent(embedBuilder::setDescription);
        authorOptional.ifPresent(embedBuilder::setAuthor);
        footerOptional.ifPresent(embedBuilder::setFooter);
        thumbnailOptional.ifPresent((thumbnail) -> embedBuilder.setThumbnail(thumbnail.getUrl()));
        imageOptional.ifPresent((image) -> embedBuilder.setImage(image.getUrl()));
        colorOptional
                .map((colorString) -> {
                    if (!colorString.startsWith("#")) return "#" + colorString;
                    return colorString;
                })
                .map((colorString) -> {
                    try { return Color.decode(colorString); }
                    catch (NumberFormatException e) { return Color.decode("#FFC0CB"); }
                })
                .ifPresent(embedBuilder::setColor);

        try {
            return Optional.of(embedBuilder.build());
        } catch (IllegalStateException e) {
            return Optional.empty();
        }
    }

    @Override
    public String getName() {
        return "embed";
    }

    @Override
    public String getDescriptionPath() {
        return "command.embed.description";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.GENERIC;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.CHANNEL, "channel", "command.embed.arguments.channel", true),
                new OptionData(OptionType.STRING, "title", "command.embed.arguments.title", false),
                new OptionData(OptionType.STRING, "description", "command.embed.arguments.description", false),
                new OptionData(OptionType.STRING, "author", "command.embed.arguments.author", false),
                new OptionData(OptionType.STRING, "message", "command.embed.arguments.message", false),
                new OptionData(OptionType.STRING, "footer", "command.embed.arguments.footer", false),
                new OptionData(OptionType.ATTACHMENT, "thumbnail", "command.embed.arguments.thumbnail", false),
                new OptionData(OptionType.ATTACHMENT, "image", "command.embed.arguments.image", false),
                new OptionData(OptionType.STRING, "color", "command.embed.arguments.color", false)
        };
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[] {
                Permission.MESSAGE_EMBED_LINKS,
                Permission.MANAGE_CHANNEL,
                Permission.MANAGE_SERVER
        };
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
        return false;
    }

}

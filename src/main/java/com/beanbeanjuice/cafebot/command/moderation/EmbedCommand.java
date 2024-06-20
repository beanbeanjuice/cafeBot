package com.beanbeanjuice.cafebot.command.moderation;

import com.beanbeanjuice.cafebot.utility.command.CommandCategory;
import com.beanbeanjuice.cafebot.utility.command.CommandType;
import com.beanbeanjuice.cafebot.utility.command.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to create custom {@link MessageEmbed}
 *
 * @author beanbeanjuice
 */
public class EmbedCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        Modal.Builder modalBuilder = Modal.create("embed-modal", "Create a Custom Embed");
        getModalOptions().forEach((option) -> modalBuilder.addComponents(ActionRow.of(option)));
        event.replyModal(modalBuilder.build()).queue();
    }

    @Override
    public void handleModal(@NotNull ModalInteractionEvent event) {
        if (event.getValue("embed-message") == null) event.getChannel().sendMessageEmbeds(createEmbed(event)).queue();
        else event.getChannel().sendMessage(event.getValue("embed-message").getAsString()).addEmbeds(createEmbed(event)).queue();

        event.getHook().sendMessageEmbeds(Helper.successEmbed(
                "Created the Custom Message Embed",
                "Successfully created the custom embed in " + event.getChannel().getAsMention() + "!"
        )).queue();
    }

    @NotNull
    private MessageEmbed createEmbed(@NotNull ModalInteractionEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        // Required options.
        embedBuilder.setTitle(event.getValue("embed-title").getAsString());
        embedBuilder.setDescription(event.getValue("embed-description").getAsString());

        if (event.getValue("embed-footer") != null)
            embedBuilder.setFooter(event.getValue("embed-footer").getAsString());

        try {
            if (event.getValue("embed-image") != null)
                embedBuilder.setImage(event.getValue("embed-image").getAsString());
        } catch (IllegalArgumentException ignored) { }

        embedBuilder.setColor(Helper.getRandomColor());

        return embedBuilder.build();
    }

    @NotNull
    @Override
    public CommandType getType() {
        return CommandType.MODAL;
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Create a customised embedded message!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/embed`";
    }

    private ArrayList<TextInput> getModalOptions() {
        ArrayList<TextInput> options = new ArrayList<>();

        options.add(
                TextInput.create("embed-title", "Title", TextInputStyle.SHORT)
                    .setPlaceholder("The title for the embed.")
                    .build()
        );

        options.add(
                TextInput.create("embed-description", "Description", TextInputStyle.PARAGRAPH)
                        .setPlaceholder("Inner message for the embed.")
                        .build()
        );

        options.add(
                TextInput.create("embed-footer", "Footer", TextInputStyle.SHORT)
                        .setPlaceholder("Footer for the embed.")
                        .setRequired(false)
                        .build()
        );

        options.add(
                TextInput.create("embed-message", "Optional Message", TextInputStyle.SHORT)
                        .setPlaceholder("@everyone please check out this embed!")
                        .setRequired(false)
                        .build()
        );

        options.add(
                TextInput.create("embed-image", "Large Image", TextInputStyle.SHORT)
                        .setPlaceholder("A link to a large image to place in the embed.")
                        .setRequired(false)
                        .build()
        );

        return options;
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.MODERATION;
    }

    @NotNull
    @Override
    public Boolean isHidden() {
        return true;
    }

    @Nullable
    @Override
    public ArrayList<Permission> getPermissions() {
        ArrayList <Permission> permissions = new ArrayList<>();
        permissions.add(Permission.MANAGE_SERVER);
        return permissions;
    }

}

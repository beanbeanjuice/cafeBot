package com.beanbeanjuice.cafebot.commands.moderation.polls;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class PollCreateSubCommand extends Command implements ISubCommand {

    public PollCreateSubCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        // TODO: This might not be needed anymore.
//        TextInput title = TextInput.create("title", "Title", TextInputStyle.SHORT)
//                .setPlaceholder("Welcome to the server, {user}!")
//                .setRequired(false)
//                .build();
//
//        TextInput description = TextInput.create("description", "Description", TextInputStyle.PARAGRAPH)
//                .setPlaceholder("Welcome {user} to the server!\nThese are our rules...")
//                .setRequired(false)
//                .build();
//
//        TextInput message = TextInput.create("message", "Message", TextInputStyle.PARAGRAPH)
//                .setPlaceholder("@SomeRole, {user} just joined!")
//                .setRequired(false)
//                .build();
//
//        TextInput largeImage = TextInput.create("image-url", "Large Image", TextInputStyle.SHORT)
//                .setPlaceholder("https://some.image.url")
//                .setRequired(false)
//                .build();
//
//        TextInput smallImage = TextInput.create("thumbnail-url", "Small Image", TextInputStyle.SHORT)
//                .setPlaceholder("https://some.image.url")
//                .setRequired(false)
//                .build();
//
//        Modal modal = Modal.create("cafeBot:modal:welcome:message:" + this.getName(), "Edit Welcome Message Content")
//                .addComponents(
//                        ActionRow.of(title),
//                        ActionRow.of(description),
//                        ActionRow.of(message),
//                        ActionRow.of(largeImage),
//                        ActionRow.of(smallImage)
//                )
//                .build();
//
//        event.replyModal(modal).queue();
    }

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getDescription() {
        return "Create a poll!";
    }

    @Override
    public boolean isModal() {
        return true;
    }

}

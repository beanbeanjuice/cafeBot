package com.beanbeanjuice.cafebot.commands.settings.goodbye.message;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class GoodbyeMessageSetSubCommand extends Command implements ISubCommand {

    public GoodbyeMessageSetSubCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        TextInput title = TextInput.create("title", "Title", TextInputStyle.SHORT)
                .setPlaceholder("Goodbye... {user}!")
                .setRequired(false)
                .build();

        TextInput description = TextInput.create("description", "Description", TextInputStyle.PARAGRAPH)
                .setPlaceholder("{user} left... sad...")
                .setRequired(false)
                .build();

        TextInput message = TextInput.create("message", "Message", TextInputStyle.PARAGRAPH)
                .setPlaceholder("@SomeRole, {user} just left...")
                .setRequired(false)
                .build();

        TextInput largeImage = TextInput.create("image-url", "Large Image", TextInputStyle.SHORT)
                .setPlaceholder("https://some.image.url")
                .setRequired(false)
                .build();

        TextInput smallImage = TextInput.create("thumbnail-url", "Small Image", TextInputStyle.SHORT)
                .setPlaceholder("https://some.image.url")
                .setRequired(false)
                .build();

        Modal modal = Modal.create("cafeBot:modal:goodbye:message:" + this.getName(), "Edit Goodbye Message Content")
                .addComponents(
                        ActionRow.of(title),
                        ActionRow.of(description),
                        ActionRow.of(message),
                        ActionRow.of(largeImage),
                        ActionRow.of(smallImage)
                )
                .build();

        event.replyModal(modal).queue();
    }

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String getDescription() {
        return "Set the goodbye message!";
    }

    @Override
    public boolean isModal() {
        return true;
    }

}

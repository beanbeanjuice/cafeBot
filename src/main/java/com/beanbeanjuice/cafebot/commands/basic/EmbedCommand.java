package com.beanbeanjuice.cafebot.commands.basic;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;

import java.util.Optional;

public class EmbedCommand extends Command implements ISubCommand {

    public EmbedCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handleModal(ModalInteractionEvent event) {
        Optional<ModalMapping> subjectMapping = Optional.ofNullable(event.getValue("subject"));
        Optional<ModalMapping> bodyMapping = Optional.ofNullable(event.getValue("body"));

        EmbedBuilder embedBuilder = new EmbedBuilder();

        subjectMapping.ifPresent((mapping) -> embedBuilder.setTitle(mapping.getAsString()));
        bodyMapping.ifPresent((mapping) -> embedBuilder.setDescription(mapping.getAsString()));

        event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
        event.getHook().sendMessage("Successfully created the embed!").queue();
    }

    @Override
    public String getName() {
        return "embed";
    }

    @Override
    public String getDescription() {
        return "Create an embed in the current channel.";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[0];
    }



//    @Override
//    public Permission[] getPermissions() {
//        return new Permission[] {
//                Permission.MESSAGE_EMBED_LINKS,
//                Permission.MANAGE_CHANNEL
//        };
//    }

//    @Override
//    public boolean isNSFW() {
//        return false;
//    }
//
//    @Override
//    public boolean allowDM() {
//        return true;
//    }

    @Override
    public boolean isModal() {
        return true;
    }

    @Override
    public Modal getModal() {
        TextInput subject = TextInput.create("subject", "Subject", TextInputStyle.SHORT)
                .setPlaceholder("Subject of this ticket")
                .setMinLength(10)
                .setMaxLength(100) // or setRequiredRange(10, 100)
                .build();

        TextInput body = TextInput.create("body", "Body", TextInputStyle.PARAGRAPH)
                .setPlaceholder("Your concerns go here")
                .setMinLength(30)
                .setMaxLength(1000)
                .build();

        return Modal.create(this.getName(), this.getName().toUpperCase())
                .addActionRow(subject)
                .addActionRow(body)
                .build();
    }

}

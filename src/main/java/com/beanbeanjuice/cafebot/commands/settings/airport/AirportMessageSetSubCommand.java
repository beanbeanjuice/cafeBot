package com.beanbeanjuice.cafebot.commands.settings.airport;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.AirportMessageType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import net.dv8tion.jda.api.components.label.Label;
import net.dv8tion.jda.api.components.textinput.TextInput;
import net.dv8tion.jda.api.components.textinput.TextInputStyle;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.modals.Modal;

import java.util.Arrays;

public class AirportMessageSetSubCommand extends Command implements ISubCommand {

    public AirportMessageSetSubCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        AirportMessageType type = AirportMessageType.valueOf(event.getOption("type").getAsString());

//        TextDisplay instructions = TextDisplay.of("""
//                **Editing the Airport Message!**
//
//                This message is sent when a user joins/leaves your server (depending on which one you choose).
//
//                Please read the instructions:
//
//                - You can use the placeholder `{user}` to indicate the mentionable user when they are joining.
//                - To also mention a role, just copy the role mention type with the <@XXXX>!
//                """);

        Label title = Label.of("Title", TextInput.create("title", TextInputStyle.SHORT)
                .setPlaceholder("Welcome to the server, {user}!")
                .setRequired(false)
                .build());

        Label description = Label.of("Description", TextInput.create("description", TextInputStyle.PARAGRAPH)
                .setPlaceholder("Welcome {user} to the server!\nThese are our rules...")
                .setRequired(false)
                .build());

        Label message = Label.of("Message", TextInput.create("message", TextInputStyle.PARAGRAPH)
                .setPlaceholder("@SomeRole, {user} just joined!")
                .setRequired(false)
                .build());

        Label largeImage = Label.of("Image URL", TextInput.create("image-url", TextInputStyle.SHORT)
                .setPlaceholder("https://some.image.url")
                .setRequired(false)
                .build());

        Label smallImage = Label.of("Thumbnail URL", TextInput.create("thumbnail-url", TextInputStyle.SHORT)
                .setPlaceholder("https://some.image.url")
                .setRequired(false)
                .build());

//        Label type = Label.of("Airport Type", StringSelectMenu.create("airport-type")
//                .addOption("Welcome Message", AirportMessageType.WELCOME.name())
//                .addOption("Goodbye Message", AirportMessageType.GOODBYE.name())
//                .build());

        Modal modal = Modal.create("cafeBot:modal:airport:message:" + type.name(), String.format("Edit %s Message Content", type.name()))
                .addComponents(
//                        instructions,
                        title,
                        description,
                        message,
                        largeImage,
                        smallImage
                )
                .build();

        event.replyModal(modal).queue();
    }

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String getDescriptionPath() {
        return "Set the airport message.";
    }

    @Override
    public boolean isModal() {
        return true;
    }

    @Override
    public OptionData[] getOptions() {
        OptionData channelTypeData = new OptionData(OptionType.STRING, "type", "The message type you want to set", true);

        Arrays.stream(AirportMessageType.values()).forEach((type) -> channelTypeData.addChoice(type.name(), type.name()));

        return new OptionData[] {
                channelTypeData
        };
    }

}

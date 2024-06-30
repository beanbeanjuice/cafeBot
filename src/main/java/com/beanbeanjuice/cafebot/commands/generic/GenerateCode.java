package com.beanbeanjuice.cafebot.commands.generic;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class GenerateCode extends Command implements ICommand {

    public GenerateCode(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        cafeBot.getCafeAPI().getGeneratedCodesEndpoint().updateUserGeneratedCodeIfExists(event.getUser().getId())
                .thenAcceptAsync(generatedCode -> {
                    cafeBot.pmUser(event.getUser(), Helper.successEmbed(
                            "Generated Code",
                            String.format("Code: `%s`", generatedCode)
                    ));

                    event.getHook().sendMessageEmbeds(Helper.successEmbed(
                            "Code Generated",
                            "The code has been generated and DM'd to you."
                    )).queue();
                })
                .exceptionallyAsync((e) -> {
                    event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                            "Error Generating Code",
                            "There was an error generating your code. Please contact the developer."
                    )).queue();
                    return null;
                });
    }

    @Override
    public String getName() {
        return "generate-code";
    }

    @Override
    public String getDescription() {
        return "Generate a random 32 character code!";
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[0];
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
        return true;
    }

}

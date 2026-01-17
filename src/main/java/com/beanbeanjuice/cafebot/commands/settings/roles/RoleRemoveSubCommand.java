package com.beanbeanjuice.cafebot.commands.settings.roles;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomRoleType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Arrays;

public class RoleRemoveSubCommand extends Command implements ISubCommand {

    public RoleRemoveSubCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        String guildId = event.getGuild().getId();
        CustomRoleType type = CustomRoleType.valueOf(event.getOption("type").getAsString());

        this.bot.getCafeAPI().getCustomRoleApi().deleteCustomRole(guildId, type)
                .thenAccept((ignored) -> {
                    event.getHook().sendMessageEmbeds(Helper.successEmbed(
                            String.format("%s Role Removed", type.getFriendlyName()),
                            String.format("The %s role has been successfully removed.", type.getFriendlyName())
                    )).queue();
                })
                .exceptionally((ex) -> {
                    event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                            String.format("Error Removing %s Role", type.getFriendlyName()),
                            "There was a problem removing the role 🥺 please try again later..."
                    )).queue();

                    this.bot.getLogger().log(this.getClass(), LogLevel.WARN, String.format("Error Removing %s Role: %s", type.getFriendlyName(), ex.getMessage()));
                    return null;
                });
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescription() {
        return "Remove a custom role!";
    }

    @Override
    public OptionData[] getOptions() {
        OptionData roleTypeData = new OptionData(OptionType.STRING, "type", "The role type you want to set", true);

        Arrays.stream(CustomRoleType.values()).forEach((type) -> roleTypeData.addChoice(type.getFriendlyName(), type.name()));

        return new OptionData[] {
                roleTypeData
        };
    }

}

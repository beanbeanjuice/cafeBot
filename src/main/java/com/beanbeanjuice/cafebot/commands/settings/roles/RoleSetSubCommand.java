package com.beanbeanjuice.cafebot.commands.settings.roles;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomRoleType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Arrays;

public class RoleSetSubCommand extends Command implements ISubCommand {

    public RoleSetSubCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        CustomRoleType type = CustomRoleType.valueOf(event.getOption("type").getAsString());
        Role role = event.getOption("role").getAsRole();

        String guildId = event.getGuild().getId();
        String roleId = role.getId();

        this.bot.getCafeAPI().getCustomRoleApi().setCustomRole(guildId, type, roleId)
                .thenAccept((customRole) -> {
                    event.getHook().sendMessageEmbeds(Helper.successEmbed(
                            String.format("%s Role Set", type.getFriendlyName()),
                            String.format("Role has been set to %s! Please make sure I have a higher role permission than the role you're trying to set 🥺!", role.getAsMention())
                    )).queue();
                })
                .exceptionally((ex) -> {
                    event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                            String.format("Error Setting %s Role", type.getFriendlyName()),
                            "There was an error setting the role... I'm so sorry.. 🥺"
                    )).queue();

                    this.bot.getLogger().log(this.getClass(), LogLevel.WARN, String.format("Error Setting %s Channel: %s", type.getFriendlyName(), ex.getMessage()), true, true);
                    return null;
                });
    }

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String getDescription() {
        return "Set a custom role!";
    }

    @Override
    public OptionData[] getOptions() {
        OptionData roleTypeData = new OptionData(OptionType.STRING, "type", "The role type you want to set", true);

        Arrays.stream(CustomRoleType.values()).forEach((type) -> roleTypeData.addChoice(type.getFriendlyName(), type.name()));

        return new OptionData[] {
                roleTypeData,
                new OptionData(OptionType.ROLE, "role", "The discord role you want to bind to", true)
        };
    }

}

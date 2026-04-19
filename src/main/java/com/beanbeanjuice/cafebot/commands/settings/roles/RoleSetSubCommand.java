package com.beanbeanjuice.cafebot.commands.settings.roles;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomRoleType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.i18n.I18N;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
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
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        I18N bundle = ctx.getDefaultBundle();
        CustomRoleType type = CustomRoleType.valueOf(event.getOption("type").getAsString());
        Role role = event.getOption("role").getAsRole();

        String guildId = event.getGuild().getId();
        String roleId = role.getId();

        this.bot.getCafeAPI().getCustomRoleApi().setCustomRole(guildId, type, roleId)
                .thenAccept((customRole) -> {
                    String title = bundle.getString("command.role.subcommand.set.embed.success.title")
                            .replace("{type}", type.getFriendlyName());
                    String description = bundle.getString("command.role.subcommand.set.embed.success.description")
                            .replace("{role}", role.getAsMention());
                    event.getHook().sendMessageEmbeds(Helper.successEmbed(title, description)).queue();
                })
                .exceptionally((ex) -> {
                    String title = bundle.getString("command.role.subcommand.set.embed.error.title")
                            .replace("{type}", type.getFriendlyName());
                    event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                            title,
                            bundle.getString("command.role.subcommand.set.embed.error.description")
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
    public String getDescriptionPath() {
        return "command.role.subcommand.set.description";
    }

    @Override
    public OptionData[] getOptions() {
        OptionData roleTypeData = new OptionData(OptionType.STRING, "type", "command.role.subcommand.set.arguments.type.description", true);

        Arrays.stream(CustomRoleType.values()).forEach((type) -> roleTypeData.addChoice(type.getFriendlyName(), type.name()));

        return new OptionData[] {
                roleTypeData,
                new OptionData(OptionType.ROLE, "role", "command.role.subcommand.set.arguments.role.description", true)
        };
    }

}

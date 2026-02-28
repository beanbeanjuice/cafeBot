package com.beanbeanjuice.cafebot.commands.settings.roles;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomRoleType;
import com.beanbeanjuice.cafebot.api.wrapper.type.CustomRole;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class RoleListSubCommand extends Command implements ISubCommand {

    public RoleListSubCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        Guild guild = event.getGuild();

        this.bot.getCafeAPI().getCustomRoleApi().getCustomRoles(guild.getId())
                .thenAccept((customRoles) -> {
                    String description = Arrays.stream(CustomRoleType.values()).map((type) -> {
                        Role role = Optional.ofNullable(customRoles.get(type))
                                .map(CustomRole::getRoleId)
                                .map(guild::getRoleById)
                                .orElse(null);

                        String mention = (role == null) ? "*Unset*" : role.getAsMention();

                        return String.format("**%s** - %s", type.getFriendlyName(), mention);
                    }).collect(Collectors.joining("\n"));

                    event.getHook().sendMessageEmbeds(Helper.successEmbed(
                            "Custom Roles",
                            description
                    )).queue();
                })
                .exceptionally((ex) -> {
                    event.getHook().sendMessage(ex.getCause().toString()).queue();
                    return null;
                });
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescriptionPath() {
        return "List all custom roles for the server!";
    }

}

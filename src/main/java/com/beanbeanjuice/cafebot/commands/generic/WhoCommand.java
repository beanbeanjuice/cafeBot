package com.beanbeanjuice.cafebot.commands.generic;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.i18n.I18N;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

public class WhoCommand extends Command implements ICommand {

    public WhoCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        final I18N bundle = ctx.getUserI18n();
        if (event.isFromGuild()) handleFromGuild(event, bundle);
        else handleOther(event, bundle);
    }

    private void handleFromGuild(SlashCommandInteractionEvent event, final I18N bundle) {
        Optional<OptionMapping> memberMapping = Optional.ofNullable(event.getOption("user"));
        Member member = memberMapping.map(OptionMapping::getAsMember).orElse(event.getMember());

        event.getHook().sendMessageEmbeds(memberInfoEmbed(member, bundle)).queue();
    }

    private void handleOther(SlashCommandInteractionEvent event, final I18N bundle) {
        Optional<OptionMapping> userMapping = Optional.ofNullable(event.getOption("user"));
        User user = userMapping.map(OptionMapping::getAsUser).orElse(event.getUser());

        event.getHook().sendMessageEmbeds(userInfoEmbed(user, bundle)).queue();
    }

    private MessageEmbed userInfoEmbed(User user, final I18N bundle) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy @ h:mma");

        return new EmbedBuilder()
                .setThumbnail(user.getAvatarUrl())
                .setTitle(user.getName())
                .addField(bundle.getString("command.who.embed.registered"), user.getTimeCreated().format(formatter), true)
                .setFooter(bundle.getString("command.who.embed.id-footer").replace("{id}", user.getId()))
                .setColor(Helper.getRandomColor())
                .setAuthor(user.getName(), null, user.getAvatarUrl())
                .setDescription(user.getAsMention())
                .setTimestamp(new Date().toInstant())
                .build();
    }

    private MessageEmbed memberInfoEmbed(Member member, final I18N bundle) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy @ h:mma");
        EmbedBuilder embedBuilder = new EmbedBuilder(userInfoEmbed(member.getUser(), bundle));
        embedBuilder
                .addField(bundle.getString("command.who.embed.joined"), member.getTimeJoined().format(formatter), true)
                .setAuthor(member.getEffectiveName(), null, member.getUser().getAvatarUrl());

        String roleString = member.getRoles().stream().map(Role::getAsMention).collect(Collectors.joining(" "));
        String permissionString = member.getPermissions().stream().map(Permission::getName).collect(Collectors.joining(", "));
        String rolesHeader = bundle.getString("command.who.embed.roles").replace("{count}", String.valueOf(member.getRoles().size()));

        embedBuilder
                .addField(rolesHeader, roleString, false)
                .addField(bundle.getString("command.who.embed.permissions"), permissionString, false);
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "who";
    }

    @Override
    public String getDescriptionPath() {
        return "command.who.description";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.GENERIC;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "command.who.arguments.user.description", false)
        };
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

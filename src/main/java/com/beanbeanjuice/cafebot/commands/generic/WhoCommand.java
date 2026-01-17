package com.beanbeanjuice.cafebot.commands.generic;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
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
    public void handle(SlashCommandInteractionEvent event) {
        Optional<OptionMapping> userMapping = Optional.ofNullable(event.getOption("user"));

        if (event.isFromGuild()) handleFromGuild(event);
        else handleOther(event);
    }

    private void handleFromGuild(SlashCommandInteractionEvent event) {
        Optional<OptionMapping> memberMapping = Optional.ofNullable(event.getOption("user"));
        Member member = memberMapping.map(OptionMapping::getAsMember).orElse(event.getMember());

        event.getHook().sendMessageEmbeds(memberInfoEmbed(member)).queue();
    }

    private void handleOther(SlashCommandInteractionEvent event) {
        Optional<OptionMapping> userMapping = Optional.ofNullable(event.getOption("user"));
        User user = userMapping.map(OptionMapping::getAsUser).orElse(event.getUser());

        event.getHook().sendMessageEmbeds(userInfoEmbed(user)).queue();
    }

    private MessageEmbed userInfoEmbed(User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy @ h:mma");

        return new EmbedBuilder()
                .setThumbnail(user.getAvatarUrl())
                .setTitle(user.getName())
                .addField("Registered", user.getTimeCreated().format(formatter), true)
                .setFooter("ID: " + user.getId())
                .setColor(Helper.getRandomColor())
                .setAuthor(user.getName(), null, user.getAvatarUrl())
                .setDescription(user.getAsMention())
                .setTimestamp(new Date().toInstant())
                .build();
    }

    private MessageEmbed memberInfoEmbed(Member member) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy @ h:mma");
        EmbedBuilder embedBuilder = new EmbedBuilder(userInfoEmbed(member.getUser()));
        embedBuilder
                .addField("Joined", member.getTimeJoined().format(formatter), true)
                .setAuthor(member.getEffectiveName(), null, member.getUser().getAvatarUrl());

        String roleString = member.getRoles().stream().map(Role::getAsMention).collect(Collectors.joining(" "));
        String permissionString = member.getPermissions().stream().map(Permission::getName).collect(Collectors.joining(", "));

        embedBuilder
                .addField("Roles [" + member.getRoles().size() + "]", roleString, false)
                .addField("Permissions", permissionString, false);
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "who";
    }

    @Override
    public String getDescription() {
        return "See stats about yourself or another user!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.GENERIC;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "The person to get stats about.", false)
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

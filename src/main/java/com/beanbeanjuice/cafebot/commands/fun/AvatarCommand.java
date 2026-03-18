package com.beanbeanjuice.cafebot.commands.fun;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Optional;
import java.util.ResourceBundle;

public class AvatarCommand extends Command implements ICommand {

    public AvatarCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        Optional<OptionMapping> userOption = Optional.ofNullable(event.getOption("user"));
        Optional<OptionMapping> serverOption = Optional.ofNullable(event.getOption("server"));

        User user = userOption.map(OptionMapping::getAsUser).orElse(event.getUser());
        Member member = userOption.map(OptionMapping::getAsMember).orElseGet(event::getMember);
        boolean useServer = serverOption.map(OptionMapping::getAsBoolean).orElse(false);

        if (useServer && !event.isFromGuild()) {
            event.getHook().sendMessageEmbeds(serverErrorEmbed(ctx.getUserI18n())).queue();
            return;
        }

        Optional<String> urlOptional = Optional.ofNullable(member)
                .filter(m -> useServer)
                .map(Member::getAvatarUrl)
                .or(() -> Optional.ofNullable(user.getAvatarUrl()));

        urlOptional.ifPresentOrElse(
                (url) -> event.getHook().sendMessageEmbeds(avatarEmbed(user.getName(), url, ctx.getUserI18n())).queue(),
                () -> event.getHook().sendMessageEmbeds(missingErrorEmbed(ctx.getUserI18n())).queue()
        );
    }

    private MessageEmbed avatarEmbed(String name, String avatarURL, ResourceBundle i18n) {
        String title = i18n.getString("command.avatar.embed.title").replace("{user}", name);
        return new EmbedBuilder()
                .setTitle(title)
                .setImage(avatarURL + "?size=512")
                .setColor(Helper.getRandomColor())
                .build();
    }

    private MessageEmbed missingErrorEmbed(ResourceBundle i18n) {
        String title = i18n.getString("command.avatar.error.missing.title");
        String description = i18n.getString("command.avatar.error.missing.description");
        return Helper.errorEmbed(title, description);
    }

    private MessageEmbed serverErrorEmbed(ResourceBundle i18n) {
        String title = i18n.getString("command.avatar.error.server.title");
        String description = i18n.getString("command.avatar.error.server.description");
        return Helper.errorEmbed(title, description);
    }

    @Override
    public String getName() {
        return "avatar";
    }

    @Override
    public String getDescriptionPath() {
        return "command.avatar.description";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.FUN;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "command.avatar.arguments.user.description", false),
                new OptionData(OptionType.BOOLEAN, "server", "command.avatar.arguments.server.description", false)
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

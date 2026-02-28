package com.beanbeanjuice.cafebot.commands.fun;

import com.beanbeanjuice.cafebot.api.wrapper.type.DiscordServer;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class AiCommand extends Command implements ICommand {

    public AiCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        String guildId = event.getGuild().getId();
        boolean status = event.getOption("status").getAsBoolean();

        String statusString = (status) ? "Enabled ✅" : "Disabled ❌";

        // TODO: Update syntax for this later
        bot.getCafeAPI().getGuildApi().updateDiscordServer(guildId, new DiscordServer(null, 0, status)).thenRun(() -> {
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    "AI Response Changed",
                    String.format("Status: %s", statusString)
            )).queue();
        }).exceptionally((ex) -> {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Error Changing AI Response",
                    "There was an error changing the AI response... I'm so sorry..."
            )).queue();

            bot.getLogger().log(this.getClass(), LogLevel.WARN, "Error Updating AI Response: " + ex.getMessage(), true, false);
            return null;
        });
    }

    @Override
    public String getName() {
        return "ai";
    }

    @Override
    public String getDescriptionPath() {
        return "Want a sassy AI that will serve you some coffee?";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.FUN;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.BOOLEAN, "status", "Whether to enable or disable the \"AI\".", true)
        };
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[] {
                Permission.MANAGE_SERVER
        };
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
        return false;
    }

}

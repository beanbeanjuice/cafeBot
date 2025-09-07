package com.beanbeanjuice.cafebot.utility.listeners;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class BotRemoveListener extends ListenerAdapter {

    private final CafeBot cafeBot;

    public BotRemoveListener(final CafeBot cafeBot) {
        this.cafeBot = cafeBot;
    }

    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {
        cafeBot.getCafeAPI().getGuildApi().deleteDiscordServer(event.getGuild().getId());
        cafeBot.getLogger().log(BotRemoveListener.class, LogLevel.INFO, "**" + event.getGuild().getName() + "** has removed me... :pleading_face:", false, true);
    }

}

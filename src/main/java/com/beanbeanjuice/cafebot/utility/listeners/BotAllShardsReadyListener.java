package com.beanbeanjuice.cafebot.utility.listeners;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.atomic.AtomicInteger;

public class BotAllShardsReadyListener extends ListenerAdapter {

    private final CafeBot bot;
    private final AtomicInteger readyShards = new AtomicInteger(0);

    public BotAllShardsReadyListener(CafeBot bot) {
        this.bot = bot;
    }

    @Override
    public void onReady(@NonNull ReadyEvent event) {
        readyShards.incrementAndGet();

        ShardManager shardManager = event.getJDA().getShardManager();
        if (shardManager != null && readyShards.get() == shardManager.getShardsTotal()) {
            bot.getShardManager().setStatus(OnlineStatus.ONLINE);
            bot.getLogger().log(this.getClass(), LogLevel.OKAY, String.format("The bot is online with %d shards!", readyShards.get()), true, true);
        }

        bot.getLogger().log(this.getClass(), LogLevel.INFO, String.format("Shard #%d is ready.", event.getJDA().getShardInfo().getShardId()), true, false);
    }

}

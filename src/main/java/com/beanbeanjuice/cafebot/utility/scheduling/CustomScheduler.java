package com.beanbeanjuice.cafebot.utility.scheduling;

import com.beanbeanjuice.cafebot.CafeBot;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class CustomScheduler {

    protected final CafeBot bot;
    private final AtomicBoolean started = new AtomicBoolean(false); // need thread safety.
    protected final ScheduledExecutorService scheduler;

    public CustomScheduler(CafeBot bot, String name) {
        this.bot = bot;
        scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, name);
            t.setDaemon(true);
            return t;
        });
    }

    public final void start() {
        if (!started.compareAndSet(false, true)) return; // already started
        onStart();
    }

    protected abstract void onStart();

}

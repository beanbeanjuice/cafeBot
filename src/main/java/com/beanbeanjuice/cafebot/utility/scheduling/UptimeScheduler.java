package com.beanbeanjuice.cafebot.utility.scheduling;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.EnvironmentVariable;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.http.Method;
import org.apache.hc.core5.net.URIBuilder;

import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class UptimeScheduler extends CustomScheduler {

    public UptimeScheduler(CafeBot bot) {
        super(bot, "Uptime-Scheduler");
    }

    @Override
    protected void onStart() {
        this.scheduler.scheduleAtFixedRate(() -> {
            try {
                String urlString = String.format(
                        "%s?status=%s&msg=%s&ping=%d",
                        EnvironmentVariable.CAFEBOT_KUMA_URL.getSystemVariable(),
                        "up",
                        "OK",
                        (int) bot.getShardManager().getAverageGatewayPing()
                );
                URIBuilder uriBuilder = new URIBuilder(urlString);
                SimpleHttpRequest httpRequest = new SimpleHttpRequest(Method.GET, uriBuilder.build());

                CloseableHttpAsyncClient client = HttpAsyncClients.custom().build();
                client.start();
                try {
                    client.execute(httpRequest, null).get();
                } catch (ExecutionException e) {
                    if (e.getCause() instanceof UnknownHostException) {
                        bot.getLogger().log(CafeBot.class, LogLevel.ERROR, "The bot appears to be offline.", false, false, e.getCause());
                    } else {
                        throw e;
                    }
                }
                client.close();
            } catch (Exception e) {
                bot.getLogger().log(this.getClass(), LogLevel.WARN, "Failed Uptime Check: " + e.getMessage(), true, false);
            }
        }, 0, 60, TimeUnit.SECONDS);
    }

}

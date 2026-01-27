package com.beanbeanjuice.cafebot.utility.scheduling;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomChannelType;
import com.beanbeanjuice.cafebot.api.wrapper.type.Birthday;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.time.Duration;
import java.time.Instant;
import java.time.Month;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.*;

public class BirthdayScheduler extends CustomScheduler {

    public BirthdayScheduler(CafeBot bot) {
        super(bot, "Birthday-Scheduler");
    }

    @Override
    protected void onStart() {
        bot.getLogger().log(this.getClass(), LogLevel.INFO, "Starting the birthday scheduler...", false, false);

        Runnable task = () -> {
            try {
                bot.getLogger().log(BirthdayScheduler.class, LogLevel.INFO, "Checking birthdays...", false, false);
                handleBirthdays().thenAccept(count -> {
                            bot.getLogger().log(
                                    BirthdayScheduler.class,
                                    LogLevel.INFO,
                                    String.format("Wished %d people a happy birthday!", count),
                                    true,
                                    count > 0
                            );
                        }
                );
            } catch (Exception e) {
                bot.getLogger().log(this.getClass(), LogLevel.WARN, "Error Checking Birthdays: " + e.getMessage(), true, false);
            }
        };

        this.scheduler.schedule(task, 1, TimeUnit.MINUTES);

        // Calculate delay until the next top of the hour
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime nextHour = now.truncatedTo(ChronoUnit.HOURS).plusHours(1);
        long initialDelay = Duration.between(now, nextHour).toMillis();

        // Run every hour ON the hour
        scheduler.scheduleAtFixedRate(
                task,
                initialDelay,
                TimeUnit.HOURS.toMillis(1),
                TimeUnit.MILLISECONDS
        );
    }

    /**
     * @return Number of current birthdays wished.
     */
    private CompletableFuture<Integer> handleBirthdays() {
        return bot.getCafeAPI()
                .getBirthdayApi()
                .getCurrentBirthdays()
                .thenCompose((birthdays) -> {

                    List<CompletableFuture<Void>> tasks = birthdays.stream()
                            .map(this::handleSingleBirthday)
                            .toList();

                    return CompletableFuture
                            .allOf(tasks.toArray(new CompletableFuture[0]))
                            .thenApply((v) -> birthdays.size());
                });
    }

    private CompletableFuture<Void> handleSingleBirthday(Birthday birthday) {
        if (birthday.getLastMentionedAt()
                .map(time -> time.isAfter(Instant.now().minus(72, ChronoUnit.HOURS)))
                .orElse(false)) {
            return CompletableFuture.completedFuture(null);
        }

        String userId = birthday.getUserId();

        sendBirthdayWishToUser(userId);

        return bot.getCafeAPI()
                .getMutualGuildsApi()
                .getMutualGuilds(userId)
                .thenCompose(mutualGuildIds -> {

                    List<CompletableFuture<Void>> guildTasks = mutualGuildIds.stream()
                            .map(guildId ->
                                    bot.getCafeAPI()
                                            .getCustomChannelApi()
                                            .getCustomChannel(guildId, CustomChannelType.BIRTHDAY)
                                            .thenAccept(channel ->
                                                    sendBirthdayWishToServer(
                                                            channel.getGuildId(),
                                                            channel.getChannelId(),
                                                            userId,
                                                            birthday
                                                    )
                                            )
                            )
                            .toList();

                    return CompletableFuture.allOf(
                            guildTasks.toArray(new CompletableFuture[0])
                    );
                });
    }

    private void sendBirthdayWishToUser(String userId) {
        this.bot.getShardManager().retrieveUserById(userId).queue( (user) -> {
            user.openPrivateChannel().queue((privateChannel) -> {
                    privateChannel.sendMessage(
                            """
                            Psst... <a:cafeBot:1119635469727191190> It's busy here at the cafe right now but I just
                            wanted to wish you a happy birthday! I saved some cake for you~ 🍰
                            I hope you have an amazing day with lots of presents! 🥰
                            """
                    ).queue(
                            (success) -> { this.bot.getCafeAPI().getBirthdayApi().updateBirthdayMention(userId); },
                            (error) -> this.bot.getLogger().log(this.getClass(), LogLevel.WARN, "Error Wishing Birthday: " + error.getMessage(), false, false)
                    );
                });
            },
            (error) -> {
                this.bot.getLogger().log(this.getClass(), LogLevel.WARN, "Error Wishing Birthday: " + userId + " - " + error.getMessage(), false, false);
            }
        );
    }

    private void sendBirthdayWishToServer(String guildId, String channelId, String userId, Birthday birthday) {
        this.bot.getLogger().log(this.getClass(), LogLevel.DEBUG, "Attempting to send to guild.", true, false);

        Guild guild = this.bot.getShardManager().getGuildById(guildId);
        if (guild == null) return;

        this.bot.getLogger().log(this.getClass(), LogLevel.DEBUG, "Guild Found", true, false);

        Member member = guild.retrieveMemberById(userId).complete();
        if (member == null) return;

        this.bot.getLogger().log(this.getClass(), LogLevel.DEBUG, "Member Found", true, false);

        TextChannel channel = guild.getTextChannelById(channelId);
        if (channel == null) return;

        this.bot.getLogger().log(this.getClass(), LogLevel.DEBUG, "Channel Found", true, false);

        String wish = String.format(
                "%s was born on %s %d! Wish them a happy birthday! 🎂",
                member.getAsMention(),
                Month.of(birthday.getMonth()),
                birthday.getDay()
        );

        MessageEmbed embed = new EmbedBuilder()
                .setAuthor(member.getNickname(), null, member.getAvatarUrl())
                .setDescription(wish)
                .setColor(Helper.getRandomColor())
                .build();

        this.bot.getLogger().log(this.getClass(), LogLevel.DEBUG, "Sending Wish", true, false);

        channel.sendMessage("It's someone's birthday! 🥳").addEmbeds(embed).queue();
    }

}

package com.beanbeanjuice.cafebot.api.wrapper;

import com.beanbeanjuice.cafebot.api.wrapper.api.discord.MenuApi;
import com.beanbeanjuice.cafebot.api.wrapper.api.discord.generic.BotSettingsApi;
import com.beanbeanjuice.cafebot.api.wrapper.api.discord.server.*;
import com.beanbeanjuice.cafebot.api.wrapper.api.discord.user.*;
import com.beanbeanjuice.cafebot.api.wrapper.api.discord.generic.GreetingApi;
import lombok.Getter;

/**
 * A class used for the {@link CafeAPI}.
 *
 * @author beanbeanjuice
 * @since 1.1.1
 */
@Getter
public class CafeAPI {

    // General APIs
    private final BotSettingsApi botSettingsApi;
    private final GreetingApi greetingApi;

    // Discord APIs
    private final MenuApi menuApi;

    // User APIs
    private final BirthdayApi birthdayApi;
    private final DonationApi donationApi;
    private final GamesApi gamesApi;
    private final InteractionsApi interactionsApi;
    private final OrderApi orderApi;
    private final ServeWordsApi serveWordsApi;
    private final UserApi userApi;

    // Server APIs
    private final AirportApi airportApi;
    private final CountingApi countingApi;
    private final CustomChannelApi customChannelApi;
    private final CustomRoleApi customRoleApi;
    private final GuildApi guildApi;
    private final MutualGuildsApi mutualGuildsApi;
    private final PollApi pollApi;
    private final RaffleApi raffleApi;
    private final TwitchChannelApi twitchChannelApi;
    private final VoiceRoleApi voiceRoleApi;

    /**
     * Create a new {@link CafeAPI} object.
     * @param token The {@link String token} for keeping track of statistics.
     * @see <a href="https://docs.kawaii.red/tutorials/token">Token</a>
     * @see <a href="https://docs.kawaii.red/">Kawaii API Documentation</a>
     */
    public CafeAPI(String baseUrl, String token) {
        // General APIs
        this.botSettingsApi = new BotSettingsApi(baseUrl, token);
        this.greetingApi = new GreetingApi(baseUrl, token);

        // Discord APIs
        this.menuApi = new MenuApi(baseUrl, token);

        // User APIs
        this.birthdayApi = new BirthdayApi(baseUrl, token);
        this.donationApi = new DonationApi(baseUrl, token);
        this.gamesApi = new GamesApi(baseUrl, token);
        this.interactionsApi = new InteractionsApi(baseUrl, token);
        this.orderApi = new OrderApi(baseUrl, token);
        this.serveWordsApi = new ServeWordsApi(baseUrl, token);
        this.userApi = new UserApi(baseUrl, token);

        // Server APIs
        this.airportApi = new AirportApi(baseUrl, token);
        this.countingApi = new CountingApi(baseUrl, token);
        this.customChannelApi = new CustomChannelApi(baseUrl, token);
        this.customRoleApi = new CustomRoleApi(baseUrl, token);
        this.guildApi = new GuildApi(baseUrl, token);
        this.mutualGuildsApi = new MutualGuildsApi(baseUrl, token);
        this.pollApi = new PollApi(baseUrl, token);
        this.raffleApi = new RaffleApi(baseUrl, token);
        this.twitchChannelApi = new TwitchChannelApi(baseUrl, token);
        this.voiceRoleApi = new VoiceRoleApi(baseUrl, token);
    }

}

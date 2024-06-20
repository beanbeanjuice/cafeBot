package com.beanbeanjuice.cafeapi;

import com.beanbeanjuice.KawaiiAPI;
import com.beanbeanjuice.cafeapi.cafebot.goodbyes.Goodbyes;
import com.beanbeanjuice.cafeapi.requests.*;
import com.beanbeanjuice.cafeapi.user.Users;
import com.beanbeanjuice.cafeapi.cafebot.beancoins.users.DonationUsers;
import com.beanbeanjuice.cafeapi.cafebot.birthdays.Birthdays;
import com.beanbeanjuice.cafeapi.cafebot.cafe.CafeUsers;
import com.beanbeanjuice.cafeapi.cafebot.codes.GeneratedCodes;
import com.beanbeanjuice.cafeapi.cafebot.counting.GlobalCountingInformation;
import com.beanbeanjuice.cafeapi.cafebot.guilds.GlobalGuildInformation;
import com.beanbeanjuice.cafeapi.cafebot.interactions.Interactions;
import com.beanbeanjuice.cafeapi.cafebot.interactions.pictures.InteractionPictures;
import com.beanbeanjuice.cafeapi.cafebot.minigames.winstreaks.WinStreaks;
import com.beanbeanjuice.cafeapi.cafebot.polls.Polls;
import com.beanbeanjuice.cafeapi.cafebot.raffles.Raffles;
import com.beanbeanjuice.cafeapi.cafebot.twitches.GuildTwitches;
import com.beanbeanjuice.cafeapi.cafebot.version.Versions;
import com.beanbeanjuice.cafeapi.cafebot.voicebinds.VoiceChannelBinds;
import com.beanbeanjuice.cafeapi.cafebot.welcomes.Welcomes;
import com.beanbeanjuice.cafeapi.cafebot.words.Words;
import lombok.Getter;

import java.util.TimeZone;

public class CafeAPI {

    private String apiKey;
    private String userAgent;
    @Getter private static RequestLocation requestLocation;
    public KawaiiAPI KAWAII_API;

    public Users USER;

    public Words WORD;
    public Welcomes WELCOME;
    public Goodbyes GOODBYE;
    public VoiceChannelBinds VOICE_CHANNEL_BIND;
    public Raffles RAFFLE;
    public Polls POLL;
    public WinStreaks WIN_STREAK;
    public Interactions INTERACTION;
    public GuildTwitches TWITCH;
    public GlobalGuildInformation GUILD;
    public GeneratedCodes GENERATED_CODE;
    public Versions VERSION;
    public GlobalCountingInformation COUNTING_INFORMATION;
    public CafeUsers CAFE_USER;
    public Birthdays BIRTHDAY;
    public DonationUsers DONATION_USER;
    public InteractionPictures INTERACTION_PICTURE;

    /**
     * Creates a new {@link CafeAPI} object.
     * @param username The {@link String username}.
     * @param password The {@link String password}.
     * @param requestLocation The {@link RequestLocation requestLocation}.
     */
    public CafeAPI(String username, String password, RequestLocation requestLocation) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        this.userAgent = username;
        CafeAPI.requestLocation = requestLocation;

        try {
            apiKey = getToken(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        USER = new Users(apiKey);

        // cafeBot
        WORD = new Words(apiKey);
        WELCOME = new Welcomes(apiKey);
        GOODBYE = new Goodbyes(apiKey);
        VOICE_CHANNEL_BIND = new VoiceChannelBinds(apiKey);
        RAFFLE = new Raffles(apiKey);
        POLL = new Polls(apiKey);
        WIN_STREAK = new WinStreaks(apiKey);
        INTERACTION = new Interactions(apiKey);
        TWITCH = new GuildTwitches(apiKey);
        GUILD = new GlobalGuildInformation(apiKey);
        GENERATED_CODE = new GeneratedCodes(apiKey);
        VERSION = new Versions(apiKey);
        COUNTING_INFORMATION = new GlobalCountingInformation(apiKey);
        CAFE_USER = new CafeUsers(apiKey);
        BIRTHDAY = new Birthdays(apiKey);
        DONATION_USER = new DonationUsers(apiKey);
        INTERACTION_PICTURE = new InteractionPictures(apiKey, this);

        KAWAII_API = new KawaiiAPI("anonymous");
    }

    /**
     * Sets the {@link KawaiiAPI} token.
     * @param token The {@link String} token for the {@link KawaiiAPI}.
     */
    public void setKawaiiAPI(String token) {
        KAWAII_API = new KawaiiAPI(token);
    }

    private String getToken(String username, String password) {
        Request request = new RequestBuilder(RequestRoute.CAFE, RequestType.POST)
                .setRoute("/user/login")
                .addParameter("username", username)
                .addParameter("password", password)
                .build().orElseThrow();

        return request.getData().get("api_key").textValue();
    }

}

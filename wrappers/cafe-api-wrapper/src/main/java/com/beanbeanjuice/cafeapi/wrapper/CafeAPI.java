package com.beanbeanjuice.cafeapi.wrapper;

import com.beanbeanjuice.kawaiiapi.wrapper.KawaiiAPI;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.goodbyes.Goodbyes;
import com.beanbeanjuice.cafeapi.wrapper.requests.*;
import com.beanbeanjuice.cafeapi.wrapper.user.Users;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.beancoins.users.DonationUsers;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.birthdays.Birthdays;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.cafe.CafeUsers;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.codes.GeneratedCodes;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.counting.GlobalCountingInformation;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.guilds.GlobalGuildInformation;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.interactions.Interactions;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.interactions.pictures.InteractionPictures;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.minigames.winstreaks.WinStreaks;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.polls.Polls;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.raffles.Raffles;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.twitches.GuildTwitches;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.version.Versions;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.voicebinds.VoiceChannelBinds;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.welcomes.Welcomes;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.words.Words;
import lombok.Getter;

import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            Logger.getLogger(CafeAPI.class.getName()).log(Level.SEVERE, "Unable to login. Could the username or password be incorrect?");
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

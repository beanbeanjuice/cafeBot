package com.beanbeanjuice.cafeapi.beta;

import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.guilds.GuildInformationType;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.ConflictException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.NotFoundException;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GuildInformationTest {

    @Test
    @DisplayName("Guild Information API Test")
    public void testGuildInformationAPI() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        // Makes sure the guild is deleted beforehand.
        Assertions.assertTrue(cafeAPI.GUILD.deleteGuildInformation("816880157490675732"));

        // Makes sure the guild does not exist.
        Assertions.assertThrows(NotFoundException.class, () -> cafeAPI.GUILD.getGuildInformation("816880157490675732"));

        // Makes sure the guild can be created.
        Assertions.assertTrue(cafeAPI.GUILD.createGuildInformation("816880157490675732"));

        // Makes sure the guild cannot be created as it already exists.
        Assertions.assertThrows(ConflictException.class, () -> cafeAPI.GUILD.createGuildInformation("816880157490675732"));

        // Makes sure the prefix is default and can be changed.
        Assertions.assertEquals("!!", cafeAPI.GUILD.getAllGuildInformation().get("816880157490675732").getSetting(GuildInformationType.PREFIX));
        Assertions.assertTrue(cafeAPI.GUILD.updateGuildInformation("816880157490675732", GuildInformationType.PREFIX, "beta!"));
        Assertions.assertEquals("beta!", cafeAPI.GUILD.getGuildInformation("816880157490675732").getSetting(GuildInformationType.PREFIX));

        // Makes sure the moderator role ID is default and can be changed.
        Assertions.assertEquals("0", cafeAPI.GUILD.getAllGuildInformation().get("816880157490675732").getSetting(GuildInformationType.MODERATOR_ROLE_ID));
        Assertions.assertTrue(cafeAPI.GUILD.updateGuildInformation("816880157490675732", GuildInformationType.MODERATOR_ROLE_ID, "738590591767543921"));
        Assertions.assertEquals("738590591767543921", cafeAPI.GUILD.getGuildInformation("816880157490675732").getSetting(GuildInformationType.MODERATOR_ROLE_ID));

        // Makes sure the twitch channel ID is default and can be changed.
        Assertions.assertEquals("0", cafeAPI.GUILD.getAllGuildInformation().get("816880157490675732").getSetting(GuildInformationType.TWITCH_CHANNEL_ID));
        Assertions.assertTrue(cafeAPI.GUILD.updateGuildInformation("816880157490675732", GuildInformationType.TWITCH_CHANNEL_ID, "856782983709458472"));
        Assertions.assertEquals("856782983709458472", cafeAPI.GUILD.getGuildInformation("816880157490675732").getSetting(GuildInformationType.TWITCH_CHANNEL_ID));

        // Makes sure the muted role ID is default and can be changed.
        Assertions.assertEquals("0", cafeAPI.GUILD.getAllGuildInformation().get("816880157490675732").getSetting(GuildInformationType.MUTED_ROLE_ID));
        Assertions.assertTrue(cafeAPI.GUILD.updateGuildInformation("816880157490675732", GuildInformationType.MUTED_ROLE_ID, "856776166618300436"));
        Assertions.assertEquals("856776166618300436", cafeAPI.GUILD.getGuildInformation("816880157490675732").getSetting(GuildInformationType.MUTED_ROLE_ID));

        // Makes sure the live notifications role ID is default and can be changed.
        Assertions.assertEquals("0", cafeAPI.GUILD.getAllGuildInformation().get("816880157490675732").getSetting(GuildInformationType.LIVE_NOTIFICATIONS_ROLE_ID));
        Assertions.assertTrue(cafeAPI.GUILD.updateGuildInformation("816880157490675732", GuildInformationType.LIVE_NOTIFICATIONS_ROLE_ID, "858231567196618772"));
        Assertions.assertEquals("858231567196618772", cafeAPI.GUILD.getGuildInformation("816880157490675732").getSetting(GuildInformationType.LIVE_NOTIFICATIONS_ROLE_ID));

        // Makes sure notify on update is default and can be changed.
        Assertions.assertTrue(Boolean.parseBoolean(cafeAPI.GUILD.getAllGuildInformation().get("816880157490675732").getSetting(GuildInformationType.NOTIFY_ON_UPDATE)));
        Assertions.assertTrue(cafeAPI.GUILD.updateGuildInformation("816880157490675732", GuildInformationType.NOTIFY_ON_UPDATE, false));
        Assertions.assertFalse(Boolean.parseBoolean(cafeAPI.GUILD.getGuildInformation("816880157490675732").getSetting(GuildInformationType.NOTIFY_ON_UPDATE)));

        // Makes sure the update channel ID is default and can be changed.
        Assertions.assertEquals("0", cafeAPI.GUILD.getAllGuildInformation().get("816880157490675732").getSetting(GuildInformationType.UPDATE_CHANNEL_ID));
        Assertions.assertTrue(cafeAPI.GUILD.updateGuildInformation("816880157490675732", GuildInformationType.UPDATE_CHANNEL_ID, "645490120052703239"));
        Assertions.assertEquals("645490120052703239", cafeAPI.GUILD.getGuildInformation("816880157490675732").getSetting(GuildInformationType.UPDATE_CHANNEL_ID));

        // Makes sure the counting channel ID is default and can be changed.
        Assertions.assertEquals("0", cafeAPI.GUILD.getAllGuildInformation().get("816880157490675732").getSetting(GuildInformationType.COUNTING_CHANNEL_ID));
        Assertions.assertTrue(cafeAPI.GUILD.updateGuildInformation("816880157490675732", GuildInformationType.COUNTING_CHANNEL_ID, "733203398337495041"));
        Assertions.assertEquals("733203398337495041", cafeAPI.GUILD.getGuildInformation("816880157490675732").getSetting(GuildInformationType.COUNTING_CHANNEL_ID));

        // Makes sure the poll channel ID is default and can be changed.
        Assertions.assertEquals("0", cafeAPI.GUILD.getAllGuildInformation().get("816880157490675732").getSetting(GuildInformationType.POLL_CHANNEL_ID));
        Assertions.assertTrue(cafeAPI.GUILD.updateGuildInformation("816880157490675732", GuildInformationType.POLL_CHANNEL_ID, "359760149683896320"));
        Assertions.assertEquals("359760149683896320", cafeAPI.GUILD.getGuildInformation("816880157490675732").getSetting(GuildInformationType.POLL_CHANNEL_ID));

        // Makes sure the raffle channel ID is default and can be changed.
        Assertions.assertEquals("0", cafeAPI.GUILD.getAllGuildInformation().get("816880157490675732").getSetting(GuildInformationType.RAFFLE_CHANNEL_ID));
        Assertions.assertTrue(cafeAPI.GUILD.updateGuildInformation("816880157490675732", GuildInformationType.RAFFLE_CHANNEL_ID, "813817485014990860"));
        Assertions.assertEquals("813817485014990860", cafeAPI.GUILD.getGuildInformation("816880157490675732").getSetting(GuildInformationType.RAFFLE_CHANNEL_ID));

        // Makes sure the birthday channel ID is default and can be changed.
        Assertions.assertEquals("0", cafeAPI.GUILD.getAllGuildInformation().get("816880157490675732").getSetting(GuildInformationType.BIRTHDAY_CHANNEL_ID));
        Assertions.assertTrue(cafeAPI.GUILD.updateGuildInformation("816880157490675732", GuildInformationType.BIRTHDAY_CHANNEL_ID, "500658624109084682"));
        Assertions.assertEquals("500658624109084682", cafeAPI.GUILD.getGuildInformation("816880157490675732").getSetting(GuildInformationType.BIRTHDAY_CHANNEL_ID));

        // Makes sure the welcome channel ID is default and can be changed.
        Assertions.assertEquals("0", cafeAPI.GUILD.getAllGuildInformation().get("816880157490675732").getSetting(GuildInformationType.WELCOME_CHANNEL_ID));
        Assertions.assertTrue(cafeAPI.GUILD.updateGuildInformation("816880157490675732", GuildInformationType.WELCOME_CHANNEL_ID, "877240864240443412"));
        Assertions.assertEquals("877240864240443412", cafeAPI.GUILD.getGuildInformation("816880157490675732").getSetting(GuildInformationType.WELCOME_CHANNEL_ID));

        // Makes sure the goodbye channel ID is default and can be changed.
        Assertions.assertEquals("0", cafeAPI.GUILD.getAllGuildInformation().get("816880157490675732").getSetting(GuildInformationType.GOODBYE_CHANNEL_ID));
        Assertions.assertTrue(cafeAPI.GUILD.updateGuildInformation("816880157490675732", GuildInformationType.GOODBYE_CHANNEL_ID, "877240864240443412"));
        Assertions.assertEquals("877240864240443412", cafeAPI.GUILD.getGuildInformation("816880157490675732").getSetting(GuildInformationType.GOODBYE_CHANNEL_ID));

        // Makes sure the log channel ID is default and can be changed.
        Assertions.assertEquals("0", cafeAPI.GUILD.getAllGuildInformation().get("816880157490675732").getSetting(GuildInformationType.LOG_CHANNEL_ID));
        Assertions.assertTrue(cafeAPI.GUILD.updateGuildInformation("816880157490675732", GuildInformationType.LOG_CHANNEL_ID, "857024885968732181"));
        Assertions.assertEquals("857024885968732181", cafeAPI.GUILD.getGuildInformation("816880157490675732").getSetting(GuildInformationType.LOG_CHANNEL_ID));

        // Makes sure the venting channel ID is default and can be changed.
        Assertions.assertEquals("0", cafeAPI.GUILD.getAllGuildInformation().get("816880157490675732").getSetting(GuildInformationType.VENTING_CHANNEL_ID));
        Assertions.assertTrue(cafeAPI.GUILD.updateGuildInformation("816880157490675732", GuildInformationType.VENTING_CHANNEL_ID, "125227483518861312"));
        Assertions.assertEquals("125227483518861312", cafeAPI.GUILD.getGuildInformation("816880157490675732").getSetting(GuildInformationType.VENTING_CHANNEL_ID));

        // Makes sure the AI response status is default and can be changed.
        Assertions.assertFalse(Boolean.parseBoolean(cafeAPI.GUILD.getAllGuildInformation().get("816880157490675732").getSetting(GuildInformationType.AI_RESPONSE)));
        Assertions.assertTrue(cafeAPI.GUILD.updateGuildInformation("816880157490675732", GuildInformationType.AI_RESPONSE, true));
        Assertions.assertTrue(Boolean.parseBoolean(cafeAPI.GUILD.getGuildInformation("816880157490675732").getSetting(GuildInformationType.AI_RESPONSE)));

        // Makes sure the daily channel ID is default and can be changed.
        Assertions.assertEquals("0", cafeAPI.GUILD.getAllGuildInformation().get("816880157490675732").getSetting(GuildInformationType.DAILY_CHANNEL_ID));
        Assertions.assertTrue(cafeAPI.GUILD.updateGuildInformation("816880157490675732", GuildInformationType.DAILY_CHANNEL_ID, "606222472274116676"));
        Assertions.assertEquals("606222472274116676", cafeAPI.GUILD.getGuildInformation("816880157490675732").getSetting(GuildInformationType.DAILY_CHANNEL_ID));

        // Makes sure the guild "bruh" doesn't exist.
        Assertions.assertThrows(NotFoundException.class, () -> cafeAPI.GUILD.updateGuildInformation("bruh", GuildInformationType.NOTIFY_ON_UPDATE, false));

        // Makes sure the guild is deleted beforehand.
        Assertions.assertTrue(cafeAPI.GUILD.deleteGuildInformation("816880157490675732"));

        // Makes sure the guild does not exist.
        Assertions.assertThrows(NotFoundException.class, () -> cafeAPI.GUILD.getGuildInformation("816880157490675732"));

    }

}

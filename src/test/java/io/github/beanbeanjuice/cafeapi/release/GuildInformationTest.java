package io.github.beanbeanjuice.cafeapi.release;

import io.github.beanbeanjuice.cafeapi.CafeAPI;
import io.github.beanbeanjuice.cafeapi.cafebot.guilds.GuildInformationType;
import io.github.beanbeanjuice.cafeapi.exception.ConflictException;
import io.github.beanbeanjuice.cafeapi.exception.NotFoundException;
import io.github.beanbeanjuice.cafeapi.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GuildInformationTest {

    @Test
    @DisplayName("Guild Information API Test")
    public void testGuildInformationAPI() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("RELEASE_API_PASSWORD"), RequestLocation.RELEASE);

        // Makes sure the guild is deleted beforehand.
        Assertions.assertTrue(cafeAPI.guildInformations().deleteGuildInformation("817975989547040795"));

        // Makes sure the guild does not exist.
        Assertions.assertThrows(NotFoundException.class, () -> cafeAPI.guildInformations().getGuildInformation("817975989547040795"));

        // Makes sure the guild can be created.
        Assertions.assertTrue(cafeAPI.guildInformations().createGuildInformation("817975989547040795"));

        // Makes sure the guild cannot be created as it already exists.
        Assertions.assertThrows(ConflictException.class, () -> cafeAPI.guildInformations().createGuildInformation("817975989547040795"));

        // Makes sure the prefix is default and can be changed.
        Assertions.assertEquals("!!", cafeAPI.guildInformations().getAllGuildInformation().get("817975989547040795").getPrefix());
        Assertions.assertTrue(cafeAPI.guildInformations().updateGuildInformation("817975989547040795", GuildInformationType.PREFIX, "beta!"));
        Assertions.assertEquals("beta!", cafeAPI.guildInformations().getGuildInformation("817975989547040795").getPrefix());

        // Makes sure the moderator role ID is default and can be changed.
        Assertions.assertEquals("0", cafeAPI.guildInformations().getAllGuildInformation().get("817975989547040795").getModeratorRoleID());
        Assertions.assertTrue(cafeAPI.guildInformations().updateGuildInformation("817975989547040795", GuildInformationType.MODERATOR_ROLE_ID, "738590591767543921"));
        Assertions.assertEquals("738590591767543921", cafeAPI.guildInformations().getGuildInformation("817975989547040795").getModeratorRoleID());

        // Makes sure the twitch channel ID is default and can be changed.
        Assertions.assertEquals("0", cafeAPI.guildInformations().getAllGuildInformation().get("817975989547040795").getTwitchChannelID());
        Assertions.assertTrue(cafeAPI.guildInformations().updateGuildInformation("817975989547040795", GuildInformationType.TWITCH_CHANNEL_ID, "856782983709458472"));
        Assertions.assertEquals("856782983709458472", cafeAPI.guildInformations().getGuildInformation("817975989547040795").getTwitchChannelID());

        // Makes sure the muted role ID is default and can be changed.
        Assertions.assertEquals("0", cafeAPI.guildInformations().getAllGuildInformation().get("817975989547040795").getMutedRoleID());
        Assertions.assertTrue(cafeAPI.guildInformations().updateGuildInformation("817975989547040795", GuildInformationType.MUTED_ROLE_ID, "856776166618300436"));
        Assertions.assertEquals("856776166618300436", cafeAPI.guildInformations().getGuildInformation("817975989547040795").getMutedRoleID());

        // Makes sure the live notifications role ID is default and can be changed.
        Assertions.assertEquals("0", cafeAPI.guildInformations().getAllGuildInformation().get("817975989547040795").getLiveNotificationsRoleID());
        Assertions.assertTrue(cafeAPI.guildInformations().updateGuildInformation("817975989547040795", GuildInformationType.LIVE_NOTIFICATIONS_ROLE_ID, "858231567196618772"));
        Assertions.assertEquals("858231567196618772", cafeAPI.guildInformations().getGuildInformation("817975989547040795").getLiveNotificationsRoleID());

        // Makes sure notify on update is default and can be changed.
        Assertions.assertEquals(true, cafeAPI.guildInformations().getAllGuildInformation().get("817975989547040795").getNotifyOnUpdate());
        Assertions.assertTrue(cafeAPI.guildInformations().updateGuildInformation("817975989547040795", GuildInformationType.NOTIFY_ON_UPDATE, false));
        Assertions.assertEquals(false, cafeAPI.guildInformations().getGuildInformation("817975989547040795").getNotifyOnUpdate());

        // Makes sure the update channel ID is default and can be changed.
        Assertions.assertEquals("0", cafeAPI.guildInformations().getAllGuildInformation().get("817975989547040795").getUpdateChannelID());
        Assertions.assertTrue(cafeAPI.guildInformations().updateGuildInformation("817975989547040795", GuildInformationType.UPDATE_CHANNEL_ID, "645490120052703239"));
        Assertions.assertEquals("645490120052703239", cafeAPI.guildInformations().getGuildInformation("817975989547040795").getUpdateChannelID());

        // Makes sure the counting channel ID is default and can be changed.
        Assertions.assertEquals("0", cafeAPI.guildInformations().getAllGuildInformation().get("817975989547040795").getCountingChannelID());
        Assertions.assertTrue(cafeAPI.guildInformations().updateGuildInformation("817975989547040795", GuildInformationType.COUNTING_CHANNEL_ID, "733203398337495041"));
        Assertions.assertEquals("733203398337495041", cafeAPI.guildInformations().getGuildInformation("817975989547040795").getCountingChannelID());

        // Makes sure the poll channel ID is default and can be changed.
        Assertions.assertEquals("0", cafeAPI.guildInformations().getAllGuildInformation().get("817975989547040795").getPollChannelID());
        Assertions.assertTrue(cafeAPI.guildInformations().updateGuildInformation("817975989547040795", GuildInformationType.POLL_CHANNEL_ID, "359760149683896320"));
        Assertions.assertEquals("359760149683896320", cafeAPI.guildInformations().getGuildInformation("817975989547040795").getPollChannelID());

        // Makes sure the raffle channel ID is default and can be changed.
        Assertions.assertEquals("0", cafeAPI.guildInformations().getAllGuildInformation().get("817975989547040795").getRaffleChannelID());
        Assertions.assertTrue(cafeAPI.guildInformations().updateGuildInformation("817975989547040795", GuildInformationType.RAFFLE_CHANNEL_ID, "813817485014990860"));
        Assertions.assertEquals("813817485014990860", cafeAPI.guildInformations().getGuildInformation("817975989547040795").getRaffleChannelID());

        // Makes sure the birthday channel ID is default and can be changed.
        Assertions.assertEquals("0", cafeAPI.guildInformations().getAllGuildInformation().get("817975989547040795").getBirthdayChannelID());
        Assertions.assertTrue(cafeAPI.guildInformations().updateGuildInformation("817975989547040795", GuildInformationType.BIRTHDAY_CHANNEL_ID, "500658624109084682"));
        Assertions.assertEquals("500658624109084682", cafeAPI.guildInformations().getGuildInformation("817975989547040795").getBirthdayChannelID());

        // Makes sure the welcome channel ID is default and can be changed.
        Assertions.assertEquals("0", cafeAPI.guildInformations().getAllGuildInformation().get("817975989547040795").getWelcomeChannelID());
        Assertions.assertTrue(cafeAPI.guildInformations().updateGuildInformation("817975989547040795", GuildInformationType.WELCOME_CHANNEL_ID, "877240864240443412"));
        Assertions.assertEquals("877240864240443412", cafeAPI.guildInformations().getGuildInformation("817975989547040795").getWelcomeChannelID());

        // Makes sure the log channel ID is default and can be changed.
        Assertions.assertEquals("0", cafeAPI.guildInformations().getAllGuildInformation().get("817975989547040795").getLogChannelID());
        Assertions.assertTrue(cafeAPI.guildInformations().updateGuildInformation("817975989547040795", GuildInformationType.LOG_CHANNEL_ID, "857024885968732181"));
        Assertions.assertEquals("857024885968732181", cafeAPI.guildInformations().getGuildInformation("817975989547040795").getLogChannelID());

        // Makes sure the venting channel ID is default and can be changed.
        Assertions.assertEquals("0", cafeAPI.guildInformations().getAllGuildInformation().get("817975989547040795").getVentingChannelID());
        Assertions.assertTrue(cafeAPI.guildInformations().updateGuildInformation("817975989547040795", GuildInformationType.VENTING_CHANNEL_ID, "125227483518861312"));
        Assertions.assertEquals("125227483518861312", cafeAPI.guildInformations().getGuildInformation("817975989547040795").getVentingChannelID());

        // Makes sure the AI response status is default and can be changed.
        Assertions.assertEquals(false, cafeAPI.guildInformations().getAllGuildInformation().get("817975989547040795").getAiResponseStatus());
        Assertions.assertTrue(cafeAPI.guildInformations().updateGuildInformation("817975989547040795", GuildInformationType.AI_RESPONSE, true));
        Assertions.assertEquals(true, cafeAPI.guildInformations().getGuildInformation("817975989547040795").getAiResponseStatus());

        // Makes sure the daily channel ID is default and can be changed.
        Assertions.assertEquals("0", cafeAPI.guildInformations().getAllGuildInformation().get("817975989547040795").getDailyChannelID());
        Assertions.assertTrue(cafeAPI.guildInformations().updateGuildInformation("817975989547040795", GuildInformationType.DAILY_CHANNEL_ID, "606222472274116676"));
        Assertions.assertEquals("606222472274116676", cafeAPI.guildInformations().getGuildInformation("817975989547040795").getDailyChannelID());

        // Makes sure the guild "bruh" doesn't exist.
        Assertions.assertThrows(NotFoundException.class, () -> cafeAPI.guildInformations().updateGuildInformation("bruh", GuildInformationType.NOTIFY_ON_UPDATE, false));

        // Makes sure the guild is deleted beforehand.
        Assertions.assertTrue(cafeAPI.guildInformations().deleteGuildInformation("817975989547040795"));

        // Makes sure the guild does not exist.
        Assertions.assertThrows(NotFoundException.class, () -> cafeAPI.guildInformations().getGuildInformation("817975989547040795"));

    }

}

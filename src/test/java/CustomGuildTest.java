import com.beanbeanjuice.utility.guild.GuildHandler;
import com.beanbeanjuice.utility.logger.LogManager;
import com.beanbeanjuice.utility.sql.SQLServer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.security.auth.login.LoginException;

public class CustomGuildTest {

    private static SQLServer sqlServer;
    private static final String SQL_URL = "beanbeanjuice.com";
    private static final String SQL_PORT = "4001";
    private static final String SQL_USERNAME = "root";
    private static final String SQL_PASSWORD = "Hawaii&Florida12345";
    private static final boolean SQL_ENCRYPT = true;

    private static final String BOT_TOKEN = "Nzk4OTc4NDE3OTk0NDk4MDYx.X_84ow.P05jVyf4YhLXAdBnxxqFH1X4gBs";
    private static final String WEBHOOK_URL = "https://discord.com/api/webhooks/818075614388158485/e6Hm9g1VITD0IB3QqTwC5f3UvN27TOrflH9GjAwkpmOBKVCpeXcgBx4atIrI1k81o3GJ";
    private static JDA jda;
    private static JDABuilder jdaBuilder;
    private static final String BOT_PREFIX = "!!";

    private static Guild guild;
    private static TextChannel logChannel;
    private static final String GUILD_ID = "798830792938881024";
    private static final String LOG_CHANNEL_ID = "799005537542471751";
    private static GuildHandler guildHandler;
    private static LogManager logManager;

    @Test
    @DisplayName("Testing the Custom Guild")
    public void test1() throws LoginException, InterruptedException {

        sqlServer = new SQLServer(SQL_URL, SQL_PORT, SQL_ENCRYPT, SQL_USERNAME, SQL_PASSWORD);
        sqlServer.startConnection();

        logManager = new LogManager("Log Manager", guild, logChannel);

        logManager.addWebhookURL(WEBHOOK_URL);

        jdaBuilder = JDABuilder.createDefault(BOT_TOKEN);
        jdaBuilder.setActivity(Activity.playing("beanbeanjuice is testing this."));

        jdaBuilder.enableIntents(GatewayIntent.GUILD_PRESENCES);
        jdaBuilder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        jdaBuilder.enableIntents(GatewayIntent.GUILD_VOICE_STATES);
        jdaBuilder.enableIntents(GatewayIntent.GUILD_BANS);
        jdaBuilder.enableIntents(GatewayIntent.GUILD_EMOJIS);
        jdaBuilder.enableCache(CacheFlag.ACTIVITY);
        jdaBuilder.enableCache(CacheFlag.CLIENT_STATUS);
        jdaBuilder.enableCache(CacheFlag.EMOTE);
        jdaBuilder.enableCache(CacheFlag.VOICE_STATE);
        jdaBuilder.setMemberCachePolicy(MemberCachePolicy.ALL);
        jdaBuilder.setChunkingFilter(ChunkingFilter.ALL);

        jda = jdaBuilder.build().awaitReady();

        guild = jda.getGuildById(GUILD_ID);
        logChannel = guild.getTextChannelById(LOG_CHANNEL_ID);

        logManager.setGuild(guild);
        logManager.setLogChannel(logChannel);
        guildHandler = new GuildHandler(jda, sqlServer.getConnection(), logManager, BOT_PREFIX);

        Assertions.assertTrue(guildHandler.getGuilds().containsKey("809303046470238218"));
        Assertions.assertEquals(guildHandler.getGuilds().get("809303046470238218").getPrefix(), "!!");

    }

}

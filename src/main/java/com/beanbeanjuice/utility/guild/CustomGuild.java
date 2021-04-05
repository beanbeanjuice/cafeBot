package com.beanbeanjuice.utility.guild;

public class CustomGuild {

    private String guildID;
    private String prefix;
    private GuildHandler guildHandler;

    public CustomGuild(String guildID, String prefix, GuildHandler guildHandler) {
        this.guildID = guildID;
        this.prefix = prefix;
        this.guildHandler = guildHandler;
    }

    public String getGuildID() {
        return guildID;
    }

    public String getPrefix() {
        return prefix;
    }

    public Boolean setPrefix(String newPrefix) {
        return guildHandler.updateGuildPrefix(guildID, newPrefix);
    }

}

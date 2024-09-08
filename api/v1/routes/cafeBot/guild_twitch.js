const express = require('express');
const router = express.Router();

const getConnection = require('./modules/mysql-connection');
const check_authentication = require('../../middleware/check-auth');
const check_admin = require('../../middleware/check-admin.js');

const check_if_twitch_channel_exists = (request, response, next) => {
    guild_id = request.params.guild_id;
    twitch_channel = request.query.twitch_channel;
    twitch_channel = twitch_channel.toLowerCase();

    if (!twitch_channel) {
        response.status(400).json({
            variables: {
                guild_id: guild_id,
                twitch_channel: twitch_channel || "undefined"
            },
            message: "A Variable is Undefined"
        });
        return;
    }

    query = "SELECT * FROM guild_twitch WHERE guild_id = (?) AND twitch_channel = (?);";
    getConnection().query(query, [guild_id, twitch_channel], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        if (!!rows.length) {
            twitch_error = new Error("Twitch Channel (" + twitch_channel + ") Already Exists for Guild (" + guild_id + ")");
            twitch_error.status = 409;
            next(twitch_error);
            return;
        }

        next();
    });
}

// Gets all information for guild's twitch
router.get("/guilds/twitch", check_authentication, check_admin, (request, response, next) => {

    query = "SELECT * FROM guild_twitch;";
    connection = getConnection();
    connection.query(query, (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            guilds_twitch: rows,
            message: "Successfully Grabbed All Guild Twitch Information"
        });
        return;
    });

});

// Gets all information for a specified guild's twitch
router.get("/guilds/twitch/:guild_id", check_authentication, check_admin, (request, response, next) => {

    guild_id = request.params.guild_id;

    query = "SELECT * FROM guild_twitch WHERE guild_id = (?);";
    getConnection().query(query, [guild_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        var twitch_channels = [];
        rows.forEach((row) => {
            twitch_channels.push(row.twitch_channel);
        });

        response.status(200).json({
            twitch_channels: twitch_channels,
            message: "Successfully Retrieved Twitch Channels for Guild (" + guild_id + ")"
        });
        return;
    });
});

// Adds a new twitch channel for a guild.
router.post("/guilds/twitch/:guild_id", check_authentication, check_admin, check_if_twitch_channel_exists, (request, response, next) => {
    guild_id = request.params.guild_id;

    twitch_channel = request.query.twitch_channel;
    twitch_channel = twitch_channel.toLowerCase(); // Converting to lowercase

    query = "INSERT INTO guild_twitch (guild_id, twitch_channel) VALUES (?,?);";
    getConnection().query(query, [guild_id, twitch_channel], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(201).json({
            message: "Successfully Added Twitch Channel for Guild (" + guild_id + ")"
        });
        return;
    });
});

// Deletes a channel for a specified guild.
router.delete("/guilds/twitch/:guild_id", check_authentication, check_admin, (request, response, next) => {
    guild_id = request.params.guild_id;
    twitch_channel = request.query.twitch_channel;

    if (!twitch_channel) {
        response.status(400).json({
            variables: {
                guild_id: guild_id,
                twitch_channel: twitch_channel || "undefined"
            },
            message: "A Variable is Undefined"
        });
        return;
    }

    twitch_channel = twitch_channel.toLowerCase();

    query = "DELETE FROM guild_twitch WHERE guild_id = (?) AND twitch_channel = (?);";
    getConnection().query(query, [guild_id, twitch_channel], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            message: "Successfully Removed Twitch Channel (" + twitch_channel + ") From Guild (" + guild_id + ")"
        });
        return;
    });
});

module.exports = router;

const express = require('express');
const router = express.Router();

const getConnection = require('./modules/mysql-connection');
const check_authentication = require('../../middleware/check-auth');
const check_admin = require('../../middleware/check-admin.js');

const check_if_guild_exists = (request, response, next) => {
    guild_id = request.params.guild_id;

    query = "SELECT * FROM guild_information WHERE guild_id = (?);";
    getConnection().query(query, [guild_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        if (!rows.length) {
            const guild_error = new Error("The Guild (" + guild_id + ") Does Not Exist");
            guild_error.status = 404;
            next(guild_error);
            return;
        }

        next();
    });
}

const check_if_guild_does_not_exist = (request, response, next) => {
    guild_id = request.params.guild_id;

    query = "SELECT * FROM guild_information WHERE guild_id = (?);";
    getConnection().query(query, [guild_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        if (!!rows.length) {
            const guild_error = new Error("The Guild (" + guild_id + ") Already Exists");
            guild_error.status = 409;
            next(guild_error);
            return;
        }

        next();
    });
}

// Gets a list of all guilds with their information.
router.get("/guilds", check_authentication, check_admin, (request, response, next) => {
    query = "SELECT * FROM guild_information";
    connection = getConnection();
    connection.query(query, (error, rows, fields) => {

        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            guilds: rows
        });
        return;

    });
});

// Gets a specific guild with its information
router.get("/guilds/:guild_id", check_authentication, check_admin, check_if_guild_exists, (request, response, next) => {

    guild_id = request.params.guild_id;

    query = "SELECT * FROM guild_information WHERE guild_id = (?);";
    connection = getConnection();
    connection.query(query, [guild_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            guild: rows[0],
            message: "Successfully Retrieved Any Guilds with ID (" + guild_id + ")"
        })
    });

});

// Creates a new guild.
router.post("/guilds/:guild_id", check_authentication, check_admin, check_if_guild_does_not_exist, (request, response, next) => {
    guild_id = request.params.guild_id;

    query = "INSERT INTO guild_information (guild_id, prefix) VALUES (?,?);";
    getConnection().query(query, [guild_id, "!!"], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(201).json({
            message: "Successfully Created Guild (" + guild_id + ") with Default Settings"
        });
        return;
    })
});

// Deletes a specified guild.
router.delete("/guilds/:guild_id", check_authentication, check_admin, (request, response, next) => {
    guild_id = request.params.guild_id;

    query = "DELETE FROM guild_information WHERE guild_id = (?);";
    getConnection().query(query, [guild_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            message: "Successfully Deleted Guild (" + guild_id + ")"
        });
        return;
    });
});

// Updates a guild's information.
router.patch("/guilds/:guild_id", check_authentication, check_admin, check_if_guild_exists, (request, response, next) => {
    guild_id = request.params.guild_id;

    type = request.query.type;
    value = request.query.value;

    var valid_variables = {
        prefix: "String",
        moderator_role_id: "Big Integer/Long",
        twitch_channel_id: "Big Integer/Long",
        muted_role_id: "Big Integer/Long",
        live_notifications_role_id: "Big Integer/Long",
        notify_on_update: "Tiny Integer",
        update_channel_id: "Big Integer/Long",
        counting_channel_id: "Big Integer/Long",
        poll_channel_id: "Big Integer/Long",
        raffle_channel_id: "Big Integer/Long",
        birthday_channel_id: "Big Integer/Long",
        welcome_channel_id: "Big Integer/Long",
        log_channel_id: "Big Integer/Long",
        venting_channel_id: "Big Integer/Long",
        ai_response: "Tiny Integer",
        daily_channel_id: "Big Integer/Long",
        goodbye_channel_id: "Big Integer/Long"
    };

    if (!type || !valid_variables[type]) {
        response.status(400).json({
            valid_variables,
            message: "A Valid Type is Missing"
        });
        return;
    }

    // Retrieves the value of the key, "type"
    value_type = Object.getOwnPropertyDescriptor(valid_variables, type).value;

    if (!value) {
        response.status(500).json({
            variables_required: {
                type: type,
                value_type: value_type
            },
            message: "A Value Is Required",
            example: "If the type is a Timestamp, then the value would be 2021-07-07 13:46:36."
        });
        return;
    }

    query = "UPDATE guild_information SET ?? = (?) WHERE guild_id = (?);";
    getConnection().query(query, [type, value, guild_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            message: "Successfully Updated Guild (" + guild_id + ")"
        });
        return;
    });
});

module.exports = router;

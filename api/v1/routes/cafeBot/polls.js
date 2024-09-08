const express = require('express');
const router = express.Router();

const getConnection = require('./modules/mysql-connection');
const check_authentication = require('../../middleware/check-auth');
const check_admin = require('../../middleware/check-admin.js');

const check_if_poll_does_not_exist = (request, response, next) => {
    guild_id = request.params.guild_id;
    message_id = request.query.message_id;

    if (!message_id) {
        response.status(400).json({
            variables: {
                guild_id: guild_id,
                message_id: message_id || "undefined"
            },
            message: "A Variable is Undefined"
        });
        return;
    }

    query = "SELECT * FROM polls WHERE guild_id = (?) AND message_id = (?);";
    getConnection().query(query, [guild_id, message_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        if (!!rows.length) {
            const poll_error = new Error("A Poll with Message ID (" + message_id + ") For Guild (" + guild_id + ") Already Exists");
            poll_error.status = 409;
            next(poll_error);
            return;
        }

        next();
    });
}

// Retrieves all polls
router.get("/polls", check_authentication, check_admin, (request, response, next) => {
    query = "SELECT * FROM polls;";
    getConnection().query(query, (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            polls: rows,
            message: "Successfully Retrieved All Polls"
        });
        return;
    });
});

// Retrieves all polls for a specific guild.
router.get("/polls/:guild_id", check_authentication, check_admin, (request, response, next) => {
    guild_id = request.params.guild_id;

    query = "SELECT * FROM polls WHERE guild_id = (?);";
    getConnection().query(query, [guild_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            polls: rows,
            message: "Successfully Retrieved Polls for Guild (" + guild_id + ")"
        });
        return;
    });
});

// Adds a new poll
router.post("/polls/:guild_id", check_authentication, check_admin, check_if_poll_does_not_exist, (request, response, next) => {
    guild_id = request.params.guild_id;

    message_id = request.query.message_id;
    ending_time = request.query.ending_time;

    if (!message_id || !ending_time) {
        response.status(400).json({
            variables: {
                guild_id: guild_id,
                message_id: message_id || "undefined",
                ending_time: ending_time || "undefined"
            },
            message: "A Variable is Undefined"
        });
        return;
    }

    query = "INSERT INTO polls (guild_id, message_id, ending_time) VALUES (?,?,?);";
    getConnection().query(query, [guild_id, message_id, ending_time], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(201).json({
            message: "Successfully Created POLL for Guild (" + guild_id + ")"
        });
        return;
    });
});

// Deletes a poll
router.delete("/polls/:guild_id", check_authentication, check_admin, (request, response, next) => {
    guild_id = request.params.guild_id;

    message_id = request.query.message_id;

    if (!message_id) {
        response.status(400).json({
            variables: {
                guild_id: guild_id,
                message_id: message_id || "undefined"
            },
            message: "A Variable is Undefined"
        });
        return;
    }

    query = "DELETE FROM polls WHERE guild_id = (?) AND message_id = (?);";
    getConnection().query(query, [guild_id, message_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            message: "Successfully Deleted Poll from Guild (" + guild_id + ")"
        });
        return;
    });
});

module.exports = router;

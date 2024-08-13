const express = require('express');
const router = express.Router();

const getConnection = require('./modules/mysql-connection');
const check_authentication = require('../../middleware/check-auth');
const check_admin = require('../../middleware/check-admin.js');

const check_if_raffle_does_not_exist = (request, response, next) => {
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

    query = "SELECT * FROM raffles WHERE guild_id = (?) AND message_id = (?);";
    getConnection().query(query, [guild_id, message_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        if (!!rows.length) {
            const raffle_error = new Error("A Raffle with Message ID (" + message_id + ") For Guild (" + guild_id + ") Already Exists");
            raffle_error.status = 409;
            next(raffle_error);
            return;
        }

        next();
    });
}

// Retrieves all raffles.
router.get("/raffles", check_authentication, check_admin, (request, response, next) => {
    query = "SELECT * FROM raffles;";
    getConnection().query(query, (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            raffles: rows,
            message: "Successfully Retrieved All Raffles"
        });
        return;
    });
});

// Gets all raffles for a specific guild.
router.get("/raffles/:guild_id", check_authentication, check_admin, (request, response, next) => {
    guild_id = request.params.guild_id;
    query = "SELECT * FROM raffles WHERE guild_id = (?);";
    getConnection().query(query, [guild_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            raffles: rows,
            message: "Successfully Retrieved Raffles for Guild (" + guild_id + ")"
        });
        return;
    });
});

// Creates a new raffle.
router.post("/raffles/:guild_id", check_authentication, check_admin, check_if_raffle_does_not_exist, (request, response, next) => {
    guild_id = request.params.guild_id;

    message_id = request.query.message_id;
    ending_time = request.query.ending_time;
    winner_amount = request.query.winner_amount;

    if (!message_id || !ending_time || !winner_amount) {
        response.status(400).json({
            variables: {
                guild_id: guild_id,
                message_id: message_id || "undefined",
                ending_time: ending_time || "undefined",
                winner_amount: winner_amount || "undefined"
            },
            message: "A Variable is Undefined"
        });
        return;
    }

    query = "INSERT INTO raffles (guild_id, message_id, ending_time, winner_amount) VALUES (?,?,?,?);";
    getConnection().query(query, [guild_id, message_id, ending_time, winner_amount], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(201).json({
            message: "Successfully Inserted Raffle to Guild (" + guild_id + ")"
        });
        return;
    });
});

// Deletes a raffle from a guild.
router.delete("/raffles/:guild_id", check_authentication, check_admin, (request, response, next) => {
    guild_id = request.params.guild_id;

    message_id = request.query.message_id;

    if (!message_id) {
        response.status(500).json({
            variables: {
                guild_id: guild_id,
                message_id: message_id || "undefined"
            },
            message: "A Variable is Undefined"
        });
        return;
    }

    query = "DELETE FROM raffles WHERE guild_id = (?) AND message_id = (?);";
    getConnection().query(query, [guild_id, message_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            message: "Successfully Deleted Raffle from Guild (" + guild_id + ")"
        });
        return;
    });
});

module.exports = router;

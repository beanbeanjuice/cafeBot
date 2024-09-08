const express = require('express');
const router = express.Router();

const getConnection = require('../cafe/modules/mysql-connection');
const check_authentication = require('../../middleware/check-auth');
const check_admin = require('../../middleware/check-admin.js');

router.get("/guilds/ai/", check_authentication, check_admin, (request, response, next) => {
    query = "SELECT * FROM guild_ai_threads";
    getConnection().query(query, (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            threads: rows,
            message: "Successfully retrieved all AI threads."
        });
        return;
    })
});

router.get("/guilds/ai/:guild_id", check_authentication, check_admin, (request, response, next) => {
    const guild_id = request.params.guild_id;

    query = "SELECT thread_id FROM guild_ai_threads WHERE guild_id = (?);";
    getConnection().query(query, [guild_id], (error, rows, fields) => {
        if (error) {
            next(error);
            return;
        }

        if (rows.size === 0) {
            response.status(404).send(`${guild_id} does not contain an AI thread.`);
            return;
        }

        response.status(200).json({
            thread_id: rows[0],
        });
        return;
    });
});

router.post("/guilds/ai/:guild_id", check_authentication, check_admin, (request, response, next) => {
    const guild_id = request.params.guild_id;
    const thread_id = request.query.thread_id;

    if (thread_id === undefined) {
        response.status(401);
        return;
    }

    query = "INSERT INTO guild_ai_threads (guild_id, thread_id) VALUES (?,?);";
    getConnection().query(query, [guild_id, thread_id], check_authentication, check_admin, (error, rows, fields) => {
        if (error) {
            next(error);
            return;
        }

        response.status(201).send("Successfully inserted into database.");
        return;
    });
});

router.delete("/guilds/ai/:guild_id", check_authentication, check_admin, (request, response, next) => {
    const guild_id = request.params.guild_id;

    query = "DELETE FROM guild_ai_threads WHERE guild_id = (?);";
    getConnection().query(query, [guild_id], (error, rows, fields) => {
        if (error) {
            next(error);
            return;
        }

        response.status(200).send(`AI thread for ${guild_id} has been successfully deleted.`);
        return;
    })
});

module.exports = router;

const express = require('express');
const router = express.Router();

const getConnection = require('./modules/mysql-connection');
const check_authentication = require('../../middleware/check-auth');
const check_admin = require('../../middleware/check-admin.js');

// Gets all voice channel role binds.
router.get("/voice_binds", check_authentication, check_admin, (request, response, next) => {
    query = "SELECT * FROM voice_channel_role_binds;";
    getConnection().query(query, (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            voice_binds: rows,
            message: "Successfully Retrieved All Voice Channel Role Binds"
        });
        return;
    });
});

// Gets all voice binds for that guild.
router.get("/voice_binds/:guild_id", check_authentication, check_admin, (request, response, next) => {
    guild_id = request.params.guild_id;

    query = "SELECT * FROM voice_channel_role_binds WHERE guild_id = (?);";
    getConnection().query(query, [guild_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            voice_binds: rows,
            message: "Successfully Retrieved Binds for Guild (" + guild_id + ")"
        });
        return;
    });
});

// Adds a new voice bind for a guild.
router.post("/voice_binds/:guild_id", check_authentication, check_admin, (request, response, next) => {
    guild_id = request.params.guild_id;
    voice_channel_id = request.query.voice_channel_id;
    role_id = request.query.role_id;

    if (!voice_channel_id || !role_id) {
        response.status(400).json({
            variables: {
                guild_id: guild_id,
                voice_channel_id: voice_channel_id || "undefined",
                role_id: role_id || "undefined"
            },
            message: "A Variable is Undefined"
        });
        return;
    }

    check_query = "SELECT * FROM voice_channel_role_binds WHERE guild_id = (?) AND voice_channel_id = (?) AND role_id = (?);";
    getConnection().query(check_query, [guild_id, voice_channel_id, role_id], (check_error, check_rows, check_fields) => {
        if (check_error) {
            next(new Error(check_error));
            return;
        }

        if (!check_rows.length) {
            query = "INSERT INTO voice_channel_role_binds (guild_id, voice_channel_id, role_id) VALUES (?,?,?);";
            getConnection().query(query, [guild_id, voice_channel_id, role_id], (error, rows, fields) => {
                if (error) {
                    next(new Error(error));
                    return;
                }

                response.status(201).json({
                    message: "Successfully Created New Bind for Guild (" + guild_id + ")"
                });
                return;
            });
        } else {
            response.status(409).json({
                message: "That Bind for the Guild (" + guild_id + ") Already Exists"
            });
            return;
        }
    });
});

// Deletes a voice channel bind.
router.delete("/voice_binds/:guild_id", check_authentication, check_admin, (request, response, next) => {
    guild_id = request.params.guild_id;
    voice_channel_id = request.query.voice_channel_id;
    role_id = request.query.role_id;

    if (!voice_channel_id || !role_id) {
        response.status(500).json({
            variables: {
                guild_id: guild_id,
                voice_channel_id: voice_channel_id || "undefined",
                role_id: role_id || "undefined"
            },
            message: "A Variable is Undefined"
        });
        return;
    }

    query = "DELETE FROM voice_channel_role_binds WHERE guild_id = (?) AND voice_channel_id = (?) AND role_id = (?);";
    getConnection().query(query, [guild_id, voice_channel_id, role_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            message: "Successfully Removed Role Bind for Guild (" + guild_id + ")"
        });
        return;
    });
});

module.exports = router;

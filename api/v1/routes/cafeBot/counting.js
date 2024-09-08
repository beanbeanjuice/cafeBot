const express = require('express');
const router = express.Router();

const getConnection = require('./modules/mysql-connection');
const check_authentication = require('../../middleware/check-auth');
const check_admin = require('../../middleware/check-admin.js');

const check_if_guild_exists = (request, response, next) => {
    guild_id = request.params.guild_id;

    query = "SELECT * FROM counting_information WHERE guild_id = (?);";
    getConnection().query(query, [guild_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        if (!rows.length) {
            const guild_error = new Error("Counting Information for Guild (" + guild_id + ") Does Not Exist");
            guild_error.status = 404;
            next(guild_error);
            return;
        }

        next();
    });
}

const check_if_guild_does_not_exist = (request, response, next) => {
    guild_id = request.params.guild_id;

    query = "SELECT * FROM counting_information WHERE guild_id = (?);";
    getConnection().query(query, [guild_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        if (!!rows.length) {
            const guild_error = new Error("Counting Information for Guild (" + guild_id + ") Already Exist");
            guild_error.status = 409;
            next(guild_error);
            return;
        }

        next();
    });
}

// Gets a list of all guild counting information.
router.get("/counting/guilds", check_authentication, check_admin, (request, response, next) => {
    query = "SELECT * FROM counting_information;";
    getConnection().query(query, (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            guilds: rows,
            message: "Successfully Retrieved Counting Information for All Guilds"
        });
        return;
    });
});

// Gets a list of counting information for a specified guild.
router.get("/counting/guilds/:guild_id", check_authentication, check_admin, check_if_guild_exists, (request, response, next) => {
    guild_id = request.params.guild_id;

    query = "SELECT * FROM counting_information WHERE guild_id = (?);";
    getConnection().query(query, [guild_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            counting_information: rows[0],
            message: "Successfully Retrieved Counting Information for Guild (" + guild_id + ")"
        });
        return;
    });
});

// Updates counting information for a specified guild.
router.patch("/counting/guilds/:guild_id", check_authentication, check_admin, check_if_guild_exists, (request, response, next) => {
    guild_id = request.params.guild_id;

    highest_number = request.query.highest_number;
    last_number = request.query.last_number;
    last_user_id = request.query.last_user_id;
    failure_role_id = request.query.failure_role_id;

    if (!highest_number || !last_number || !last_user_id || !failure_role_id) {
        response.status(500).json({
            variables: {
                guild_id: guild_id,
                highest_number: highest_number || "undefined",
                last_number: last_number || "undefined",
                last_user_id: last_user_id || "undefined",
                failure_role_id: failure_role_id || "undefined"
            },
            message: "A Variable is Undefined"
        });
        return;
    }

    query = "UPDATE counting_information SET highest_number = (?), last_number = (?), last_user_id = (?), failure_role_id = (?) WHERE guild_id = (?);";
    getConnection().query(query, [highest_number, last_number, last_user_id, failure_role_id, guild_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            message: "Successfully Updated Counting Information for Guild (" + guild_id + ")"
        });
        return;
    });
});

// Creates new counting information for a guild.
router.post("/counting/guilds/:guild_id", check_authentication, check_admin, check_if_guild_does_not_exist, (request, response, next) => {
    guild_id = request.params.guild_id;

    query = "INSERT INTO counting_information (guild_id) VALUES (?);";
    getConnection().query(query, [guild_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(201).json({
            message: "Successfully Created new Counting Information for Guild (" + guild_id + ")"
        });
        return;
    });
});

// Deletes counting information for a guild.
router.delete("/counting/guilds/:guild_id", check_authentication, check_admin, (request, response, next) => {
    guild_id = request.params.guild_id;

    query = "DELETE FROM counting_information WHERE guild_id = (?);";
    getConnection().query(query, [guild_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            mesage: "Successfully Deleted Counting Information for Guild (" + guild_id + ")"
        });
        return;
    });
});

module.exports = router;

const express = require('express');
const router = express.Router();

const getConnection = require('./modules/mysql-connection');
const check_authentication = require('../../middleware/check-auth');
const check_admin = require('../../middleware/check-admin.js');

// Checks if a guild's welcome exists
const check_if_guild_exists = (request, response, next) => {
    guild_id = request.params.guild_id;
    query = "SELECT * FROM welcome_information WHERE guild_id = (?);";

    getConnection().query(query, [guild_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        if (!rows.length) {
            const guild_error = new Error("Guild (" + guild_id + ") Does Not Exist");
            guild_error.status = 404;
            next(guild_error);
            return;
        }

        next();
    });
}

const check_if_guild_does_not_exist = (request, response, next) => {
    guild_id = request.params.guild_id;
    query = "SELECT * FROM welcome_information WHERE guild_id = (?);";

    getConnection().query(query, [guild_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        if (!!rows.length) {
            const guild_error = new Error("Guild (" + guild_id + ") Already Exists");
            guild_error.status = 409;
            next(guild_error);
            return;
        }

        next();
    });
}

// Gets all welcome information.
router.get("/welcomes", check_authentication, check_admin, (request, response, next) => {
    query = "SELECT * FROM welcome_information;";
    getConnection().query(query, (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            welcomes: rows,
            message: "Successfully Retrieved All Welcome Information"
        });
        return;
    });
});

// Gets a specific guild's welcome information.
router.get("/welcomes/:guild_id", check_authentication, check_admin, check_if_guild_exists, (request, response, next) => {
    guild_id = request.params.guild_id;
    query = "SELECT * FROM welcome_information WHERE guild_id = (?);";
    getConnection().query(query, [guild_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            welcome: rows[0],
            message: "Successfully Retrieved Welcome Information for Guild (" + guild_id + ")"
        });
        return;
    });
});

// Updates a specific guild's welcome information
router.patch("/welcomes/:guild_id", check_authentication, check_admin, check_if_guild_exists, (request, response, next) => {
    guild_id = request.params.guild_id;
    description = request.query.description || null;
    thumbnail_url = request.query.thumbnail_url || null;
    image_url = request.query.image_url || null;
    message = request.query.message || null;

    query = "UPDATE welcome_information SET description = (?), thumbnail_url = (?), image_url = (?), message = (?) WHERE guild_id = (?);";
    getConnection().query(query, [description, thumbnail_url, image_url, message, guild_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            message: "Successfully Updated Welcome Information for Guild (" + guild_id + ")"
        });
        return;
    });

});

// Adds a new guild welcome
router.post("/welcomes/:guild_id", check_authentication, check_admin, check_if_guild_does_not_exist, (request, response, next) => {
    guild_id = request.params.guild_id;

    description = request.query.description || null;
    thumbnail_url = request.query.thumbnail_url || null;
    image_url = request.query.image_url || null;
    message = request.query.message || null;

    query = "INSERT INTO welcome_information (guild_id, description, thumbnail_url, image_url, message) VALUES (?,?,?,?,?);";
    getConnection().query(query, [guild_id, description, thumbnail_url, image_url, message], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(201).json({
            message: "Successfully Created Welcome Information for Guild (" + guild_id + ")"
        });
        return;
    });
});

// Deletes a guild's welcome information
router.delete("/welcomes/:guild_id", check_authentication, check_admin, (request, response, next) => {
    guild_id = request.params.guild_id;

    query = "DELETE FROM welcome_information WHERE guild_id = (?);";
    getConnection().query(query, [guild_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            message: "Successfully Removed Welcome Information for Guild (" + guild_id + ")"
        });
        return;
    });
});

module.exports = router;

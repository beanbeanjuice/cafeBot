const express = require('express');
const router = express.Router();

const check_authentication = require('../../middleware/check-auth');
const getConnection = require('./modules/mysql-connection');

const check_if_user_exists = (request, response, next) => {
    const user_id = request.user_data.user_id;

    const query = "SELECT * FROM user_settings WHERE user_id = (?);";
    const connection = getConnection();
    connection.query(query, [user_id], (error, rows, fields) => {
        if (error) {
            response.status(500).json({
                message: error
            });
            return;
        }

        if (rows.length < 1) {
            response.status(500).json({
                message: "Settings for User (" + user_id + ") Do Not Exist"
            });
            return;
        }

        next(); // Continue
    });
};

// Gets a list of all settings.
router.get("/user_settings", check_authentication, (request, response) => {
    const connection = getConnection();
    const query = "SELECT * FROM user_settings;";
    connection.query(query, (error, rows, fields) => {
        if (error) {
            console.log("Failed to Query User Settings: " + error);
            response.status(500).json({
                message: error
            });
            return;
        }
        response.status(200).json({
            settings: rows,
            message: "Successfully Retrieved All User Settings"
        });
    });
});

// Gets a User's Settings
router.get("/user_setting", check_authentication, check_if_user_exists, (request, response) => {
    const user_id = request.user_data.user_id;
    const connection = getConnection();
    const query = "SELECT * FROM user_settings WHERE user_id = (?);";
    connection.query(query, [user_id], (error, rows, fields) => {
        if (error) {
            response.status(500).json({
                message: error
            });
            return;
        }

        response.status(200).json({
            settings: rows,
            message: "Successfully Retrived Settings for User (" + user_id + ")"
        });
    });
});

// Updates a specific user's settings.
router.patch("/user_setting/update", check_authentication, check_if_user_exists, (request, response) => {

    const user_id = request.user_data.user_id;
    const time_limit = request.query.time_limit;
    const ignore_time_limit = request.query.ignore_time_limit;

    if (ignore_time_limit != 0 && ignore_time_limit != 1) {
        response.status(500).json({
            message: "The variable, ignore_time_limit, can only be between 0 and 1"
        })
        return;
    }

    const connection = getConnection();
    const query = "UPDATE user_settings SET time_limit = (?), ignore_time_limit = (?) WHERE user_id = (?);";
    connection.query(query, [time_limit, ignore_time_limit, user_id], (error, rows, fields) => {
        if (error) {
            console.log("Error Updating User (" + user_id + ") Settings: " + error);
            response.status(500).json({
                message: error
            });
            return;
        }

        console.log("Updated User (" + user_id + ") Settings");
        response.status(200).json({
            message: "Successfully Updated User (" + user_id + ") Settings"
        });
    });
});


// Creating User Settings
router.post("/user_setting/create", check_authentication, (request, response) => {
    const user_id = request.user_data.user_id;

    // Checking If Settings Exist
    const check_query = "SELECT * FROM user_settings WHERE user_id = (?);";
    const check_connection = getConnection();
    check_connection.query(check_query, [user_id], (error, rows, fields) => {
        if (error) {
            response.status(500).json({
                message: "Error Checking if User (" + user_id + ") Settings Exists: " + error
            });
            return;
        }

        if (rows.length > 0) {
            response.status(500).json({
                message: "Error Creating User Settings: User's Settings Already Exists"
            });
            return;
        }

        const query = "INSERT INTO user_settings (user_id) VALUES (?);";
        const connection = getConnection();
        connection.query(query, [user_id], (error, rows, fields) => {
            if (error) {
                response.status(500).json({
                    message: "Error Creating User Settings: " + error
                });
                return;
            }

            response.status(201).json({
                message: "Created User (" + user_id + ") Settings with Defaults"
            });
        })
    });
});

// Deleting User Settings
router.delete("/user_setting/remove", check_authentication, check_if_user_exists, (request, response) => {
    const user_id = request.user_data.user_id;

    const query = "DELETE FROM user_settings WHERE user_id = (?);";
    const connection = getConnection();
    connection.query(query, [user_id], (error, rows, fields) => {
        if (error) {
            response.status(500).json({
                message: "Error Removing User (" + user_id + ") Settings: " + error
            });
            return;
        }

        response.status(200).json({
            message: "Successfully Removed User (" + user_id + ") Settings"
        });
    });
});

module.exports = router;
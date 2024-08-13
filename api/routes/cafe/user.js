const express = require('express');
const router = express.Router();
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');

const getConnection = require('../cafe/modules/mysql-connection');
const check_authentication = require('../../middleware/check-auth');
const check_admin = require('../../middleware/check-admin.js');
const checkType = require('../../middleware/check-type');

const TOKEN = checkType.TOKEN;

// Gets a list of all users.
router.get("/users", check_authentication, check_admin, (request, response, next) => {
    query = "SELECT * FROM users;";
    getConnection().query(query, (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            users: rows,
            message: "Successfully Retrieved All Users"
        });
        return;
    });
});

const check_if_user_exists = (request, response, next) => {
    username = request.params.username;

    if (!username) {
        response.status(500).json({
            variables: {
                username: username || "undefined"
            },
            message: "A Variable is Undefined"
        });
        return;
    }

    query = "SELECT * FROM users WHERE username = (?);";
    getConnection().query(query, [username], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        if (!rows.length) {
            const auth_error = new Error("Authorization Failed");
            auth_error.status = 401;
            next(auth_error);
            return;
        }

        next();
    });
}

// Login to an account
router.post("/user/login", (request, response, next) => {
    const username = request.query.username;

    const check_query = "SELECT * FROM users WHERE username = (?);";
    const check_connection = getConnection();
    check_connection.query(check_query, [username], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        // Checking If Users Exist
        if (rows.length < 1) {
            response.status(401).json({
                message: "Authorization Failed" // Used to Prevent Brute Forcing
            });
            return;
        }

        const hashed_password = rows[0].password;
        console.info("Checking Token...");
        bcrypt.compare(request.query.password, hashed_password, (error, result) => {
            if (error) {
                response.status(401).json({
                    message: "Authorization Failed"
                });
                console.error("Error: " + error.message);
                return;
            }

            console.log(TOKEN);

            if (result) {
                const api_key = jwt.sign({
                    user_id: rows[0].user_id,
                    username: rows[0].username,
                    user_type: rows[0].user_type
                },
                    TOKEN, // TOKEN
                    {
                        expiresIn: "1h"
                    });

                return response.status(200).json({
                    message: "Authorization Successful",
                    api_key: api_key,
                    expires_in: "1 Hour"
                });
            }

            response.status(401).json({
                message: "Authorization Failed"
            });
        });


    });
});

// Creates a user.
router.post('/user/signup', (request, response) => {

    // HASHING the password
    bcrypt.hash(request.query.password, 10, (error, hashed_password) => {
        if (error) {
            return response.status(500).json({
                message: error
            });
        } else {

            const username = request.query.username;
            const password = hashed_password;

            if (!username || !password) {
                response.status(500).json({
                    variables: {
                        username: username || "undefined",
                        password: password || "undefined"
                    },
                    message: "A Variable is Undefined"
                });
                return;
            }

            // Checking if the User Exists Already
            const check_query = "SELECT * FROM users WHERE username = (?);";
            const check_connection = getConnection();
            check_connection.query(check_query, [username], (error, rows, fields) => {

                if (error) {
                    response.status(500).json({
                        message: "Error Checking if User Exists"
                    })
                    return;
                }

                if (rows.length > 0) {
                    response.status(401).json({
                        message: "Authorization Failed"
                    });
                    return;
                } else {
                    const query = "INSERT INTO users (username, password) VALUES (?,?);";
                    const connection = getConnection();
                    connection.query(query, [username, password], (error, rows, fields) => {
                        if (error) {
                            console.log("Failed to Insert New User: " + error);
                            response.status(500).json({
                                message: error
                            });
                            return;
                        }

                        console.log("Inserted New User Successfully with ID: " + rows.insertId);

                        response.status(201).json({
                            message: "Successfully Created User (" + rows.insertId + ")"
                        });
                    });
                    return;
                }
            });

        }
    });
});

// Gets a specified user.
router.get("/user/:username", check_authentication, check_admin, check_if_user_exists, (request, response) => {
    const username = request.params.username;

    query = "SELECT * FROM users WHERE username = (?);";
    getConnection().query(query, [username], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            user: rows[0],
            message: "Successfully Retrieved User"
        });
        return;
    });
});

// Deleting User
router.delete("/user/:username", check_authentication, check_admin, (request, response) => {

    const username = request.params.username;

    console.log("Deleting User (" + username + ")");

    const query = "DELETE FROM users WHERE username = (?);";
    const connection = getConnection();
    connection.query(query, [username], (error, rows, fields) => {
        if (error) {
            response.status(500).json({
                message: "Error Removing User (" + username + "): " + error
            });
            return;
        }

        response.status(200).json({
            message: "Successfully Deleted User (" + username + ")"
        });
    });
});

module.exports = router;

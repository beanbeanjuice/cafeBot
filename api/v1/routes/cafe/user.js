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
    const query = "SELECT * FROM users;";
    getConnection().query(query, (error, rows, fields) => {
        if (error) {
            next(error);
            return;
        }

        response.status(200).json({
            message: "Successfully retrieved users.",
            users: rows
        });
    });
});

const check_if_user_exists = (request, response, next) => {
    const username = request.params.username;

    if (username === undefined) {
        response.status(500).json({
            variables: {
                username: "undefined"
            },
            message: "A variable is undefined."
        });
        return;
    }

    const query = "SELECT * FROM users WHERE username = (?);";
    getConnection().query(query, [username], (error, rows, fields) => {
        if (error) {
            next(error);
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
            next(error);
            return;
        }

        // Checking If Users Exist
        if (rows.length < 1) {
            response.status(401).json({
                message: "Authorization failed."  // Used to Prevent Brute Forcing
            });
            return;
        }

        const hashed_password = rows[0].password;
        console.info("Checking Token...");
        bcrypt.compare(request.query.password, hashed_password, (error, result) => {
            if (error) {
                response.status(401).json({
                    message: "Authorization failed."
                });
                console.error("Error: " + error.message);
                return;
            }

            if (result) {
                const access_token = jwt.sign({
                    user_id: rows[0].user_id,
                    username: rows[0].username,
                    user_type: rows[0].user_type
                },
                    TOKEN, // TOKEN
                    {
                        expiresIn: "1h"
                    });

                return response.status(200).json({
                    message: "Successfully authenticated.",
                    access_token: access_token,
                    expires_in: "3600"
                });
            }

            response.status(401).json({ message: "Authorization failed." });
        });


    });
});

// Creates a user.
router.post('/user/signup', (request, response, next) => {

    // HASHING the password
    bcrypt.hash(request.query.password, 10, (error, hashed_password) => {
        if (error) {
            next(error);
            return;
        }

        const username = request.query.username;
        const password = hashed_password;

        if (!username || !password) {
            response.status(500).json({
                variables: {
                    username: username || "undefined",
                    password: password || "undefined"
                },
                message: "A variable is undefined."
            });
            return;
        }

        // Checking if the User Exists Already
        const check_query = "SELECT * FROM users WHERE username = (?);";
        getConnection().query(check_query, [username], (error, rows, fields) => {
            if (error) {
                next(error);
                return;
            }

            if (rows.length > 0) {
                response.status(401).json({ message: "Authorization failed." });
                return;
            }

            const query = "INSERT INTO users (username, password) VALUES (?,?);";
            getConnection().query(query, [username, password], (error, rows, fields) => {
                if (error) {
                    next(error);
                    return;
                }

                response.status(201).json({ message: `Successfully created user (${rows.insertId}).` })
            });
        });


    });
});

// Gets a specified user.
router.get("/user/:username", check_authentication, check_admin, check_if_user_exists, (request, response, next) => {
    const username = request.params.username;

    const query = "SELECT * FROM users WHERE username = (?);";
    getConnection().query(query, [username], (error, rows, fields) => {
        if (error) {
            next(error);
            return;
        }

        response.status(200).json({
            message: "Successfully retrieved user.",
            user: rows[0]
        });
    });
});

// Deleting User
router.delete("/user/:username", check_authentication, check_admin, (request, response, next) => {
    const username = request.params.username;

    const query = "DELETE FROM users WHERE username = (?);";
    getConnection().query(query, [username], (error, rows, fields) => {
        if (error) {
            next(error);
            return;
        }

        response.status(200).json({ message: `Successfully deleted user (${username}).` });
    });
});

module.exports = router;

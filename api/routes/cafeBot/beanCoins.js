const express = require('express');
const router = express.Router();

const getConnection = require('./modules/mysql-connection');
const check_authentication = require('../../middleware/check-auth');
const check_admin = require('../../middleware/check-admin.js');

const check_if_user_exists = (request, response, next) => {
    user_id = request.params.user_id;

    query = "SELECT * FROM beancoin_donation_users WHERE user_id = (?);";
    getConnection().query(query, [user_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        if (!rows.length) {
            const user_error = new Error("Donation User (" + user_id + ") Does Not Exist");
            user_error.status = 404;
            next(user_error);
            return;
        }

        next();
    });
}

const check_if_user_does_not_exist = (request, response, next) => {
    user_id = request.params.user_id;

    query = "SELECT * FROM beancoin_donation_users WHERE user_id = (?);";
    getConnection().query(query, [user_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        if (!!rows.length) {
            const user_error = new Error("User (" + user_id + ") Already Exists");
            user_error.status = 409;
            next(user_error);
            return;
        }

        next();
    });
}

// Gets beanCoin donation users.
router.get("/beanCoin/donation_users", check_authentication, check_admin, (request, response, next) => {
    query = "SELECT * FROM beancoin_donation_users;";
    connection = getConnection();
    connection.query(query, (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            users: rows,
            message: "Successfully Retrieved List of Active Donation Users"
        });
        return;
    });
});

// Gets a specific donation user.
router.get("/beanCoin/donation_users/:user_id", check_authentication, check_admin, check_if_user_exists, (request, response, next) => {

    user_id = request.params.user_id;

    query = "SELECT * FROM beancoin_donation_users WHERE user_id = (?);";
    getConnection().query(query, [user_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            user: rows[0],
            message: "Successfully Retrieved User (" + user_id + ")"
        });
        return;
    });
});

// Add a new donation user
router.post("/beanCoin/donation_users/:user_id", check_authentication, check_admin, check_if_user_does_not_exist, (request, response, next) => {

    user_id = request.params.user_id;
    time_stamp = request.query.time_stamp;

    if (!time_stamp) {
        response.status(400).json({
            variables: {
                user_id: user_id,
                time_stamp: time_stamp || "undefined"
            },
            message: "A Variable is Undefined"
        });
        return;
    }

    query = "INSERT INTO beancoin_donation_users (user_id, time_until_next_donation) VALUES (?,?);";
    getConnection().query(query, [user_id, time_stamp], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(201).json({
            message: "Successfully Created User (" + user_id + ")"
        });
        return;
    });

});

// Deletes a beanCoin donation user
router.delete("/beanCoin/donation_users/:user_id", check_authentication, check_admin, (request, response, next) => {
    user_id = request.params.user_id;

    query = "DELETE FROM beancoin_donation_users WHERE user_id = (?);";
    getConnection().query(query, [user_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            message: "Successfully Removed User (" + user_id + ")"
        });
        return;
    });

});

module.exports = router;

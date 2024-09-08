const express = require('express');
const router = express.Router();

const getConnection = require('./modules/mysql-connection');
const check_authentication = require('../../middleware/check-auth');
const check_admin = require('../../middleware/check-admin.js');

// const check_if_user_exists = (request, response, next) => {
//     const user_id = request.params.user_id;
//
//     const query = "SELECT * FROM beancoin_donation_users WHERE user_id = (?);";
//     getConnection().query(query, [user_id], (error, rows, fields) => {
//         if (error) {
//             next(error);
//             return;
//         }
//
//         if (!rows.length) {
//             const user_error = new Error("Donation User (" + user_id + ") Does Not Exist");
//             user_error.status = 404;
//             next(user_error);
//             return;
//         }
//
//         next();
//     });
// }
//
// const check_if_user_does_not_exist = (request, response, next) => {
//     user_id = request.params.user_id;
//
//     query = "SELECT * FROM beancoin_donation_users WHERE user_id = (?);";
//     getConnection().query(query, [user_id], (error, rows, fields) => {
//         if (error) {
//             next(new Error(error));
//             return;
//         }
//
//         if (!!rows.length) {
//             const user_error = new Error("User (" + user_id + ") Already Exists");
//             user_error.status = 409;
//             next(user_error);
//             return;
//         }
//
//         next();
//     });
// }

// Gets beanCoin donation users.
router.get("/beanCoin/donation_users", check_authentication, check_admin, (request, response, next) => {
    const query = "SELECT * FROM beancoin_donation_users;";
    getConnection().query(query, (error, rows, fields) => {
        if (error) {
            next(error);
            return;
        }

        response.status(200).json({
            message: "Successfully retrieved a list of the active donation users.",
            users: rows
        });
    });
});

// Gets a specific user's time until next donation.
router.get("/beanCoin/donation_users/:user_id", check_authentication, check_admin, (request, response, next) => {
    const user_id = request.params.user_id;

    const query = "SELECT time_until_next_donation FROM beancoin_donation_users WHERE user_id = (?);";
    getConnection().query(query, [user_id], (error, rows, fields) => {
        if (error) {
            next(error);
            return;
        }

        if (!rows) {
            let doesNotExistError = new Error(`${user_id} does not have an active wait time.`);
            doesNotExistError.status = 404;
            next(doesNotExistError);
            return;
        }

        response.status(200).json({
            message: `Successfully retrieved time until next donation for user (${user_id}).`,
            time_until_next_donation: rows[0]
        });
    });
});

// Add a new donation user
router.post("/beanCoin/donation_users/:user_id", check_authentication, check_admin, (request, response, next) => {
    const user_id = request.params.user_id;
    const time_stamp = request.query.time_stamp;

    if (!time_stamp) {
        response.status(400).json({
            variables: {
                time_stamp: time_stamp || "undefined"
            },
            message: "A variable is undefined."
        });
        return;
    }

    const query = "INSERT INTO beancoin_donation_users (user_id, time_until_next_donation) VALUES (?,?);";
    getConnection().query(query, [user_id, time_stamp], (error, rows, fields) => {
        if (error) {
            next(error);
            return;
        }

        response.status(201).json({ message: `Successfully created user (${user_id}).` });
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

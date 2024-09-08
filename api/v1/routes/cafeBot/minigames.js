const express = require('express');
const router = express.Router();

const getConnection = require('./modules/mysql-connection');
const check_authentication = require('../../middleware/check-auth');
const check_admin = require('../../middleware/check-admin.js');

const check_if_user_exists = (request, response, next) => {
    user_id = request.params.user_id;

    query = "SELECT * FROM minigames_win_streaks WHERE user_id = (?);";
    getConnection().query(query, [user_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        if (!rows.length) {
            user_error = new Error("User (" + user_id + ") Win Streaks Does Not Exist");
            user_error.status = 404;
            next(user_error);
            return;
        }

        next();
    });
}

const check_if_user_does_not_exist = (request, response, next) => {
    user_id = request.params.user_id;

    query = "SELECT * FROM minigames_win_streaks WHERE user_id = (?);";

    getConnection().query(query, [user_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        if (!!rows.length) {
            user_error = new Error("User (" + user_id + ") Win Streaks Already Exists");
            user_error.status = 409;
            next(user_error);
            return;
        }

        next();
    });
}

// Retrieves all minigame win streaks.
router.get("/minigames/win_streaks", check_authentication, check_admin, (request, response, next) => {
    query = "SELECT * FROM minigames_win_streaks;";
    getConnection().query(query, (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            win_streaks: rows,
            message: "Successfully Retrieved Minigame Win Streaks"
        })
    });
});

// Retrieves minigame streaks for a specified user.
router.get("/minigames/win_streaks/:user_id", check_authentication, check_if_user_exists, (request, response, next) => {
    user_id = request.params.user_id;
    query = "SELECT * FROM minigames_win_streaks WHERE user_id = (?);";
    getConnection().query(query, [user_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            win_streaks: rows[0],
            message: "Successfully Retrieved Minigame Win Streaks for User (" + user_id + ")"
        });
        return;
    });
});

// Updates the minigame winstreaks for a specified user.
router.patch("/minigames/win_streaks/:user_id", check_authentication, check_admin, check_if_user_exists, (request, response, next) => {
    user_id = request.params.user_id;
    // tic_tac_toe_streak = request.query.tic_tac_toe_streak;
    // connect_four_streak = request.query.connect_four_streak;

    type = request.query.type;
    value = request.query.value;

    var valid_variables = {
        tic_tac_toe: "Integer",
        connect_four: "Integer"
    };

    if (!type || !valid_variables[type]) {
        response.status(400).json({
            valid_variables,
            message: "A Valid Type is Missing"
        });
        return;
    }

    // Retrieves the value of the key, "type"
    value_type = Object.getOwnPropertyDescriptor(valid_variables, type).value;

    if (!value) {
        response.status(400).json({
            variables_required: {
                type: type,
                value_type: value_type
            },
            message: "A Value Is Required",
            example: "If the type is a Timestamp, then the value would be 2021-07-07 13:46:36."
        });
        return;
    }

    // if (!tic_tac_toe_streak || !connect_four_streak) {
    //     response.status(500).json({
    //         variables: {
    //             user_id: user_id,
    //             tic_tac_toe_streak: tic_tac_toe_streak || "undefined",
    //             connect_four_streak: connect_four_streak || "undefined"
    //         },
    //         message: "A Variable is Undefined"
    //     });
    //     return;
    // }

    query = "UPDATE minigames_win_streaks SET ?? = (?) WHERE user_id = (?);";
    getConnection().query(query, [type, value, user_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            message: "Successfully Updated Win Streaks for User (" + user_id + ")"
        });
        return;
    });
});

// Creating a new win streak for a specified user.
router.post("/minigames/win_streaks/:user_id", check_authentication, check_admin, check_if_user_does_not_exist, (request, response, next) => {
    user_id = request.params.user_id;

    query = "INSERT INTO minigames_win_streaks (user_id) VALUES (?);";
    getConnection().query(query, [user_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(201).json({
            message: "Successfully Created New Win Streak for User (" + user_id + ")"
        });
        return;
    });
});

// Deletes a win streak for a specified user.
router.delete("/minigames/win_streaks/:user_id", check_authentication, check_admin, (request, response, next) => {
    user_id = request.params.user_id;

    query = "DELETE FROM minigames_win_streaks WHERE user_id = (?);";
    getConnection().query(query, [user_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            message: "Successfully Deleted User (" + user_id + ")"
        });
        return;
    });
});

module.exports = router;

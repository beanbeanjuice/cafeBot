const express = require('express');
const router = express.Router();

const getConnection = require('./modules/mysql-connection');
const check_authentication = require('../../middleware/check-auth');
const check_admin = require('../../middleware/check-admin.js');

const check_if_user_exists = (request, response, next) => {
    user_id = request.params.user_id;

    query = "SELECT * FROM cafe_information WHERE user_id = (?);";
    getConnection().query(query, [user_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        if (!rows.length) {
            const user_error = new Error("User (" + user_id + ") Does Not Exist");
            user_error.status = 404;
            next(user_error);
            return;
        }

        next();
    });
}

const check_if_user_does_not_exist = (request, response, next) => {
    user_id = request.params.user_id;

    query = "SELECT * FROM cafe_information WHERE user_id = (?);";
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

// Gets a list of all cafe user information.
router.get("/cafe/users", check_authentication, check_admin, (request, response, next) => {
    query = "SELECT * FROM cafe_information;";
    connection = getConnection();
    connection.query(query, (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            users: rows,
            message: "Successfully Retrieved Cafe Users"
        });
        return;
    });
});

// Gets a list of all information for a specific cafe user
router.get("/cafe/users/:user_id", check_authentication, check_admin, check_if_user_exists, (request, response, next) => {
    user_id = request.params.user_id;

    query = "SELECT * FROM cafe_information WHERE user_id = (?);";
    getConnection().query(query, [user_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            cafe_user: rows[0],
            message: "Successfully Retrieved Cafe User (" + user_id + ")"
        });
        return;
    });
});

// Updates a specific cafe user.
router.patch("/cafe/users/:user_id", check_authentication, check_admin, check_if_user_exists, (request, response, next) => {
    user_id = request.params.user_id;
    
    type = request.query.type;
    value = request.query.value;

    var valid_variables = {
        bean_coins: "Double",
        last_serving_time: "SQL Timestamp",
        orders_bought: "Integer",
        orders_received: "Integer"
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

    if (type === "last_serving_time") {
        if (value === "null") {
            value = null;
        }
    }

    query = "UPDATE cafe_information SET ?? = (?) WHERE user_id = (?);";
    getConnection().query(query, [type, value, user_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            message: "Successfully Updated Cafe User (" + user_id + ")"
        });
        return;
    });
});

// Creates a new cafe user.
router.post("/cafe/users/:user_id", check_authentication, check_admin, check_if_user_does_not_exist, (request, response, next) => {
    user_id = request.params.user_id;

    query = "INSERT INTO cafe_information (user_id) VALUES (?);";
    getConnection().query(query, [user_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(201).json({
            message: "Successfully Created New Cafe User (" + user_id + ")"
        });
        return;
    });
});

// Deletes a cafe user.
router.delete("/cafe/users/:user_id", check_authentication, check_admin, (request, response, next) => {
    user_id = request.params.user_id;

    query = "DELETE FROM cafe_information WHERE user_id = (?);";
    getConnection().query(query, [user_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            message: "Successfully Deleted Cafe User (" + user_id + ")"
        });
        return;
    });
});

module.exports = router;

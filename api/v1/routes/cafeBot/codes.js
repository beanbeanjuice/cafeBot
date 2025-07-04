const express = require('express');
const router = express.Router();

const getConnection = require('./modules/mysql-connection');
const check_authentication = require('../../middleware/check-auth');
const check_admin = require('../../middleware/check-admin.js');

const check_if_user_exists = (request, response, next) => {
    user_id = request.params.user_id;

    query = "SELECT * FROM generated_codes WHERE user_id = (?);";
    getConnection().query(query, [user_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        if (!rows.length) {
            const user_error = new Error("Generated Code for User (" + user_id + ") Does Not Exist");
            user_error.status = 404;
            next(user_error);
            return;
        }

        next();
    });
}

const check_if_user_does_not_exist = (request, response, next) => {
    user_id = request.params.user_id;

    query = "SELECT * FROM generated_codes WHERE user_id = (?);";
    getConnection().query(query, [user_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        if (!!rows.length) {
            const user_error = new Error("Generated Code for User (" + user_id + ") Already Exists");
            user_error.status = 409;
            next(user_error);
            return;
        }

        next();
    });
}

// Gets all generated codes.
router.get("/codes", check_authentication, check_admin, (request, response, next) => {
    query = "SELECT * FROM generated_codes;";
    getConnection().query(query, (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            users: rows,
            message: "Successfully Retrieved the Generated Codes for All Users"
        });
        return;
    });
});

// Gets a user's code.
router.get("/codes/:user_id", check_authentication, check_admin, check_if_user_exists, (request, response, next) => {
    user_id = request.params.user_id;

    query = "SELECT * FROM generated_codes WHERE user_id = (?);";
    getConnection().query(query, [user_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            generated_code: rows[0].generated_code,
            message: "Successfully Retrieved User (" + user_id + ") Code"
        });
        return;
    });
});

// Updates an existing user's code.
router.patch("/codes/:user_id", check_authentication, check_admin, check_if_user_exists, (request, response, next) => {
    user_id = request.params.user_id;
    generated_code = request.query.generated_code;

    if (!generated_code) {
        response.status(400).json({
            variables: {
                user_id: user_id,
                generated_code: generated_code || "undefined"
            },
            message: "A Variable is Undefined"
        });
        return;
    }

    query = "UPDATE generated_codes SET generated_code = (?) WHERE user_id = (?);";
    getConnection().query(query, [generated_code, user_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            message: "Successfully Updated Existing Code for User (" + user_id + ")"
        });
        return;
    });
});

// Creates a new user code.
router.post("/codes/:user_id", check_authentication, check_admin, check_if_user_does_not_exist, (request, response, next) => {
    user_id = request.params.user_id;
    generated_code = request.query.generated_code;

    if (!generated_code) {
        response.status(500).json({
            variables: {
                user_id: user_id,
                generated_code: generated_code || "undefined"
            },
            message: "A Variable is Undefined"
        });
        return;
    }

    query = "INSERT INTO generated_codes (user_id, generated_code) VALUES (?,?);";
    getConnection().query(query, [user_id, generated_code], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(201).json({
            message: "Successfully Generated Code for User (" + user_id + ")"
        });
        return;
    });
});

// Deletes a user code.
router.delete("/codes/:user_id", check_authentication, check_admin, (request, response, next) => {
    user_id = request.params.user_id;

    query = "DELETE FROM generated_codes WHERE user_id = (?);";
    getConnection().query(query, [user_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            message: "Successfully Deleted Generated Code for User (" + user_id + ")"
        });
        return;
    });
});

module.exports = router;

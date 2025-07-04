const express = require('express');
const router = express.Router();

const getConnection = require('./modules/mysql-connection');
const check_authentication = require('../../middleware/check-auth');
const check_admin = require('../../middleware/check-admin.js');

const check_if_sender_exists = (request, response, next) => {
    user_id = request.params.user_id;
    query = "SELECT * FROM interaction_senders WHERE user_id = (?);";
    getConnection().query(query, [user_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        if (!rows.length) {
            const sender_error = new Error("User (" + user_id + ") Does Not Exist");
            sender_error.status = 404;
            next(sender_error);
            return;
        }

        next();
    });
}

const check_if_sender_does_not_exist = (request, response, next) => {
    user_id = request.params.user_id;
    query = "SELECT * FROM interaction_senders WHERE user_id = (?);";
    getConnection().query(query, [user_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        if (!!rows.length) {
            const sender_error = new Error("User (" + user_id + ") Already Exists");
            sender_error.status = 409;
            next(sender_error);
            return;
        }

        next();
    });
}

const check_if_receiver_exists = (request, response, next) => {
    user_id = request.params.user_id;
    query = "SELECT * FROM interaction_receivers WHERE user_id = (?);";
    getConnection().query(query, [user_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        if (!rows.length) {
            const receiver_error = new Error("User (" + user_id + ") Does Not Exist");
            receiver_error.status = 404;
            next(receiver_error);
            return;
        }

        next();
    });
}

const check_if_receiver_does_not_exist = (request, response, next) => {
    user_id = request.params.user_id;
    query = "SELECT * FROM interaction_receivers WHERE user_id = (?);";
    getConnection().query(query, [user_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        if (!!rows.length) {
            const receiver_error = new Error("User (" + user_id + ") Already Exists");
            receiver_error.status = 409;
            next(receiver_error);
            return;
        }

        next();
    });
}

// Gets all interaction senders.
router.get("/interactions/senders", check_authentication, check_admin, (request, response, next) => {
    query = "SELECT * FROM interaction_senders;";
    getConnection().query(query, (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            interactions_sent: rows,
            message: "Successfully Retrieved Interaction Senders"
        });
        return;
    });
});

// Gets all interactions sent from a specified user.
router.get("/interactions/senders/:user_id", check_authentication, check_if_sender_exists, (request, response, next) => {
    user_id = request.params.user_id;

    type = request.query.type;

    query = "SELECT * FROM interaction_senders WHERE user_id = (?);";
    getConnection().query(query, [user_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        if (!type) {
            response.status(200).json({
                interactions_sent: rows[0],
                message: "Successfully Retrieved Interactions Sent for User (" + user_id + ")"
            });
            return;
        } else {

            var type_amount;
            type += "_amount"

            if (!rows[0][type] === undefined) {
                type_amount = type + " Does Not Exist";
            } else {
                type_amount = rows[0][type];
            }

            response.status(200).json({
                [type]: type_amount,
                message: "Successfully Retrieved " + type + " Interactions Sent for User (" + user_id + ")"
            });
            return;
        }
    });
});

// Updates interactions for a user.
router.patch("/interactions/senders/:user_id", check_authentication, check_admin, check_if_sender_exists, (request, response, next) => {
    user_id = request.params.user_id;
    type = request.query.type;
    value = request.query.value;

    if (!type || !value) {
        response.status(500).json({
            variables: {
                user_id: user_id,
                type: type || "undefined",
                value: value || "undefined"
            },
            message: "A Variable is Undefined"
        });
        return;
    }

    type += "_amount"

    query = "UPDATE interaction_senders SET ?? = (?) WHERE user_id = (?);";
    getConnection().query(query, [type, value, user_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            message: "Successfully Updated " + type + " for User (" + user_id + ")"
        });
        return;
    });
});

// Inserts a new interaction sender.
router.post("/interactions/senders/:user_id", check_authentication, check_admin, check_if_sender_does_not_exist, (request, response, next) => {
    user_id = request.params.user_id;

    query = "INSERT INTO interaction_senders (user_id) VALUES (?);";
    getConnection().query(query, [user_id], (error, rows, fields) => {
        if (error) {
            next(error);
            return;
        }

        response.status(201).json({
            message: "Successfully Created Interaction Sender User (" + user_id + ")"
        });
        return;
    });
});

// Deletes an interaction sender.
router.delete("/interactions/senders/:user_id", check_authentication, check_admin, (request, response, next) => {
    user_id = request.params.user_id;

    query = "DELETE FROM interaction_senders WHERE user_id = (?);";
    getConnection().query(query, [user_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            message: "Successfully Removed User (" + user_id + ") From Interaction Senders"
        });
        return;
    });
});

// ========================================
//          INTERACTIONS RECEIVED
// ========================================

// Gets all interaction receivers.
router.get("/interactions/receivers", check_authentication, check_admin, (request, response, next) => {
    query = "SELECT * FROM interaction_receivers;";
    getConnection().query(query, (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            interactions_received: rows,
            message: "Successfully Retrieved Interaction Receivers"
        });
        return;
    })
});

// Gets all interactions received for a specified user.
router.get("/interactions/receivers/:user_id", check_authentication, check_if_receiver_exists, (request, response, next) => {
    user_id = request.params.user_id;

    type = request.query.type;

    query = "SELECT * FROM interaction_receivers WHERE user_id = (?);";
    getConnection().query(query, [user_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        if (!type) {
            response.status(200).json({
                interactions_received: rows[0],
                message: "Successfully Retrieved Interactions Received for User (" + user_id + ")"
            });
            return;
        } else {
            var type_amount;
            type += "_amount"

            if (!rows[0][type] === undefined) {
                type_amount = type + " Does Not Exist";
            } else {
                type_amount = rows[0][type];
            }

            response.status(200).json({
                [type]: type_amount,
                message: "Successfully Retrieved " + type + " Interactions Received for User (" + user_id + ")"
            });
            return;
        }
    });
});

// Updates interactions for a user.
router.patch("/interactions/receivers/:user_id", check_authentication, check_admin, check_if_receiver_exists, (request, response, next) => {
    user_id = request.params.user_id;
    type = request.query.type;
    value = request.query.value;

    if (!type || !value) {
        response.status(500).json({
            variables: {
                user_id: user_id,
                type: type || "undefined",
                value: value || "undefined"
            },
            message: "A Variable is Undefined"
        });
        return;
    }

    type += "_amount"

    query = "UPDATE interaction_receivers SET ?? = (?) WHERE user_id = (?);";
    getConnection().query(query, [type, value, user_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            message: "Successfully Updated " + type + " for User (" + user_id + ")"
        });
        return;
    });
});

// Inserts a new interaction receiver
router.post("/interactions/receivers/:user_id", check_authentication, check_admin, check_if_receiver_does_not_exist, (request, response, next) => {

    user_id = request.params.user_id;

    query = "INSERT INTO interaction_receivers (user_id) VALUES (?);";
    getConnection().query(query, [user_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(201).json({
            message: "Successfully Created Interaction Receiver User (" + user_id + ")"
        });
        return;
    });
});

// Deletes a user from interaction receivers.
router.delete("/interactions/receivers/:user_id", check_authentication, check_admin, (request, response, next) => {
    user_id = request.params.user_id;

    query = "DELETE FROM interaction_receivers WHERE user_id = (?);";
    getConnection().query(query, [user_id], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            message: "Successfully Removed User (" + user_id + ") From Interaction Receivers"
        });
        return;
    });
});

module.exports = router;

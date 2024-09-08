const express = require('express');
const router = express.Router();

const getConnection = require('./modules/mysql-connection');
const check_authentication = require('../../middleware/check-auth');
const check_admin = require('../../middleware/check-admin.js');

// Gets the current bot information.
router.get("/cafeBot", (request, response, next) => {
    query = "SELECT * FROM bot_information;";
    connection = getConnection();
    connection.query(query, (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            bot_information: {
                "version": rows[0].version
            },
            message: "Successfully Retrieved the Current Bot Information"
        });
        return;
    });
});

// Updates the cafeBog Version
router.patch("/cafeBot", check_authentication, check_admin, (request, response, next) => {
    version = request.query.version;

    if (!version) {
        response.status(400).json({
            variables: {
                version: version || "undefined"
            },
            message: "A Variable is Undefined"
        });
        return;
    }
    query = "UPDATE bot_information SET version = (?) WHERE id = 1;";
    getConnection().query(query, [version], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            message: "Successfully Updated cafeBot Version (" + version + ")"
        });
        return;
    });
});

module.exports = router;

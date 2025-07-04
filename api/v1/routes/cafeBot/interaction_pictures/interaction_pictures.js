const express = require('express');
const router = express.Router();

const getConnection = require('../../cafeBot/modules/mysql-connection.js');
const check_authentication = require('../../../middleware/check-auth.js');
const check_admin = require('../../../middleware/check-admin.js');

const api_url = "https://cdn.beanbeanjuice.com/images/cafeBot/interaction/{type}/{number}.gif"

const types = {
    STAB: {
        amount: 4,
        url_type: "stabs"
    },
    HMPH: {
        amount: 8,
        url_type: "hmphs"
    },
    THROW: {
        amount: 6,
        url_type: "throws"
    },
    RAGE: {
        amount: 14,
        url_type: "rages"
    },
    DAB: {
        amount: 11,
        url_type: "dabs"
    },
    BONK: {
        amount: 13,
        url_type: "bonks"
    },
    WELCOME: {
        amount: 9,
        url_type: "welcomes"
    },
    SHUSH: {
        amount: 11,
        url_type: "shushes"
    }
}

const check_type = (request, response, next) => {
    const type = request.params.type.toUpperCase();

    if (!types[type]) {
        response.status(418).json({
            message: "Only a valid type is allowed.",
            types: Object.keys(types)
        });
        return;
    }

    next();
}

// Retrieves a random picture.
router.get("/interaction_pictures/:type", check_authentication, check_type, (request, response, next) => {
    const type = request.params.type.toUpperCase();

    const type_amount = types[type].amount;
    const type_url = types[type].url_type;
    let url = api_url.replace("{type}", type_url);
    url = url.replace("{number}", randomIntFromInterval(1, type_amount).toString());
    
    response.status(200).json({
        message: "Successfully retrieved " + type + " URL.",
        url: url
    });
});

// Generates a random number
function randomIntFromInterval(min, max) { // min and max included 
    return Math.floor(Math.random() * (max - min + 1) + min)
}

module.exports = router;

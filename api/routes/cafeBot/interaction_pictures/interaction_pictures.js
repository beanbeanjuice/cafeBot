const express = require('express');
const router = express.Router();

const getConnection = require('../../cafeBot/modules/mysql-connection.js');
const check_authentication = require('../../../middleware/check-auth.js');
const check_admin = require('../../../middleware/check-admin.js');

const api_url = "https://cdn.beanbeanjuice.com/images/cafeBot/interaction/{type}/{number}.gif"

const types = {
    HUG: {
        amount: 22,
        url_type: "hugs"
    },
    PUNCH: {
        amount: 8,
        url_type: "punches"
    },
    KISS: {
        amount: 25,
        url_type: "kisses"
    },
    BITE: {
        amount: 17,
        url_type: "bites"
    },
    BLUSH: {
        amount: 21,
        url_type: "blushes"
    },
    CUDDLE: {
        amount: 15,
        url_type: "cuddles"
    },
    NOM: {
        amount: 16,
        url_type: "noms"
    },
    POKE: {
        amount: 13,
        url_type: "pokes"
    },
    SLAP: {
        amount: 14,
        url_type: "slaps"
    },
    STAB: {
        amount: 4,
        url_type: "stabs"
    },
    HMPH: {
        amount: 8,
        url_type: "hmphs"
    },
    POUT: {
        amount: 13,
        url_type: "pouts"
    },
    THROW: {
        amount: 6,
        url_type: "throws"
    },
    SMILE: {
        amount: 15,
        url_type: "smiles"
    },
    STARE: {
        amount: 13,
        url_type: "stares"
    },
    TICKLE: {
        amount: 10,
        url_type: "tickles"
    },
    RAGE: {
        amount: 14,
        url_type: "rages"
    },
    YELL: {
        amount: 11,
        url_type: "yells"
    },
    HEADPAT: {
        amount: 18,
        url_type: "headpats"
    },
    CRY: {
        amount: 14,
        url_type: "cries"
    },
    DANCE: {
        amount: 12,
        url_type: "dances"
    },
    DAB: {
        amount: 11,
        url_type: "dabs"
    },
    BONK: {
        amount: 13,
        url_type: "bonks"
    },
    SLEEP: {
        amount: 18,
        url_type: "sleeps"
    },
    DIE: {
        amount: 7,
        url_type: "dies"
    },
    WELCOME: {
        amount: 9,
        url_type: "welcomes"
    },
    LICK: {
        amount: 14,
        url_type: "licks"
    },
    SHUSH: {
        amount: 11,
        url_type: "shushes"
    }
}

const check_type = (request, response, next) => {
    type = request.params.type;
    type = type.toUpperCase();

    if (!types[type]) {
        response.status(418).json({
            variables: types,
            message: "Only a Valid Variable is Acceptable"
        });
        return;
    }

    next();
}

// Retrieves a random picture.
router.get("/interaction_pictures/:type", check_authentication, check_type, (request, response, next) => {
    type = request.params.type;
    type = type.toUpperCase();

    type_amount = types[type].amount;
    type_url = types[type].url_type;
    var url = api_url.replace("{type}", type_url);
    url = url.replace("{number}", randomIntFromInterval(1, type_amount));
    
    response.status(200).json({
        url: url,
        message: "Successfully Retrieved " + type + " URL."
    });
    return;
});

// Generates a random number
function randomIntFromInterval(min, max) { // min and max included 
    return Math.floor(Math.random() * (max - min + 1) + min)
}

module.exports = router;

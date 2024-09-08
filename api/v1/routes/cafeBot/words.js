const express = require('express');
const router = express.Router();

const getConnection = require('./modules/mysql-connection');
const check_authentication = require('../../middleware/check-auth');
const check_admin = require('../../middleware/check-admin.js');

// Checks if a word exists
const check_if_word_exists = (request, response, next) => {
    word = request.params.word;
    query = "SELECT * FROM serve_words WHERE word = (?);";

    getConnection().query(query, [word], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        if (!rows.length) {
            const request_error = new Error("Word (" + word + ") Not Found");
            request_error.status = 404;
            next (request_error);
            return;
        }

        next();
    });
}

// Gets all dictionary words.
router.get("/words", check_authentication, (request, response, next) => {
    query = "SELECT * FROM serve_words;";

    getConnection().query(query, (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            words: rows,
            message: "Successfully Retrieved All Words"
        });
        return;
    });
});

// Gets a specific word
router.get("/words/:word", check_authentication, check_if_word_exists, (request, response, next) => {
    word = request.params.word;

    query = "SELECT * FROM serve_words WHERE word = (?);";
    getConnection().query(query, [word], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            word: rows[0],
            message: "Successfully Retrieved Word (" + word + ")"
        });
        return;
    });
});

// Updates a specific word.
router.patch("/words/:word", check_authentication, check_admin, check_if_word_exists, (request, response, next) => {
    word = request.params.word;
    uses = request.query.uses;

    if (!uses) {
        response.status(400).json({
            variables: {
                word: word,
                uses: uses || "undefined"
            },
            message: "A Variable is Undefined"
        });
        return;
    }

    query = "UPDATE cafeBot.serve_words SET uses = (?) WHERE word = (?);";
    getConnection().query(query, [uses, word], (error, rows, fields) => {
        if (error) {
            next(new Error(error));
            return;
        }

        response.status(200).json({
            message: "Successfully Updated Word (" + word + ") With Uses (" + uses + ")"
        })
        return;
    });
});

module.exports = router;

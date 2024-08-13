const express = require('express');
const router = express.Router();

const check_authentication = require('../../middleware/check-auth');
const getConnection = require('./modules/mysql-connection');

// Gets a list of all games.
router.get("/games", (request, response) => {
    const connection = getConnection();
    const query = "SELECT * FROM game_data;";
    connection.query(query, (error, rows, fields) => {
        if (error) {
            console.log("Failed to Query Games: " + error);
            response.status(500).json({
                message: error
            });
            return;
        }
        response.status(200).json({
            games: rows,
            message: "Successfully Retrieved Games"
        });
    });
});

// Gets a list of all games for a specific user.
router.get("/games/:user_id", (request, response) => {
    const connection = getConnection();
    const query = "SELECT * FROM game_data WHERE user_id = (?);";
    const user_id = request.params.user_id;

    connection.query(query, [user_id], (error, rows, fields) => {
        if (error) {
            console.log("Error Getting Games for User (" + user_id + "): " + error);
            response.status(500).json({
                message: error
            });
        }

        response.status(200).json({
            games: rows,
            message: "Successfully Retrieved Games"
        });
    });
})

// Gets a specific game for a specific user.
router.get("/game", (request, response) => {
    const user_id = request.query.user_id;
    const game_id = request.query.game_id;

    // Checking if the userID and gameID exist.
    if (typeof user_id === 'undefined' || typeof game_id === 'undefined') {
        response.status(400).json({
            message: "A Variable is Undefined"
        });
        return; // Must return so the app doesn't crash. Stops all the rest of the code from running.
    }

    const connection = getConnection();
    const query = "SELECT * FROM game_data WHERE user_id = (?) AND game_id = (?);";
    connection.query(query, [user_id, game_id], (error, rows, fields) => {

        if (error) {
            console.log("Failed to Query Game (" + game_id + "): " + error);
            response.status(500).json({
                message: error
            });
            return;
        }

        response.status(200).json({
            game: rows,
            message: "Successfully Retrieved Game (" + game_id + ") For User (" + user_id + ")"
        });
    });
});

// Adds a new game to the database.
router.post('/game_create', check_authentication, (request, response) => {
    console.log("Attempting to add a new game to the database...");

    const user_id = request.user_data.user_id;
    const game_id = request.query.game_id;
    const score = request.query.score;
    const board_characters = request.query.board_characters;
    const time_taken = request.query.time_taken;
    const time_allowed = request.query.time_allowed;
    const words_found = request.query.words_found;
    const total_words = request.query.total_words;
    const found_words_string_list = request.query.found_words_string_list;
    const total_words_string_list = request.query.total_words_string_list;

    if (user_id === 'undefined' || game_id === 'undefined' || score === 'undefined'
        || board_characters === 'undefined' || time_taken === 'undefined' || time_allowed === 'undefined'
        || words_found === 'undefined' || total_words === 'undefined' || found_words_string_list === 'undefined'
        || total_words_string_list === 'undefined') {
        response.status(400).json({
            message: "A Variable is Undefined"
        });
        return;
    }

    const check_query = "SELECT * FROM game_data WHERE user_id = (?) AND game_id = (?);";
    const check_connection = getConnection();
    check_connection.query(check_query, [user_id, game_id], (error, rows, fields) => {
        if (error) {
            response.status(500).json({
                message: "Error Checking if Game Exists"
            });
            return;
        }

        if (rows.length > 0) {
            response.status(500).json({
                message: "A Game with ID (" + game_id + ") for User (" + user_id + ") Already Exists"
            })
            return;
        }

        // localhost:4101/boggle/v1/game_create?user_id=2&game_id=3&score=100&board_characters=PQUIFJZLAKDNALXOW&time_taken=94&time_allowed=300&words_found=100&total_words=150&found_words_string_list=hello,test,bruh,one,two&total_words_string_list=hello,test,bruh,one,two,three
        const query = "INSERT INTO game_data " +
            "(user_id, game_id, score, board_characters, time_taken, time_allowed, words_found, total_words, found_words_string_list, total_words_string_list) " +
            "VALUES(?,?,?,?,?,?,?,?,?,?);";
        const connection = getConnection();
        connection.query(query, [user_id, game_id, score, board_characters, time_taken, time_allowed, words_found, total_words, found_words_string_list, total_words_string_list],
            (error, rows, fields) => {
                if (error) {
                    console.log("Failed to Insert New Game: " + error);
                    response.status(500).json({
                        message: error
                    });
                    return;
                }

                console.log("Inserted New Game (" + game_id + ") for User (" + user_id + ")");
                response.status(201).json({
                    message: "Successfully Added New Game (" + game_id + ") for User (" + user_id + ")"
                });
            });
    });
});

module.exports = router;
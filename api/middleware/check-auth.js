const jwt = require('jsonwebtoken');
const checkType = require('../middleware/check-type');

const TOKEN = checkType.TOKEN;

module.exports = (request, response, next) => {
    try {
        const token = request.headers.authorization;
        const decoded_token = jwt.verify(token, TOKEN);
        request.user_data = decoded_token;
        next(); // Called if successfully authenticated.
    } catch (error) {
        return response.status(401).json({
            message: "Authentication Failed"
        });
    }
};

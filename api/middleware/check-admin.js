module.exports = (request, response, next) => {
    try {
        user_type = request.user_data.user_type;

        if (user_type === 'ADMIN') {
            next();
        } else {
            response.status(401).json({
                message: "Authentication Failed"
            });
        }
    } catch (error) {
        return response.status(401).json({
            message: "Authentication Failed"
        });
    }
};

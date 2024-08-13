const mysql = require('mysql2');
const checkType = require('../../../middleware/check-type');

// TODO: Confirm this works.
const mySQL_DATA = checkType.getMySQLData();
const mysql_database = "cafeBot";

// Creates a MySQL pool. Allows multiple connections.
const pool = mysql.createPool({
    connectionLimit: 200,
    host: mySQL_DATA.url,
    port: mySQL_DATA.port,
    user: mySQL_DATA.user,
    password: mySQL_DATA.password,
    database: mysql_database,
    supportBigNumbers: true,
    bigNumberStrings: true
});

module.exports = () => {
    return pool;
}

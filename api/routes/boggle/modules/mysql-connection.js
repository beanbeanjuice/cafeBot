const mysql = require('mysql2');

const mysql_url = "beanbeanjuice.com";
const mysql_port = 4001;
const mysql_user = "root";
const mysql_password = "refr}2Z,.+ugT2Gd*.v6N36Ad,rb)L";
const mysql_database = "boggle";

// Creates a MySQL pool. Allows multiple connections.
const pool = mysql.createPool({
    connectionLimit: 10,
    host: mysql_url,
    port: mysql_port,
    user: mysql_user,
    password: mysql_password,
    database: mysql_database
});

module.exports = () => {
    return pool;
}
CREATE TABLE users
(
user_id INT NOT NULL AUTO_INCREMENT,
username TEXT NOT NULL,
password TEXT NOT NULL,
user_type VARCHAR(10) DEFAULT 'USER',
PRIMARY KEY (user_id),
UNIQUE KEY user_id_index (user_id)
);
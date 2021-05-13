CREATE TABLE cafe_information
(user_id BIGINT,
 bean_coins DOUBLE DEFAULT 0,
 last_serving_time TIMESTAMP DEFAULT null,
 orders_bought INT DEFAULT 0,
 orders_received INT DEFAULT 0);
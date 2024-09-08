CREATE TABLE IF NOT EXISTS user_birthdays (
    user_id INT UNSIGNED NOT NULL,
    birth_date TEXT NOT NULL,
    time_zone TEXT NOT NULL,
    PRIMARY KEY (user_id),
    UNIQUE (user_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

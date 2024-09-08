CREATE TABLE IF NOT EXISTS user_codes (
    user_id INT UNSIGNED NOT NULL,
    code TEXT NOT NULL,
    PRIMARY KEY (user_id),
    UNIQUE (user_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

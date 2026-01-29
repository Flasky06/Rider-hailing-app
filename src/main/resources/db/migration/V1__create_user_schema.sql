CREATE TABLE users (
    id BINARY(16) NOT NULL PRIMARY KEY,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    name VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    cell_no VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    verification_token VARCHAR(255),
    reset_password_token VARCHAR(255)
);

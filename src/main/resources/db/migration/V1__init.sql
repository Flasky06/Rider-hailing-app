CREATE TABLE users (
    id BINARY(16) NOT NULL PRIMARY KEY,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    name VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    cell_no VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE riders (
    id BINARY(16) NOT NULL PRIMARY KEY,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    user_id BINARY(16) NOT NULL UNIQUE,
    available BOOLEAN NOT NULL DEFAULT TRUE,
    deliveries_today INT NOT NULL DEFAULT 0,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_rider_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE delivery_requests (
    id BINARY(16) NOT NULL PRIMARY KEY,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    pickup_location VARCHAR(255) NOT NULL,
    dropoff_location VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    client_id BINARY(16) NOT NULL,
    rider_id BINARY(16),
    CONSTRAINT fk_delivery_client FOREIGN KEY (client_id) REFERENCES users(id),
    CONSTRAINT fk_delivery_rider FOREIGN KEY (rider_id) REFERENCES riders(id)
);

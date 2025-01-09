CREATE DATABASE warehouse_management;

\c warehouse_management


CREATE TYPE user_role AS ENUM ('manager', 'operator');


CREATE TYPE product_state_type AS ENUM ('sorting_to_store', 'sorting_to_ship', 'stored', 'shipped', 'disposed');


CREATE TYPE request_state_type AS ENUM ('pending', 'accepted', 'declined');


CREATE TABLE App_User (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role user_role NOT NULL
);


CREATE TABLE Worker (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    middle_name VARCHAR(100),
    birth_date DATE NOT NULL,
    hire_date DATE NOT NULL DEFAULT CURRENT_DATE
);


CREATE TABLE Product_Type (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);


CREATE TABLE Location (
    id SERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    location_row INT
);


CREATE TABLE Partners (
    id SERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    phone_number VARCHAR(20) UNIQUE,
    address TEXT
);


CREATE TABLE Product (
    id SERIAL PRIMARY KEY,
    product_type_id INT REFERENCES Product_Type(id),
    location_id INT REFERENCES Location(id),
    supplier INT REFERENCES Partners(id),
    customer INT REFERENCES Partners(id),
    queue_id INT REFERENCES Queue(id),
    name VARCHAR(100) NOT NULL,
    description TEXT,
    expiration_date DATE,
    product_state product_state_type NOT NULL,
    priority INT
);


CREATE TABLE Queue (
    id SERIAL PRIMARY KEY,
    capacity INT NOT NULL,
    sorting_station_id INT NOT NULL REFERENCES Sorting_Station(id) ON DELETE CASCADE
);

CREATE TABLE Shelf (
    id SERIAL PRIMARY KEY,
    location_id INT NOT NULL REFERENCES Location(id) ON DELETE CASCADE
);


CREATE TABLE Warehouse (
    id SERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    address TEXT NOT NULL
);


CREATE TABLE Goods_Management_Center (
    id SERIAL PRIMARY KEY,
    warehouse_id INT NOT NULL REFERENCES Warehouse(id) ON DELETE CASCADE,
    location_id INT NOT NULL REFERENCES Location(id) ON DELETE CASCADE,
);


CREATE TABLE Sorting_Station (
    id SERIAL PRIMARY KEY,
    management_center_id INT NOT NULL REFERENCES Goods_Management_Center(id) ON DELETE CASCADE,
    location_id INT NOT NULL REFERENCES Location(id) ON DELETE CASCADE,
    capacity INT NOT NULL,
    sort_time_seconds INT NOT NULL
);

CREATE TABLE Manager (
    id SERIAL PRIMARY KEY,
    app_user_id INT NOT NULL REFERENCES App_User(id) ON DELETE CASCADE,
    worker_id INT NOT NULL REFERENCES Worker(id) ON DELETE CASCADE,
    warehouse_id INT NOT NULL REFERENCES Warehouse(id) ON DELETE CASCADE
);


CREATE TABLE Warehouse_Operator (
    id SERIAL PRIMARY KEY,
    app_user_id INT NOT NULL REFERENCES App_User(id) ON DELETE CASCADE,
    worker_id INT NOT NULL REFERENCES Worker(id) ON DELETE CASCADE
);


CREATE TABLE Product_In_Queue (
    id SERIAL PRIMARY KEY,
    product_id INT NOT NULL REFERENCES Product(id) ON DELETE CASCADE,
    queue_id INT NOT NULL REFERENCES Queue(id) ON DELETE CASCADE
);


CREATE TABLE Loaders_And_Shelves (
    id SERIAL PRIMARY KEY,
    worker_id INT NOT NULL REFERENCES Worker(id) ON DELETE CASCADE,
    shelf_id INT NOT NULL REFERENCES Shelf(id) ON DELETE CASCADE
);


CREATE TABLE Operator_Request (
    id SERIAL PRIMARY KEY,
    operator_id INT NOT NULL REFERENCES App_User(id) ON DELETE CASCADE,
    status request_state_type NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE Warehouse ADD COLUMN manager_id INT REFERENCES Manager(id);
ALTER TABLE Queue ADD COLUMN sorting_station_id INT REFERENCES Sorting_Station(id);


CREATE OR REPLACE FUNCTION check_initial_status()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.status != 'pending' THEN
        RAISE EXCEPTION 'Status must be "pending" when creating a new Operator Request';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_operator_request_initial_status
BEFORE INSERT ON Operator_Request
FOR EACH ROW
EXECUTE FUNCTION check_initial_status();


CREATE OR REPLACE FUNCTION check_status_transition()
RETURNS TRIGGER AS $$
BEGIN
    IF OLD.status != 'pending' AND NEW.status = 'pending' THEN
        RAISE EXCEPTION 'Status can only be updated to "accepted" or "declined" from "pending"';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_operator_request_status_transition
BEFORE UPDATE ON Operator_Request
FOR EACH ROW
EXECUTE FUNCTION check_status_transition();

CREATE OR REPLACE FUNCTION find_all_pending_operator_requests()
RETURNS SETOF operator_request AS $$
BEGIN
RETURN QUERY
SELECT o.*
FROM operator_request o
WHERE o.status = 'pending';
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION find_all_in_queue(queue_id INT)
RETURNS SETOF product AS $$
BEGIN
RETURN QUERY
SELECT p.*
FROM product p
WHERE p.queue_id = queue_id;
END;
$$ LANGUAGE plpgsql;

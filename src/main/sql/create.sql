CREATE TYPE user_role AS ENUM ('MANAGER', 'OPERATOR', 'USER');


CREATE TYPE product_state_type AS ENUM ('PENDING', 'SORTING_TO_STORE', 'SORTING_TO_SHIP', 'STORED', 'SHIPPED', 'DISPOSED');


CREATE TYPE request_state_type AS ENUM ('PENDING', 'ACCEPTED', 'DECLINED');


CREATE TABLE App_User (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role user_role NOT NULL
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

CREATE TABLE Warehouse (
    id SERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    address TEXT NOT NULL
);

CREATE TABLE Worker (
                        id SERIAL PRIMARY KEY,
                        first_name VARCHAR(100) NOT NULL,
                        last_name VARCHAR(100) NOT NULL,
                        middle_name VARCHAR(100),
                        birth_date DATE NOT NULL,
                        hire_date DATE NOT NULL DEFAULT CURRENT_DATE,
                        warehouse_id INT NOT NULL REFERENCES Warehouse(id) ON DELETE CASCADE
);

CREATE TABLE Sorting_Station (
                                 id SERIAL PRIMARY KEY,
                                 warehouse_id INT NOT NULL REFERENCES Warehouse(id) ON DELETE CASCADE,
                                 location_id INT NOT NULL REFERENCES Location(id) ON DELETE CASCADE,
                                 capacity INT NOT NULL,
                                 sort_time_seconds INT NOT NULL
);

CREATE TABLE Queue (
                       id SERIAL PRIMARY KEY,
                       capacity INT NOT NULL,
                       sorting_station_id INT NOT NULL REFERENCES Sorting_Station(id) ON DELETE CASCADE
);

CREATE TABLE Warehouse_Operator (
                                    id SERIAL PRIMARY KEY,
                                    app_user_id INT NOT NULL REFERENCES App_User(id) ON DELETE CASCADE,
                                    product_type_id INT NOT NULL REFERENCES Product_Type(id) ON DELETE CASCADE
);

CREATE TABLE Loaders_And_Shelves (
                                     id SERIAL PRIMARY KEY,
                                     worker_id INT NOT NULL REFERENCES Worker(id) ON DELETE CASCADE,
                                     shelf_id INT NOT NULL REFERENCES Location(id) ON DELETE CASCADE
);

CREATE TABLE Operator_Request (
                                  id SERIAL PRIMARY KEY,
                                  operator_id INT NOT NULL REFERENCES App_User(id) ON DELETE CASCADE,
                                  status request_state_type NOT NULL,
                                  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Product (
    id SERIAL PRIMARY KEY,
    product_type_id INT REFERENCES Product_Type(id),
    location_id INT REFERENCES Location(id),
    supplier_id INT REFERENCES Partners(id),
    customer_id INT REFERENCES Partners(id),
    queue_id INT REFERENCES Queue(id),
    name VARCHAR(100) NOT NULL,
    description TEXT,
    expiration_date DATE,
    product_state product_state_type NOT NULL,
    priority INT
);

CREATE TABLE Product_In_Queue (
    id SERIAL PRIMARY KEY,
    product_id INT NOT NULL REFERENCES Product(id) ON DELETE CASCADE,
    queue_id INT NOT NULL REFERENCES Queue(id) ON DELETE CASCADE
);

CREATE OR REPLACE FUNCTION check_status_transition()
RETURNS TRIGGER AS $$
BEGIN
    IF OLD.status != 'PENDING' AND NEW.status = 'PENDING' THEN
        RAISE EXCEPTION 'Status can only be updated to "accepted" or "declined" from "PENDING"';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_operator_request_status_transition
BEFORE UPDATE ON Operator_Request
FOR EACH ROW
EXECUTE FUNCTION check_status_transition();

CREATE OR REPLACE FUNCTION find_all_operator_requests_by_user(user_id INT)
    RETURNS SETOF operator_request AS $$
BEGIN
    RETURN QUERY
        SELECT o.*
        FROM operator_request o
        WHERE o.operator_id = $1;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION find_all_pending_operator_requests()
RETURNS SETOF operator_request AS $$
BEGIN
RETURN QUERY
SELECT o.*
FROM operator_request o
WHERE o.status = 'PENDING';
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION find_all_in_queue(queue INT)
RETURNS SETOF product AS $$
BEGIN
RETURN QUERY
SELECT p.*
FROM product p
WHERE p.queue_id = queue;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION find_queues_by_sorting_station(sorting_station INT)
RETURNS SETOF queue AS $$
BEGIN
RETURN QUERY
SELECT q.*
FROM queue q
WHERE q.sorting_station_id = sorting_station;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION find_user_by_username(username TEXT)
    RETURNS SETOF app_user AS $$
BEGIN
    RETURN QUERY
        SELECT u.*
        FROM app_user u
        WHERE u.username = $1
        LIMIT 1;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION find_warehouse_operator_by_user_id(user_id INT)
RETURNS SETOF warehouse_operator AS $$
BEGIN
RETURN QUERY
SELECT wo.*
FROM warehouse_operator wo
WHERE wo.app_user_id = user_id
    LIMIT 1;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_product_types_paginated(page INT, size INT)
    RETURNS TABLE(id INT, name VARCHAR) AS $$
BEGIN
    RETURN QUERY
        SELECT pt.id, pt.name
        FROM product_type pt
        ORDER BY pt.id
        LIMIT size OFFSET page * size;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_product_types_count()
    RETURNS INT AS $$
BEGIN
    RETURN (
        SELECT COUNT(*)
        FROM Product_Type
    );
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_products_paginated(page INT, size INT)
    RETURNS SETOF JSONB AS $$
BEGIN
    RETURN QUERY
        SELECT jsonb_build_object(
                       'id', p.id,
                       'product_type_id', p.product_type_id,
                       'location_id', p.location_id,
                       'supplier_id', p.supplier_id,
                       'customer_id', p.customer_id,
                       'queue_id', p.queue_id,
                       'name', p.name,
                       'description', p.description,
                       'expiration_date', p.expiration_date,
                       'product_state', p.product_state::product_state_type,
                       'priority', p.priority
               )
        FROM Product p
        ORDER BY p.id
        LIMIT size OFFSET page * size;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_products_count()
    RETURNS INT AS $$
BEGIN
    RETURN (
        SELECT COUNT(*)
        FROM Product
    );
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_queues_paginated(page INT, size INT)
    RETURNS SETOF JSONB AS $$
BEGIN
    RETURN QUERY
        SELECT jsonb_build_object(
                       'id', q.id,
                       'capacity', q.capacity,
                       'sorting_station_id', q.sorting_station_id
               )
        FROM Queue q
        ORDER BY q.id
        LIMIT size OFFSET page * size;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_queues_count()
    RETURNS INT AS $$
BEGIN
    RETURN (
        SELECT COUNT(*)
        FROM Queue
    );
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_sorting_stations_paginated(page INT, size INT)
    RETURNS SETOF JSONB AS $$
BEGIN
    RETURN QUERY
        SELECT jsonb_build_object(
                       'id', s.id,
                       'warehouse_id', s.warehouse_id,
                       'location_id', s.location_id,
                       'capacity', s.capacity,
                       'sort_time_seconds', s.sort_time_seconds
               )
        FROM Sorting_Station s
        ORDER BY s.id
        LIMIT size OFFSET page * size;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_sorting_stations_count()
    RETURNS INT AS $$
BEGIN
    RETURN (
        SELECT COUNT(*)
        FROM Sorting_Station
    );
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_workers_paginated(page INT, size INT)
    RETURNS SETOF JSONB AS $$
BEGIN
    RETURN QUERY
        SELECT jsonb_build_object(
                       'id', w.id,
                       'first_name', w.first_name,
                       'last_name', w.last_name,
                       'middle_name', w.middle_name,
                       'birth_date', w.birth_date,
                       'hire_date', w.hire_date,
                       'warehouse_id', w.warehouse_id
               )
        FROM Worker w
        ORDER BY w.id
        LIMIT size OFFSET page * size;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_workers_count()
    RETURNS INT AS $$
BEGIN
    RETURN (
        SELECT COUNT(*)
        FROM Worker
    );
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_locations_paginated(page INT, size INT)
    RETURNS SETOF JSONB AS $$
BEGIN
    RETURN QUERY
        SELECT jsonb_build_object(
                       'id', l.id,
                       'name', l.name,
                       'description', l.description,
                       'location_row', l.location_row
               )
        FROM Location l
        ORDER BY l.id
        LIMIT size OFFSET page * size;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_locations_count()
    RETURNS INT AS $$
BEGIN
    RETURN (
        SELECT COUNT(*)
        FROM Location
    );
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_partners_paginated(page INT, size INT)
    RETURNS SETOF JSONB AS $$
BEGIN
    RETURN QUERY
        SELECT jsonb_build_object(
                       'id', p.id,
                       'name', p.name,
                       'email', p.email,
                       'phone_number', p.phone_number,
                       'address', p.address
               )
        FROM Partners p
        ORDER BY p.id
        LIMIT size OFFSET page * size;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_partners_count()
    RETURNS INT AS $$
BEGIN
    RETURN (
        SELECT COUNT(*)
        FROM Partners
    );
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_warehouses_paginated(page INT, size INT)
    RETURNS SETOF JSONB AS $$
BEGIN
    RETURN QUERY
        SELECT jsonb_build_object(
                       'id', w.id,
                       'name', w.name,
                       'address', w.address
               )
        FROM Warehouse w
        ORDER BY w.id
        LIMIT size OFFSET page * size;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_warehouses_count()
    RETURNS INT AS $$
BEGIN
    RETURN (
        SELECT COUNT(*)
        FROM Warehouse
    );
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_locations_paginated(page INT, size INT)
    RETURNS SETOF JSONB AS $$
BEGIN
RETURN QUERY
SELECT jsonb_build_object(
               'id', l.id,
               'name', l.name,
               'description', l.description,
               'location_row', l.location_row
       )
FROM location l
ORDER BY l.id
    LIMIT size OFFSET page * size;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_locations_count()
    RETURNS INT AS $$
BEGIN
RETURN (
    SELECT COUNT(*)
    FROM location
);
END;
$$ LANGUAGE plpgsql;


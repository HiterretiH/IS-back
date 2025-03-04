CREATE TYPE product_state_type AS ENUM ('PENDING', 'SORTING_TO_STORE', 'SORTING_TO_SHIP', 'STORED', 'SHIPPED', 'DISPOSED');
CREATE TYPE request_state_type AS ENUM ('PENDING', 'ACCEPTED', 'DECLINED');
CREATE OR REPLACE FUNCTION check_initial_status()
    RETURNS TRIGGER AS $$
BEGIN
    IF NEW.status != 'PENDING' THEN
        RAISE EXCEPTION 'Status must be "PENDING" when creating a new Operator Request';
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
    IF OLD.status != 'PENDING' AND NEW.status = 'PENDING' THEN
        RAISE EXCEPTION 'Status can only be updated to "ACCEPTED" or "DECLINED" from "PENDING"';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_operator_request_status_transition
    BEFORE UPDATE ON Operator_Request
    FOR EACH ROW
EXECUTE FUNCTION check_status_transition();



DROP FUNCTION IF EXISTS find_all_pending_operator_requests();
DROP FUNCTION IF EXISTS find_all_in_queue(int);
DROP FUNCTION IF EXISTS find_queues_by_sorting_station(int);
DROP FUNCTION IF EXISTS find_user_by_username(text);
DROP FUNCTION IF EXISTS find_warehouse_operator_by_user_id(int);

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


DROP FUNCTION IF EXISTS get_product_types_count() CASCADE;
DROP FUNCTION IF EXISTS get_products_count() CASCADE;
DROP FUNCTION IF EXISTS get_queues_count() CASCADE;
DROP FUNCTION IF EXISTS get_sorting_stations_count() CASCADE;
DROP FUNCTION IF EXISTS get_workers_count() CASCADE;
DROP FUNCTION IF EXISTS get_locations_count() CASCADE;
DROP FUNCTION IF EXISTS get_partners_count() CASCADE;
DROP FUNCTION IF EXISTS get_warehouses_count() CASCADE;

DROP FUNCTION IF EXISTS get_product_types_paginated(INT, INT) CASCADE;
DROP FUNCTION IF EXISTS get_products_paginated(INT, INT) CASCADE;
DROP FUNCTION IF EXISTS get_queues_paginated(INT, INT) CASCADE;
DROP FUNCTION IF EXISTS get_sorting_stations_paginated(INT, INT) CASCADE;
DROP FUNCTION IF EXISTS get_workers_paginated(INT, INT) CASCADE;
DROP FUNCTION IF EXISTS get_locations_paginated(INT, INT) CASCADE;
DROP FUNCTION IF EXISTS get_partners_paginated(INT, INT) CASCADE;
DROP FUNCTION IF EXISTS get_warehouses_paginated(INT, INT) CASCADE;

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
    RETURNS SETOF TEXT AS $$
BEGIN
    RETURN QUERY
        SELECT (jsonb_build_object(
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
                )::text)
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
    RETURNS SETOF TEXT AS $$
BEGIN
    RETURN QUERY
        SELECT (jsonb_build_object(
                'id', q.id,
                'capacity', q.capacity,
                'sorting_station_id', q.sorting_station_id
                )::text)
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
    RETURNS SETOF TEXT AS $$
BEGIN
    RETURN QUERY
        SELECT (jsonb_build_object(
                'id', s.id,
                'warehouse_id', s.warehouse_id,
                'location_id', s.location_id,
                'capacity', s.capacity,
                'sort_time_seconds', s.sort_time_seconds
                )::text)
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
    RETURNS SETOF TEXT AS $$
BEGIN
    RETURN QUERY
        SELECT (jsonb_build_object(
                'id', w.id,
                'first_name', w.first_name,
                'last_name', w.last_name,
                'middle_name', w.middle_name,
                'status', w.status,
                'birth_date', w.birth_date,
                'hire_date', w.hire_date,
                'warehouse_id', w.warehouse_id
                )::text)
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
    RETURNS SETOF TEXT AS $$
BEGIN
    RETURN QUERY
        SELECT (jsonb_build_object(
                'id', l.id,
                'name', l.name,
                'description', l.description,
                'location_row', l.location_row
                )::text)
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
    RETURNS SETOF TEXT AS $$
BEGIN
    RETURN QUERY
        SELECT (jsonb_build_object(
                'id', p.id,
                'name', p.name,
                'email', p.email,
                'phone_number', p.phone_number,
                'address', p.address
                )::text)
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
    RETURNS SETOF TEXT AS $$
BEGIN
    RETURN QUERY
        SELECT (jsonb_build_object(
                'id', w.id,
                'name', w.name,
                'address', w.address
                )::text)
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
    RETURNS SETOF TEXT AS $$
BEGIN
    RETURN QUERY
        SELECT (jsonb_build_object(
                'id', l.id,
                'name', l.name,
                'description', l.description,
                'location_row', l.location_row
                )::text)
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

CREATE OR REPLACE FUNCTION find_all_operator_requests_by_user(user_id INT)
    RETURNS TABLE (
                      id INT,
                      operator_id INT,
                      status request_state_type,
                      created_at TIMESTAMP
                  ) AS $$
BEGIN
    RETURN QUERY
        SELECT r.id, r.operator_id, r.status::request_state_type, r.created_at
        FROM operator_request r
        WHERE r.operator_id = user_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_queues_count_by_sorting_station(sorting_station_id_param INT)
    RETURNS INT AS $$
BEGIN
    RETURN (
        SELECT COUNT(*)
        FROM Queue
        WHERE sorting_station_id = sorting_station_id_param
    );
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_products_count_by_queue(queue_id_param INT)
    RETURNS INT AS $$
BEGIN
    RETURN (
        SELECT COUNT(*)
        FROM Product
        WHERE queue_id = queue_id_param
    );
END;
$$ LANGUAGE plpgsql;




INSERT INTO Location (name, description, location_row)
SELECT
    'Location #' || i,
    'Description for Location #' || i,
    i
FROM
    generate_series(1, 100000) i;

INSERT INTO Partners (name, email, phone_number, address)
SELECT
    'Partner #' || i,
    'partner' || i || '@example.com',
    '153-456-789' || i,
    'Address for Partner #' || i
FROM
    generate_series(1, 100000) i;

INSERT INTO Warehouse (name, address)
SELECT
    'Warehouse #' || i, 'Address #' || i
FROM
    generate_series(1, 100000) i;

INSERT INTO Worker (first_name, last_name, middle_name, birth_date, status, warehouse_id, hire_date)
SELECT
    'Worker #' || i, 'Last name #' || i, 'Middle name #' || i, '2000-01-01', 'PENDING', i, CURRENT_DATE
FROM
    generate_series(1, 100000) i;

INSERT INTO Sorting_Station (location_id, capacity, sort_time_seconds, warehouse_id)
SELECT
    i, 10, 10, i
FROM
    generate_series(1, 100000) i;

INSERT INTO Queue (capacity, sorting_station_id)
SELECT
    i, i
FROM
    generate_series(1, 100000) i;

INSERT INTO Product_Type (name)
SELECT
    'Product Type #' || i
FROM
    generate_series(1, 100000) i;

INSERT INTO Product (product_type_id, location_id, supplier_id, customer_id, name, description, expiration_date, product_state, priority)
SELECT
    i,
    i,
    i,
    i,
    'Product #' || i,
    'Description for Product #' || i,
    CURRENT_DATE,
    'STORED',
    i
FROM
    generate_series(1, 100000) i;

INSERT INTO Loaders_And_Shelves (worker_id, shelf_id)
SELECT
    i,
    i
FROM
    generate_series(1, 100000) i;

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



CREATE OR REPLACE FUNCTION find_all_pending_operator_requests()
RETURNS TABLE(id INT, operator_id INT, status VARCHAR, created_at TIMESTAMP) AS $$
BEGIN
RETURN QUERY
SELECT operator_request.id, operator_request.operator_id, operator_request.status, operator_request.created_at
FROM operator_request
WHERE operator_request.status = 'PENDING';
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION find_all_operator_requests_by_user(op_id INT)
RETURNS TABLE(id INT, operator_id INT, status VARCHAR, created_at TIMESTAMP) AS $$
BEGIN
RETURN QUERY
SELECT operator_request.id, operator_request.operator_id, operator_request.status, operator_request.created_at
FROM operator_request
WHERE operator_request.operator_id = op_id;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION find_all_in_queue(q_id INT)
RETURNS TABLE(
    id INT,
    description TEXT,
    expirationdate DATE,
    name VARCHAR,
    priority INT,
    productstate VARCHAR,
    customer_id INT,
    location_id INT,
    product_type_id INT,
    queue_id INT,
    supplier_id INT
) AS $$
BEGIN
RETURN QUERY
SELECT
    product.id,
    product.description,
    product.expirationdate,
    product.name,
    product.priority,
    product.productstate,
    product.customer_id,
    product.location_id,
    product.product_type_id,
    product.queue_id,
    product.supplier_id
FROM product
WHERE product.queue_id = q_id;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION find_queues_by_sorting_station(in_station_id INT)
RETURNS TABLE(
    id INT,
    capacity INT,
    station_id INT
) AS $$
BEGIN
RETURN QUERY
SELECT
    queue.id,
    queue.capacity,
    queue.station_id
FROM queue
WHERE queue.station_id = in_station_id;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION find_user_by_username(u_username VARCHAR)
RETURNS TABLE(
    id INT,
    username VARCHAR,
    password VARCHAR,
    role VARCHAR
) AS $$
BEGIN
RETURN QUERY
SELECT
    app_user.id,
    app_user.username,
    app_user.password,
    app_user.role
FROM app_user
WHERE app_user.username = u_username;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION find_warehouse_operator_by_user_id(u_id INT)
RETURNS TABLE(
    id INT,
    status VARCHAR,
    app_user_id INT,
    product_type INT,
    worker_id INT
) AS $$
BEGIN
RETURN QUERY
SELECT
    warehouse_operator.id,
    warehouse_operator.status,
    warehouse_operator.app_user_id,
    warehouse_operator.product_type,
    warehouse_operator.worker_id
FROM warehouse_operator
WHERE warehouse_operator.app_user_id = u_id;
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

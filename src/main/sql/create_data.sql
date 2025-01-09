INSERT INTO Warehouse (name, address)
SELECT
    'Warehouse #' || i, 'Address #' || i
FROM
    generate_series(1, 100000) i;

INSERT INTO App_User (username, password_hash, role)
SELECT
    'User #' || i, 'Password #' || i*10, 'manager'
FROM
    generate_series(1, 1000000) i;

INSERT INTO Worker (first_name, last_name, birth_date, hire_date)
SELECT
    'Worker #' || i, 'Last name #' || i, '2000-01-01', CURRENT_DATE
FROM
    generate_series(1, 1000000) i;

INSERT INTO Manager (app_user_id, worker_id, warehouse_id)
SELECT
    app_user_id, worker_id, warehouse_id
FROM
    (SELECT
         i AS app_user_id,
         i AS worker_id,
         i AS warehouse_id
     FROM generate_series(1, 100000) i) AS temp;

INSERT INTO Product_Type (name)
SELECT
    'Product Type #' || i
FROM
    generate_series(1, 1000000) i;

-- Insert into Location
INSERT INTO Location (name, description, location_row)
SELECT
    'Location #' || i,
    'Description for Location #' || i,
    i
FROM
    generate_series(1, 1000000) i;

-- Insert into Partners
INSERT INTO Partners (name, email, phone_number, address)
SELECT
    'Partner #' || i,
    'partner' || i || '@example.com',
    '153-456-789' || i,
    'Address for Partner #' || i
FROM
    generate_series(1, 1000000) i;

INSERT INTO Queue (capacity, sorting_station_id)
SELECT
    i, i
FROM
    generate_series(1, 1000000) i;

INSERT INTO Shelf (location_id)
SELECT
    i
FROM
    generate_series(1, 1000000) i;

INSERT INTO Goods_Management_Center (warehouse_id, location_id)
SELECT
    i, i
FROM
    generate_series(1, 1000000) i;

INSERT INTO Sorting_Station (management_center_id, location_id, capacity, sort_time_seconds)
SELECT
    i, i, 10, 10
FROM
    generate_series(1, 1000000) i;

INSERT INTO Warehouse_Operator (app_user_id, worker_id)
SELECT
    i, i
FROM
    generate_series(1, 1000000) i;

INSERT INTO Product (product_type_id, location_id, supplier, customer, queue_id, name, description, expiration_date, product_state, priority)
SELECT
    i,
    i,
    i,
    i,
    i,
    'Product #' || i,
    'Description for Product #' || i,
    CURRENT_DATE,
    'stored',
    i
FROM
    generate_series(1, 1000000) i;

INSERT INTO Product_In_Queue (product_id, queue_id)
SELECT
    i,
    i
FROM
    generate_series(1, 1000000) i;

INSERT INTO Loaders_And_Shelves (worker_id, shelf_id)
SELECT
    i,
    i
FROM
    generate_series(1, 1000000) i;

INSERT INTO Operator_Request (operator_id, status)
SELECT
    i,
    'pending'
FROM
    generate_series(1, 1000000) i;

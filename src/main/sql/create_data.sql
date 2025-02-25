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

INSERT INTO App_User (username, password, role)
SELECT
    'User #' || i, 'Password #' || i*10, 'USER'
FROM
    generate_series(1, 100000) i;

INSERT INTO Product (product_type_id, location_id, supplier_id, customer_id, queue_id, name, description, expiration_date, product_state, priority)
SELECT
    i,
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

INSERT INTO Warehouse_Operator (app_user_id, product_type_id)
SELECT
    i, i
FROM
    generate_series(1, 100000) i;

INSERT INTO Loaders_And_Shelves (worker_id, shelf_id)
SELECT
    i,
    i
FROM
    generate_series(1, 100000) i;

\c warehouse_management

DROP TRIGGER IF EXISTS check_operator_request_initial_status ON Operator_Request;
DROP TRIGGER IF EXISTS check_operator_request_status_transition ON Operator_Request;

DROP FUNCTION IF EXISTS check_initial_status();
DROP FUNCTION IF EXISTS check_status_transition();

DROP TYPE IF EXISTS request_state_type;
DROP TYPE IF EXISTS product_state_type;
DROP TYPE IF EXISTS user_role;

DROP TABLE IF EXISTS Operator_Request CASCADE;
DROP TABLE IF EXISTS Loaders_And_Shelves CASCADE;
DROP TABLE IF EXISTS Product_In_Queue CASCADE;
DROP TABLE IF EXISTS Product CASCADE;
DROP TABLE IF EXISTS Queue CASCADE;
DROP TABLE IF EXISTS Shelf CASCADE;
DROP TABLE IF EXISTS Sorting_Station CASCADE;
DROP TABLE IF EXISTS Goods_Management_Center CASCADE;
DROP TABLE IF EXISTS Warehouse CASCADE;
DROP TABLE IF EXISTS Manager CASCADE;
DROP TABLE IF EXISTS Warehouse_Operator CASCADE;
DROP TABLE IF EXISTS Worker CASCADE;
DROP TABLE IF EXISTS Product_Type CASCADE;
DROP TABLE IF EXISTS Location CASCADE;
DROP TABLE IF EXISTS Partners CASCADE;
DROP TABLE IF EXISTS App_User CASCADE;

\c postgres

DROP DATABASE IF EXISTS warehouse_management;

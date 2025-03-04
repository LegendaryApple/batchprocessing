DROP TABLE IF EXISTS sys_user;
DROP TABLE IF EXISTS my_transactions;

CREATE TABLE sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) UNIQUE NOT NULL,
    role VARCHAR(255) NOT NULL
);

CREATE TABLE my_transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(20) NOT NULL,
    trx_amount DECIMAL(20,2) NOT NULL,
    description VARCHAR(255),
    trx_date DATE NOT NULL,
    trx_time TIME NOT NULL,
    customer_id BIGINT NOT NULL,
    version BIGINT DEFAULT 0
);

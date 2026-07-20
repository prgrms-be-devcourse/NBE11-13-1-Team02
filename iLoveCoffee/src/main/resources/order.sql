CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    post_number VARCHAR(20) NOT NULL,
    address VARCHAR(255) NOT NULL,
    shipment_status VARCHAR(20) NOT NULL,
    order_status VARCHAR(20) NOT NULL,
    total_price BIGINT NOT NULL,
    order_at DATETIME,
    dispatch_at DATETIME,
    delivered_at DATETIME

);